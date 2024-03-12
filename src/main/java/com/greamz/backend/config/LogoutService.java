package com.greamz.backend.config;


import com.greamz.backend.repository.ITokenRepo;
import com.greamz.backend.service.AccountService;
import com.greamz.backend.util.CookieUtils;
import com.greamz.backend.util.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final ITokenRepo tokenRepository;
    private final JwtService jwtService;
    private final AccountService accountService;

    @Override
    @Transactional
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        String jwt = "";
        if (authHeader == null) {
            return;
        }
        final boolean isRequestFromDashboard = authHeader.startsWith("Dashboard");
        if (isRequestFromDashboard) {
            if (CookieUtils.getCookie(request, "refresh_token").isPresent()) {
                jwt = CookieUtils.getCookie(request, "refresh_token").get().getValue();
            }

        }
        if (authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        try {
            var storedToken = tokenRepository.findByToken( jwt).orElseThrow(() -> new RuntimeException("Token not found"));
            if (storedToken != null) {
                tokenRepository.delete(storedToken);
                tokenRepository.deleteAllByUser_IdAndExpiredOrRevoked(storedToken.getUser().getId(), true, true);
                SecurityContextHolder.clearContext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
