package com.greamz.backend.controller;

import com.greamz.backend.annotations.CurrentUser;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.security.auth.AuthenticationRequest;
import com.greamz.backend.security.auth.AuthenticationResponse;
import com.greamz.backend.security.auth.AuthenticationService;
import com.greamz.backend.security.auth.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AuthenticationService authenticationService;
    @GetMapping({"/", "/index", "/home"})
    public String index() {
        return "redirect:/dashboard";
    }
    @GetMapping({"/dashboard"})
    public String dashboard(@CurrentUser AccountModel accountModel) {
        if(accountModel == null){
            return "redirect:/sign-in";
        }
        return "index";
    }
    @GetMapping("/sign-in")
    public String login(){
        RegisterRequest registerRequest = new RegisterRequest();

        return "sign-in";
    }

//    @PostMapping("/sign-in")
//    public String actionLogin(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password,
//            @RequestParam(value = "remember",required = false,defaultValue = "false") boolean remember,
//            HttpServletResponse response){
//        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
//        authenticationRequest.setUsername(username);
//        authenticationRequest.setPassword(password);
//        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
//        response.addHeader("Authorization", "Bearer " + authenticationResponse.getAccessToken());
//        response.addCookie(new Cookie("refreshToken", authenticationResponse.getRefreshToken()));
//        return "forward:/dashboard";
//    }

}
