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

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final ITokenRepo tokenRepository;
    private final JwtService jwtService;
    private final AccountService accountService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = EncryptionUtil.decrypt(authHeader.substring(7));
        String username = jwtService.extractUsernameThatTokenExpired(jwt);
        try {
            var user = accountService.findByUserNameOrEmail(username).orElseThrow(() -> new RuntimeException("User not found: " + username));

            var storedToken = tokenRepository.findByUser_Id(user.getId()).orElseThrow(() -> new RuntimeException("Token not found"));
            if (storedToken != null) {
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
                CookieUtils.deleteCookie(request, response, "accessToken");
                SecurityContextHolder.clearContext();
            }
        }catch (RuntimeException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
