package com.greamz.backend.security.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greamz.backend.config.JwtService;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.enumeration.TokenType;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Authority;
import com.greamz.backend.model.Token;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.repository.ITokenRepo;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final IAccountRepo repository;
    private final ITokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Authority authority = new Authority();
        authority.setRole(Role.USER);

        request.setAuthorities(List.of(authority));
        var user = AccountModel.builder()

                .fullname(request.getFullname())
                .username(request.getUsername())
                .email(request.getEmail())
                .isEnabled(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        List<Authority> authorities = request.getAuthorities().stream()
                .map(authorityString -> Authority.builder()
                        .role(authorityString.getRole())
                        .account(user)
                        .build())
                .collect(Collectors.toList());

        user.setAuthorities(authorities);
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();


    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUserNameOrEmail(request.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void saveUserToken(AccountModel user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AccountModel user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);

    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) throws ExpiredJwtException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        final String jwt = authHeader.substring(7);
        final String username= jwtService.extractUsernameThatTokenExpired(jwt);
        if (username != null) {
            var user = this.repository.findByUserNameOrEmail(username)
                    .orElseThrow();
            log.info("user:"+user.getId());
            var refreshToken = this.tokenRepository.findByUser_Id(user.getId())
                    .orElseThrow().getToken();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                log.info("Refresh token successfully");
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .build();
            }
        }
        return null;
    }
}
