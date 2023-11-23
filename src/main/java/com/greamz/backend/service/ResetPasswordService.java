package com.greamz.backend.service;

import com.greamz.backend.config.JwtService;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.util.EncryptionUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordService {
    private final IAccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    public void sendRequestResetPasswordEmail(String email, HttpServletRequest request) throws MessagingException, NoSuchElementException, IOException {
        AccountModel accountModel = accountRepo.findByUserNameOrEmail(email).orElseThrow(() -> new NoSuchElementException("Not found account with email: " + email));
        String token= jwtService.generateTokenForResetPassword(UserPrincipal.create(accountModel));
        emailService.sendResetPasswordEmail(email,token,request);
    }
    public void resetPassword(String token,String newPassword, HttpServletRequest request) {
        String tokenDecrypt= EncryptionUtil.decrypt(token);
        String email= jwtService.extractUsername(tokenDecrypt);
        AccountModel accountModel = accountRepo.findByUserNameOrEmail(email).orElseThrow(() -> new RuntimeException("Not found account with email: " + email));
        accountModel.setPassword(passwordEncoder.encode(newPassword));
        accountRepo.save(accountModel);
    }
    public boolean checkResetPasswordTokenIsValid(String token) {

        String tokenDecrypt= EncryptionUtil.decrypt(token);
        String username= jwtService.extractUsername(tokenDecrypt);
        log.info("username: "+username);
        return accountRepo.existsByEmail(username);
    }
}
