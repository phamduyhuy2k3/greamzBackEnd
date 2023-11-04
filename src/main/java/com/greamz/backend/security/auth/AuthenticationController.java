package com.greamz.backend.security.auth;

import com.greamz.backend.service.UserService;
import com.greamz.backend.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        if(authenticationResponse!= null){
            CookieUtils.addCookie(response,"accessToken",authenticationResponse.getAccessToken());
            if(request.isRememberMe()){
                CookieUtils.addCookie(response,"refreshToken",authenticationResponse.getRefreshToken());
            }
        }
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try{
            AuthenticationResponse authenticationResponse= service.refreshToken(request);
            if(authenticationResponse!= null){
                CookieUtils.addCookie(response,"accessToken",authenticationResponse.getAccessToken());
                return ResponseEntity.ok().body(authenticationResponse);
            }else {
                return ResponseEntity.status(401).body(null);
            }
        }catch (ExpiredJwtException e){
            CookieUtils.deleteCookie(request,response,"accessToken");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }
}
