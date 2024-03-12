package com.greamz.backend.config;


import com.greamz.backend.repository.ITokenRepo;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.security.auth.AuthenticationResponse;
import com.greamz.backend.security.auth.AuthenticationService;
import com.greamz.backend.service.CustomUserDetailsService;
import com.greamz.backend.util.CookieUtils;
import com.greamz.backend.util.EncryptionUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("Request URL: " + request.getServletPath());

        if (request.getServletPath().contains("/api/v1/auth/") || request.getServletPath().contains("/sign-in")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader == null ? "" : authHeader.substring(7);
        if (request.getServletPath().equals("/") || !request.getServletPath().contains("/api")) {
            if (CookieUtils.getCookie(request, "access_token").isPresent()) {
                jwt = Objects.requireNonNull(CookieUtils.getCookie(request, "access_token")).get().getValue();
                isValid(jwt, request, response, filterChain, true);
            } else if(CookieUtils.getCookie(request, "refresh_token").isPresent()) {
                autoRefreshToken(request, response, filterChain);
            }else {
                filterChain.doFilter(request, response);
                return;
            }
            return;

        }
        if (request.getServletPath().contains("/api")) {
            if (CookieUtils.getCookie(request, "access_token").isPresent()) {
                jwt = Objects.requireNonNull(CookieUtils.getCookie(request, "access_token")).get().getValue();
                isValid(jwt, request, response, filterChain, true);
            } else if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                filterChain.doFilter(request, response);
                return;
            } else {
                if (jwt.equals("undefined") || jwt.equals("null") || jwt.equals("")) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authorized");
                    return;
                }
                isValid(jwt, request, response, filterChain, false);
            }
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void isValid(
            String token,
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            boolean isDashBoard
    ) throws ServletException, IOException {
        try {

            final String userName;
            userName = jwtService.extractUsername(token);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserPrincipal userDetails = this.userDetailsService.loadUserByUsername(userName);
                if (jwtService.isTokenValid(token, userDetails)) {
                    if (userDetails.isEnabled()) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        filterChain.doFilter(request, response);
                    } else {

                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your account is temporarily locked, please contact admin to unlock your account");

                    }
                }
            } else {
                logger.info("jwt is not valid");
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {

            if (isDashBoard) {
                autoRefreshToken(request, response, filterChain);
                return;
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired");

        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void autoRefreshToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var refreshOp= CookieUtils.getCookie(request,"refresh_token");
        if(refreshOp.isEmpty()){
            response.sendRedirect("/sign-in");
            return;
        }
        String refreshToken = refreshOp.get().getValue();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + refreshToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<AuthenticationResponse> authenticationResponse;
        try {
            authenticationResponse = restTemplate.postForEntity(
                    "http://localhost:8080/api/v1/auth/refresh-token?isDashboard=true",
                    entity,
                    AuthenticationResponse.class
            );
        } catch (HttpClientErrorException e1) {
            CookieUtils.deleteCookie(request, response, "access_token");
            CookieUtils.deleteCookie(request, response, "refresh_token");
            response.sendRedirect("/sign-in");
            return;
        }
        if (authenticationResponse.getBody() != null) {
            logger.info("refresh token: " + authenticationResponse.getBody().getRefreshToken());
            String newAccessToken = authenticationResponse.getBody().getAccessToken();
            if (newAccessToken != null) {
                CookieUtils.addCookie(response, "access_token", newAccessToken, Duration.ofMinutes(jwtService.getJwtExpiration()));
                isValid(newAccessToken, request, response, filterChain, true);
            }
        } else {
            CookieUtils.deleteCookie(request, response, "access_token");
            CookieUtils.deleteCookie(request, response, "refresh_token");
            response.sendRedirect("/sign-in");

        }
    }
}