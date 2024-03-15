package com.greamz.backend.security;


import com.greamz.backend.config.JwtAuthenticationFilter;
import com.greamz.backend.config.LogoutService;
import com.greamz.backend.security.oauth2.*;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RedirectStrategy redirectStrategy;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutService logoutHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    public static final String[] WHITE_LIST_URLS = {"/api/v1/auth/**",
            "/oauth2/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/assets/**",
            "/component/**",
            "/pages/**",
            "/static/**",
            "/sign-in",
    };

    @Bean
    public SecurityFilterChain securityFilterOauth2(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(httpSecurityHttpBasicConfigurer -> {
                    httpSecurityHttpBasicConfigurer.disable();
                })

                .authorizeRequests(expressionInterceptUrlRegistry -> {
                    expressionInterceptUrlRegistry
                            .requestMatchers("/api/user/currentUser").authenticated()
                            .requestMatchers("/api/v1/user/**","/api/user/**").hasAnyAuthority("ADMIN")
                            .requestMatchers("/").hasAnyAuthority("ADMIN", "MANAGER")
                            .requestMatchers(DELETE, "/api/v1/game/**").hasAnyAuthority("ADMIN","MANAGER")
                            .requestMatchers(
                                    GET,
                                    "/api/v1/game/**",
                                    "/api/v1/category/**",
                                    "/api/v1/platform/**",
                                    "/api/v1/checkout/**",
                                    "/api/v1/payment/vnpay/**",
                                    "/api/v1/review/**",
                                    "/api/v1/dashboard/**")
                            .permitAll()
                            .requestMatchers(WHITE_LIST_URLS).permitAll()
                            .anyRequest().authenticated();

                })
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Use stateless sessions for API
                })
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer
                            .authenticationEntryPoint(new RestAuthenticationEntryPoint()) // Use a custom entry point
                            .accessDeniedHandler(new RestAccessDeniedHandler()); // Use a custom access denied handler
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT and other filters as needed

                .authenticationProvider(authenticationProvider) // Authentication provider

                .oauth2Login(oauth2Login ->
                        oauth2Login

                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint
                                                .baseUri("/oauth2/authorize")
                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                                )
                                .redirectionEndpoint(redirectionEndpoint ->
                                        redirectionEndpoint
                                                .baseUri("/oauth2/callback/*")
                                )
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer
                            .logoutUrl("/api/v1/auth/logout")
                            .addLogoutHandler(logoutHandler)
                            .deleteCookies("access_token", "refresh_token")
                            .logoutSuccessHandler((request, response, authentication) -> {
                                response.setStatus(200);

                            });

                });

        return http.build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            redirectStrategy.sendRedirect(request, response, "/");
        };
    }
    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            redirectStrategy.sendRedirect(request, response, "/sign-in?error");
        };
    }
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        config.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000", "https://greamz.games", "https://www.greamz.games", "https://admin.greamz.games", "https://main.dlqgfk9hgo94w.amplifyapp.com"));
        config.setAllowedHeaders(Arrays.asList(
                ORIGIN,
                CONTENT_TYPE,
                ACCEPT,
                AUTHORIZATION,
                ACCESS_CONTROL_ALLOW_ORIGIN
        ));
        config.setAllowedMethods(Arrays.asList(
                GET.name(),
                POST.name(),
                DELETE.name(),
                PUT.name(),
                PATCH.name()
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }


}