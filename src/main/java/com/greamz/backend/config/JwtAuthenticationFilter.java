package com.greamz.backend.config;



import com.greamz.backend.repository.ITokenRepo;
import com.greamz.backend.security.auth.AuthenticationResponse;
import com.greamz.backend.security.auth.AuthenticationService;
import com.greamz.backend.util.CookieUtils;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ITokenRepo tokenRepository;
    private final RestTemplate restTemplate;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getServletPath().contains("/api/v1/auth") || request.getServletPath().contains("/sign-in")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        String jwt;

        if(CookieUtils.getCookie(request,"accessToken")!=null){
                System.out.println("accessToken");
                jwt=CookieUtils.getCookie(request,"accessToken").getValue();
                isValid(jwt,request,response,filterChain);
                return;
        }

        if (authHeader == null ||!authHeader.startsWith("Bearer ") ) {
            filterChain.doFilter(request, response);
        }else{
            jwt = authHeader.substring(7);
            isValid(jwt,request,response,filterChain);
        }



    }

    private void isValid(String jwt,HttpServletRequest request,HttpServletResponse response,FilterChain filterChain){
        final String userEmail;

        try{
            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                var isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
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
                }else {
                    if(CookieUtils.getCookie(request,"refreshToken")!=null){
                        HttpHeaders headers = new HttpHeaders();

                        headers.add("Authorization", "Bearer "+CookieUtils.getCookie(request,"refreshToken"));

                        HttpEntity<String> entity = new HttpEntity<>( headers);
                        ResponseEntity<AuthenticationResponse> authenticationResponse= restTemplate
                                .postForEntity(
                                        "http://localhost:8080/api/v1/auth/refresh-token",entity
                                        , AuthenticationResponse.class);
                        if(authenticationResponse.getBody()!=null){
                            jwt=authenticationResponse.getBody().getAccessToken();
                            response.addCookie(new Cookie("accessToken",jwt));
                            response.addCookie(new Cookie("refreshToken",authenticationResponse.getBody().getRefreshToken()));
                            filterChain.doFilter(request, response);
                        }else {
                            response.sendError(401,"Unauthorized");
                        }
                    }
                }

            }


        }catch (ExpiredJwtException e){

        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}