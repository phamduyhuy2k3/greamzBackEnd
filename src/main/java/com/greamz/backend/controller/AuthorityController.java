package com.greamz.backend.controller;

import com.greamz.backend.enumeration.Role;
import com.greamz.backend.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;
    private record AuthorityRequest(Integer userId, Role role) {
    }
    @DeleteMapping("/delete")
    public void deleteAuthorityByUserIdAndRole(@RequestParam("userId") Integer userId,@RequestParam("role") String role){

        authorityService.deleteAuthorityByUserIdAndRole(userId, role);
    }
    @PostMapping("/save")
    public void saveAuthority(@RequestBody AuthorityRequest authorityRequest){
        authorityService.saveAuthority(authorityRequest.userId(),authorityRequest.role());
    }
    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(authorityService.findAll());
    }

}
