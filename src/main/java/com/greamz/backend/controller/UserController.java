package com.greamz.backend.controller;


import com.greamz.backend.dto.UserProfileDTO;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal AccountModel username){
//hahahahâhahha
        return ResponseEntity.ok().body(userService.getProfile(username.getUsername()));
    }


}
