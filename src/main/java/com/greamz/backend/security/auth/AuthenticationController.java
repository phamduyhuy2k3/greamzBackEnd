package com.greamz.backend.security.auth;

import com.greamz.backend.service.EmailService;
import com.greamz.backend.service.ResetPasswordService;
import com.greamz.backend.service.UserService;
import com.greamz.backend.util.CookieUtils;
import com.greamz.backend.util.EncryptionUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserService userService;
    private final ResetPasswordService resetPasswordService;
    private final EmailService emailService;

    @GetMapping("/validate-email/{email}")
    public ResponseEntity<Boolean> checkEmailIsExisted(@PathVariable("email") String email) {
        return ResponseEntity.ok().body(userService.isEmailExists(email));
    }

    @GetMapping("/validate-email-provider-local/{email}")
    public ResponseEntity<Boolean> checkEmailIsExistedProviderLocal(@PathVariable("email") String email) {
        return ResponseEntity.ok().body(userService.isEmailExistsProviderLocal(email));
    }

    @GetMapping("/validate-username/{username}")
    public ResponseEntity<Boolean> checkUsernameIsExisted(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.isUsernameExists(username));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Validated RegisterRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authenticationResponse = service.register(request);

        return ResponseEntity.ok()
                .headers(
                        CookieUtils
                                .addCookieToResponse(
                                        response,
                                        Map.of(
                                                "access_token", authenticationResponse.getAccessToken(),
                                                "refresh_token", authenticationResponse.getRefreshToken()

                                        ),
                                        Duration.ofMinutes(authenticationResponse.getAccessTokenExpiresIn())
                                )
                )
                .body(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request, HttpServletResponse response, HttpServletRequest servletRequest
    ) {
        AuthenticationResponse authenticationResponse = service.authenticate(request, servletRequest);

        return ResponseEntity.ok()
                .headers(
                        CookieUtils
                                .addCookieToResponse(
                                        response,
                                        Map.of(
                                                "access_token", authenticationResponse.getAccessToken(),
                                                "refresh_token", authenticationResponse.getRefreshToken()

                                        ),
                                        Duration.ofMinutes(authenticationResponse.getAccessTokenExpiresIn())
                                )
                )
                .body(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        try {
            AuthenticationResponse authenticationResponse = service.refreshToken(request,response);
            if (authenticationResponse != null) {

                ResponseCookie cookieaccess_token = ResponseCookie.from("access_token", authenticationResponse.getAccessToken())
                        .maxAge(Duration.ofMinutes(authenticationResponse.getAccessTokenExpiresIn()))
                        .httpOnly(true)
                        .path("/")
                        .build();
                ResponseCookie cookierefresh_token = ResponseCookie.from("refresh_token", authenticationResponse.getRefreshToken())
                        .maxAge(Duration.ofMinutes(authenticationResponse.getRefreshTokenExpiresIn()))
                        .httpOnly(true)
                        .path("/")
                        .build();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Set-Cookie", cookieaccess_token.toString());
                headers.add("Set-Cookie", cookierefresh_token.toString());
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(authenticationResponse);
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } catch (ExpiredJwtException e) {
            CookieUtils.deleteCookie(request, response, "access_token");
            CookieUtils.deleteCookie(request, response, "refresh_token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @PostMapping("/send-otp-email")
    public ResponseEntity<String> verifyEmail(@RequestBody OtpRequest otpRequest) {
        try {
            emailService.sendEmailConfirmAccount(otpRequest);
            return ResponseEntity.status(HttpStatus.OK).body("The verification code has been sent to your email");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error when send email");
        }
    }

    @PostMapping("/send-reset-password-email")
    public ResponseEntity<String> resetPassword(@RequestParam String email, HttpServletRequest request) {
        try {
            resetPasswordService.sendRequestResetPasswordEmail(email, request);
            return ResponseEntity.ok().body("Check your email to reset password");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when send email");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found account with email: " + email + " or the email is registered by another login method");
        }

    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> checkTokenResetPassword(@RequestParam String token, HttpServletRequest request) {
        System.out.println("token: " + token);
        if (resetPasswordService.checkResetPasswordTokenIsValid(token)) {
            return ResponseEntity.ok().body("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
        try {
            resetPasswordService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getConfirmPassword(), request);
            return ResponseEntity.ok().body("Reset password success");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");
        }

    }
}
