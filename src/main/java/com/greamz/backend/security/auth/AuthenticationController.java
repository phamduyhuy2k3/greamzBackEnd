package com.greamz.backend.security.auth;


import com.greamz.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserService userService;
    @GetMapping("/validate-email/{email}")
    public ResponseEntity<Boolean> checkEmailIsExisted(@PathVariable("email") String email){
        return ResponseEntity.ok().body(userService.isEmailExists(email));
    }
    @GetMapping("/validate-username/{username}")
    public ResponseEntity<Boolean> checkUsernameIsExisted(@PathVariable("username") String username){
        return ResponseEntity.ok().body(userService.isUsernameExists(username));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Validated RegisterRequest request
    ) {
        AuthenticationResponse authenticationResponse=service.register(request);
        var cookieAccessToken= ResponseCookie.fromClientResponse("accessToken",authenticationResponse.getAccessToken())
                .httpOnly(true)
                .maxAge(7*24*60*60)
                .secure(true)
                .path("http://localhost:3000")
                .build();
        var cookieRefreshToken= ResponseCookie.fromClientResponse("refreshToken",authenticationResponse.getRefreshToken())
                .httpOnly(true)
                .maxAge(7*24*60*60)
                .secure(true)
                .path("http://localhost:3000")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookieAccessToken.toString(),cookieRefreshToken.toString()).body(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse authenticationResponse=service.authenticate(request);

        var cookieAccessToken= ResponseCookie.fromClientResponse("accessToken",authenticationResponse.getAccessToken())
                .httpOnly(true)
                .maxAge(7*24*60*60)
                .secure(true)
                .path("http://localhost:3000")
                .build();
         var cookieRefreshToken= ResponseCookie.fromClientResponse("refreshToken",authenticationResponse.getRefreshToken())
                .httpOnly(true)
                .maxAge(7*24*60*60)
                .secure(true)
                .path("http://localhost:3000")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookieAccessToken.toString(),cookieRefreshToken.toString()).body(authenticationResponse);
    }
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}
