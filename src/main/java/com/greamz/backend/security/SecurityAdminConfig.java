package com.greamz.backend.security;


import com.greamz.backend.config.JwtAuthenticationFilter;
import com.greamz.backend.enumeration.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@EnableWebSecurity
@Configuration
public class SecurityAdminConfig {
    @Autowired
    private HttpSession session;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private LogoutHandler logoutHandler;
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfigurer -> {
                    csrfConfigurer.disable();
                })

                .authorizeHttpRequests(authorize ->{
                    authorize
                            .requestMatchers("/user/**").hasAuthority(Role.USER.name())
                            .requestMatchers("/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .formLogin(customizeLoginUser())
                .logout(customizeLogoutUser());

        return http.build();
    }
    public Customizer<FormLoginConfigurer<HttpSecurity>> customizeLoginUser(){
        return formLoginConfigurer -> formLoginConfigurer
                .loginPage("/login?unauthorized")
                .defaultSuccessUrl("/")
                .loginProcessingUrl("/authenticate")


                .failureForwardUrl("/login?error")

                .permitAll();
    }
    public Customizer<LogoutConfigurer<HttpSecurity>> customizeLogoutUser(){
        return new Customizer<LogoutConfigurer<HttpSecurity>>() {
            @Override
            public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
                httpSecurityLogoutConfigurer
                        .logoutUrl("/action_logout")
                        .deleteCookies("JSESSIOND")
                        .logoutSuccessUrl("/login?logout=true");

            }

        };
    }
    @Bean
    @Order(10)
    SecurityFilterChain securityFilterChainUser(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfigurer -> {
                        }
                )
                .cors(httpSecurityCorsConfigurer ->{
                    httpSecurityCorsConfigurer.disable();
                })
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.PUT, "/admin/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/admin/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority("ADMIN", "STAFF")
                            .requestMatchers("/user/**").hasAuthority("USER")
                            .requestMatchers("/**").permitAll()
                            .anyRequest().authenticated();

                })
                .sessionManagement(sessionMnagement -> {
                    sessionMnagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer
                            .logoutUrl("/admin/action_logout")
                            .deleteCookies("JSESSIOND")
                            .logoutSuccessUrl("/admin/login")
                            .addLogoutHandler(logoutHandler);
                });

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
                AUTHORIZATION
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
