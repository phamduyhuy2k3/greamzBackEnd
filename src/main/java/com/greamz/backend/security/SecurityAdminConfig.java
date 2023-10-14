package com.greamz.backend.security;


import com.greamz.backend.config.JwtAuthenticationFilter;
import com.greamz.backend.enumeration.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.List;

import static com.greamz.backend.enumeration.Role.ADMIN;
import static com.greamz.backend.enumeration.Role.MANAGER;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityAdminConfig {


    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;
    public static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
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
            "/dashboard/**",
            "/sign-in",
            "/assets/**",
            "/component/**",
            "/pages/**",
            "/static/**"
    };
//    @Bean
//    @Order(11)
//    SecurityFilterChain securityFilterChainAdmin(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrfConfigurer -> {
//                        }
//                )
//                .cors(httpSecurityCorsConfigurer ->{
//                    httpSecurityCorsConfigurer.disable();
//                })
//                .securityMatcher("/dashboard/**")
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//                    authorizationManagerRequestMatcherRegistry
//                            .requestMatchers(WHITE_LIST_URL).permitAll()
//                            .anyRequest().authenticated();
//
//                })
//
//                .formLogin(httpSecurityFormLoginConfigurer -> {
//                    httpSecurityFormLoginConfigurer.loginPage("/sign-in");
//
//                })
//                .logout(logout ->
//                        logout.logoutUrl("/api/v1/auth/logout")
//                                .addLogoutHandler(logoutHandler)
//                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                )
//
//        ;
//
//        return http.build();
//    }
    @Bean

    SecurityFilterChain securityFilterChainUser(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfigurer -> {
                    csrfConfigurer.disable();
                })

                .cors(httpSecurityCorsConfigurer ->{
                    httpSecurityCorsConfigurer.disable();
                })
                .securityMatcher("/dashboard/**")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(WHITE_LIST_URL).permitAll()

                            .anyRequest().authenticated();

                })
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )

        ;

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
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
