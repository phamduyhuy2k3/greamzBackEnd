package com.greamz.backend.controller;


import com.greamz.backend.dto.account.ChangeEmail;
import com.greamz.backend.dto.account.ChangePassword;
import com.greamz.backend.dto.account.UserProfileDTO;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.security.auth.AuthenticationResponse;
import com.greamz.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal){

        return ResponseEntity.ok().body(userService.getProfile(userPrincipal.getUsername()));
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody @Validated ChangePassword changePassword,
            @AuthenticationPrincipal UserPrincipal userPrincipal){
        String result= userService.changePassword(changePassword,userPrincipal);
        if(!result.equals("Change password successfully")){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok().body(result);
    }
    @PostMapping("/change-email-step-1")
    public ResponseEntity<String> changeEmail(
            @RequestBody @Validated ChangeEmail changeEmail,
            @AuthenticationPrincipal UserPrincipal userPrincipal){
        String result= userService.changeEmailStep1(changeEmail,userPrincipal);
        if(!result.equals("Success")){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok().body(result);
    }
    @PostMapping("/change-email-step-2")
    public ResponseEntity<AuthenticationResponse> changeEmailStep2(
            @RequestBody @Validated ChangeEmail changeEmail,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletRequest request
            ){
        System.out.println("dsadsadsadsadsad");
        AuthenticationResponse result= userService.changeEmailStep2(changeEmail,userPrincipal,request);
        if(result==null){
            System.out.println("dsadsadsadsadsad");
            return ResponseEntity.badRequest().build();

        }
        System.out.println("454");
        return ResponseEntity.ok().body(result);
    }
}
