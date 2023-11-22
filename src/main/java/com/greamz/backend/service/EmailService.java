package com.greamz.backend.service;

import com.greamz.backend.security.auth.OtpRequest;
import com.greamz.backend.util.EncryptionUtil;
import com.greamz.backend.util.GlobalState;
import com.greamz.backend.util.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final ThymeleafService thymeleafService;

    public void sendEmail() throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    }

    public void sendResetPasswordEmail(String email, String token, HttpServletRequest request) throws MessagingException, IOException {
        String tokenEncrypt = EncryptionUtil.encrypt(token);
        if (tokenEncrypt == null) {
            throw new IOException("Token encrypt is null");
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("Reset password");
        Context context = new Context();
        String url = GlobalState.FRONTEND_URL + "/reset-password-step-2?token=" + tokenEncrypt;
        context.setVariable("resetLink", url);
        String emailContent = thymeleafService.setContent(context, "resetPassword");
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
        System.out.println("Send email success");


    }

    public void sendEmailConfirmAccount(OtpRequest otpRequest) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(otpRequest.getEmail());
        helper.setSubject("Verify your email");
        Context context = new Context();
        context.setVariable("code", otpRequest.getOtp());
        String emailContent = thymeleafService.setContent(context, "verifyEmail");
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
        System.out.println("Send email success");
    }
}
