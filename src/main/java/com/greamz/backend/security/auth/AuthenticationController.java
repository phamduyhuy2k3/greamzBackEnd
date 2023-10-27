package com.greamz.backend.security.auth;


import com.greamz.backend.service.UserService;
import com.greamz.backend.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Boolean> checkEmailIsExisted(@PathVariable("email") String email) {
        return ResponseEntity.ok().body(userService.isEmailExists(email));
    }

    @GetMapping("/validate-username/{username}")
    public ResponseEntity<Boolean> checkUsernameIsExisted(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.isUsernameExists(username));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Validated RegisterRequest request
    ) {
        AuthenticationResponse authenticationResponse = service.register(request);
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,HttpServletResponse response
    ) {
        AuthenticationResponse authenticationResponse = service.authenticate(request);
        CookieUtils.addCookie(response,"accessToken",authenticationResponse.getAccessToken());
        CookieUtils.addCookie(response,"refreshToken",authenticationResponse.getRefreshToken());
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}
