package com.greamz.backend.security.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greamz.backend.config.JwtService;
import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.enumeration.TokenType;
import com.greamz.backend.model.AccountAuthProvider;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Token;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.repository.ITokenRepo;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.util.EncryptionUtil;
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
import java.util.Optional;
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

        var user = AccountModel.builder()
                .fullname(request.getFullname())
                .username(request.getUsername())
                .email(request.getEmail())
                .emailVerified(true)
                .role(Role.USER)
                .isEnabled(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var authProvider = AccountAuthProvider.builder()
                .provider(AuthProvider.local)
                .build();
        user.setAuthProviders(List.of(authProvider));
        var savedUser = repository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);
        var jwtToken = jwtService.generateToken(userPrincipal);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);
        saveUserToken(savedUser, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(jwtService.getJwtExpiration())
                .refreshTokenExpiresIn(jwtService.getRefreshExpiration())
                .build();


    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest servletRequest) {
        System.out.println(request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var savedUser = repository.findByUserNameOrEmail(request.getUsername())
                .orElseThrow();
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);
        var jwtToken = jwtService.generateToken(userPrincipal);
        var refreshToken = jwtService.generateRefreshToken(userPrincipal);
        revokeAllUserTokens(savedUser);
        saveUserToken(savedUser, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .accessTokenExpiresIn(jwtService.getJwtExpiration())
                .refreshTokenExpiresIn(jwtService.getRefreshExpiration())
                .refreshToken(refreshToken)
                .build();
    }

    public void authenticateOauth2(UserPrincipal authentication) {
        var refreshToken = jwtService.generateRefreshToken(authentication);
        var savedUser = repository.findByUserNameOrEmail(authentication.getEmail())
                .orElseThrow();
        revokeAllUserTokens(savedUser);
        saveUserToken(savedUser, refreshToken);
    }

    public void saveUserToken(AccountModel user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(AccountModel user) {
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
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ExpiredJwtException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String jwt = authHeader.substring(7);
        if (jwt.isEmpty()) {
            return null;
        }
        Optional<Token> tokenOptional = tokenRepository.findByTokenAndAndExpiredIsFalseAndRevokedIsFalse(jwt);
        if (tokenOptional.isEmpty()) {
            return null;
        } else {
            Token token = tokenOptional.get();
            if (token.getUser().getId() != null) {

                var user = this.repository.findById(token.getUser().getId())
                        .orElseThrow();
            if (request.getParameter("isDashboard") != null && !request.getParameter("isDashboard").isEmpty()) {

                var refreshToken = this.tokenRepository.findByUser_IdAndToken(user.getId(), token.getToken())
                        .orElseThrow().getToken();
                if (jwtService.isTokenValid(refreshToken, UserPrincipal.create(user))) {
                    UserPrincipal userPrincipal = UserPrincipal.create(user);
                    var accessToken = jwtService.generateToken(userPrincipal);
                    log.info("Refresh token successfully");
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .refreshTokenExpiresIn(jwtService.getRefreshExpiration())
                            .accessTokenExpiresIn(jwtService.getJwtExpiration())
                            .build();
                }
            } else {

                    if (jwtService.isTokenValid(token.getToken(), UserPrincipal.create(user))) {
                        var accessToken = jwtService.generateToken(UserPrincipal.create(user));
                        var authResponse = AuthenticationResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(token.getToken())
                                .refreshTokenExpiresIn(jwtService.getRefreshExpiration())
                                .accessTokenExpiresIn(jwtService.getJwtExpiration())
                                .build();
                        log.info("Refresh token successfully 2");
                        return authResponse;
                    }
                }
            }
        }


        return null;
    }

    public Token findTokenByUserAndToken(String token, Integer id) {
        return tokenRepository.findByUser_IdAndToken(id, token).orElseThrow();
    }
}
