package com.greamz.backend.security.oauth2;

import com.greamz.backend.config.JwtService;
import com.greamz.backend.enumeration.TokenType;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Token;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.repository.ITokenRepo;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.security.auth.AuthenticationResponse;
import com.greamz.backend.security.auth.AuthenticationService;
import com.greamz.backend.service.AccountService;
import com.greamz.backend.util.CookieUtils;
import com.greamz.backend.util.EncryptionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Optional;

import static com.greamz.backend.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtService tokenProvider;


    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    OAuth2AuthenticationSuccessHandler(JwtService tokenProvider,

                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri.orElse(getDefaultTargetUrl()))
                .build().toUriString();
        AuthenticationResponse authenticationResponse = generateToken(request, response, (UserPrincipal) authentication.getPrincipal());
        CookieUtils.addCookie(response, "access_token", authenticationResponse.getAccessToken(), Duration.ofMinutes(tokenProvider.getJwtExpiration()));
        CookieUtils.addCookie(response, "refresh_token", authenticationResponse.getRefreshToken(),Duration.ofMinutes(tokenProvider.getRefreshExpiration()));
        logger.info("targetUrl: " + targetUrl);
        logger.info("From :" + request.getRequestURL());
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    protected AuthenticationResponse generateToken(HttpServletRequest request, HttpServletResponse response, UserPrincipal authentication) {

        String token = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
//    private boolean isAuthorizedRedirectUri(String uri) {
//        URI clientRedirectUri = URI.create(uri);
//
//        return appProperties.getOauth2().getAuthorizedRedirectUris()
//                .stream()
//                .anyMatch(authorizedRedirectUri -> {
//                    // Only validate host and port. Let the clients use different paths if they want to
//                    URI authorizedURI = URI.create(authorizedRedirectUri);
//                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
//                        return true;
//                    }
//                    return false;
//                });
//    }

}