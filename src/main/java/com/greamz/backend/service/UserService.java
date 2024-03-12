package com.greamz.backend.service;


import com.greamz.backend.dto.account.ChangeEmail;
import com.greamz.backend.dto.account.ChangePassword;
import com.greamz.backend.dto.account.UserProfileDTO;
import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.security.auth.AuthenticationRequest;
import com.greamz.backend.security.auth.AuthenticationResponse;
import com.greamz.backend.security.auth.AuthenticationService;
import com.greamz.backend.util.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IAccountRepo accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    public UserProfileDTO getProfile(String username){
        AccountModel accountModel=accountRepository
                .findByUserNameOrEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

        return Mapper.mapObject(accountModel, UserProfileDTO.class);
    }
    public Boolean isUsernameExists(String username){
        return accountRepository.existsByUsername(username);
    }
    public Boolean isEmailExists(String email){
        return accountRepository.existsByEmail(email);
    }
    public Boolean isEmailExistsProviderLocal(String email){
        return accountRepository.existsByEmail(email) && accountRepository.findByUsername(email).get().getAuthProviders().stream().noneMatch(authProvider -> authProvider.getProvider().equals(AuthProvider.local));
    }
    public String changePassword(ChangePassword changePassword, UserPrincipal userPrincipal){
        AccountModel accountModel=accountRepository.findByUserNameOrEmail(userPrincipal.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(!passwordEncoder.matches(changePassword.getOldPassword(), accountModel.getPassword())){
            return "Old password is not correct";
        }
        accountModel.setPassword(passwordEncoder.encode(changePassword.getConfirmPassword()));
        accountRepository.save(accountModel);
        return "Change password successfully";
    }

    public String changeEmailStep1(ChangeEmail changeEmail, UserPrincipal userPrincipal) {
        AccountModel accountModel=accountRepository.findByUserNameOrEmail(userPrincipal.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        if(!passwordEncoder.matches(changeEmail.getCurrentPassword(), accountModel.getPassword())){
            return "Current password is not correct";
        }
        return "Success";
    }
    public AuthenticationResponse changeEmailStep2(ChangeEmail changeEmail, UserPrincipal userPrincipal, HttpServletRequest request) {
        AccountModel accountModel=accountRepository.findByUserNameOrEmail(userPrincipal.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        if(!passwordEncoder.matches(changeEmail.getCurrentPassword(), accountModel.getPassword())){
            return null;
        }
        AuthenticationResponse reposne= authenticationService.authenticate(AuthenticationRequest.builder()
                .username(accountModel.getUsername())
                .password(changeEmail.getCurrentPassword())
                .build(),request );

        accountModel.setEmail(changeEmail.getNewEmail());
        accountRepository.save(accountModel);
        return reposne;
    }
}
