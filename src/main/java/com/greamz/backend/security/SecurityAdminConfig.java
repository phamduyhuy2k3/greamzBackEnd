package com.greamz.backend.security;


import com.greamz.backend.config.JwtAuthenticationFilter;
import com.greamz.backend.security.oauth2.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
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
public class SecurityAdminConfig {


    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;
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
            "/api/v1/game/**",
            "/"
    };

    @Bean
//    @Order(324324)
    SecurityFilterChain securityFilterOauth2(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(httpSecurityHttpBasicConfigurer -> {
                    httpSecurityHttpBasicConfigurer.disable();
                })
                .authorizeRequests(expressionInterceptUrlRegistry -> {
                    expressionInterceptUrlRegistry
                            .requestMatchers(request -> {
                                switch (request.getMethod().toLowerCase()) {
                                    case "post":
                                    case "put":
                                    case "delete":
                                        return request.getServletPath().contains("/api/v1/game/");
                                    default:
                                        return false;
                                }
                            }).hasAnyAuthority("ADMIN", "EMPLOYEE", "MANAGER")
                            .requestMatchers(request -> {
                                switch (request.getMethod().toLowerCase()) {
                                    case "post":
                                    case "get":
                                    case "put":
                                    case "delete":
                                        return request.getServletPath().contains("/api/v1/user/");
                                    default:
                                        return false;
                                }
                            }).hasAnyAuthority("ADMIN")
                            .requestMatchers(request -> {
                                switch (request.getMethod().toLowerCase()) {
                                    case "post":
                                    case "get":
                                    case "put":
                                    case "delete":
                                        return request.getServletPath().contains("/api/v1/user/employee");
                                    default:
                                        return false;
                                }

                            }).hasAnyAuthority("MANAGER")
                            .requestMatchers(GET,"/api/v1/game/**").permitAll()
                            .requestMatchers(GET,"/api/v1/category/**").permitAll()
                            .requestMatchers(WHITE_LIST_URLS).permitAll()
                            .anyRequest().authenticated();

                })
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer

                            .authenticationEntryPoint(new RestAuthenticationEntryPoint());
                })
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .formLogin(formLoginConfigurer -> {
                    formLoginConfigurer.loginPage("/sign-in");
                })
                .authenticationProvider(authenticationProvider)
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
                );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(Customizer.withDefaults())
//                .httpBasic(httpSecurityHttpBasicConfigurer -> {
//                    httpSecurityHttpBasicConfigurer.disable();
//                })
//
//                .securityMatcher("/api/**")
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//                    authorizationManagerRequestMatcherRegistry
//                            .requestMatchers(WHITE_LIST_URL).permitAll()
//                            .anyRequest().authenticated();
//                })
//                .sessionManagement(sessionManagement -> {
//                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                })
//                .authenticationProvider(authenticationProvider)
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
//                                .authorizationEndpoint(authorizationEndpoint ->
//                                        authorizationEndpoint
//                                                .baseUri("/oauth2/authorize")
//                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
//                                )
//                                .redirectionEndpoint(redirectionEndpoint ->
//                                        redirectionEndpoint
//                                                .baseUri("/oauth2/callback/*")
//
//                                )
//
//                                .userInfoEndpoint(userInfoEndpoint ->
//                                        userInfoEndpoint
//                                                .userService(customOAuth2UserService)
//                                )
//                                .successHandler(oAuth2AuthenticationSuccessHandler)
//                                .failureHandler(oAuth2AuthenticationFailureHandler)
//                );
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }


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
