package com.greamz.backend.security.oauth2;

import com.greamz.backend.config.JwtService;
import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.enumeration.TokenType;
import com.greamz.backend.exception.OAuth2AuthenticationProcessingException;
import com.greamz.backend.model.AccountAuthProvider;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Token;
import com.greamz.backend.repository.IAccountAuthProvider;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.repository.ITokenRepo;
import com.greamz.backend.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private IAccountRepo userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ITokenRepo tokenRepository;
    @Autowired
    private IAccountAuthProvider accountAuthProviderRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<AccountModel> userOptional = userRepository.findByUserNameOrEmail(oAuth2UserInfo.getEmail());
        AccountModel user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            var accountAuthProvider = accountAuthProviderRepository.findAllByAccount_Id(user.getId());
            accountAuthProvider.forEach(authProvider -> {
                if (authProvider.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))){
                    throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                            accountAuthProvider + " account. Please use your " + accountAuthProvider +
                            " account to login.");
                }
            });

            user = updateExistingUser(user, oAuth2UserInfo,oAuth2UserRequest);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        UserPrincipal userPrincipal = UserPrincipal.create(user, oAuth2User.getAttributes());
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()){
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);

        }
        var token = Token.builder()
                .user(user)
                .token(refreshToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
        return userPrincipal;
    }

    private AccountModel registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        AccountModel user = new AccountModel();
        user.setFullname(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setPhoto(oAuth2UserInfo.getImageUrl());
        user.setRole(Role.USER);
        user.setLocale(oAuth2UserInfo.getAttributes().get("locale").toString());
        AccountAuthProvider accountAuthProvider =AccountAuthProvider.builder()
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .account(user)
                .build();
        user.setAuthProviders(List.of(accountAuthProvider));
        return userRepository.save(user);
    }
    private AccountModel updateExistingUser(AccountModel existingUser, OAuth2UserInfo oAuth2UserInfo,OAuth2UserRequest oAuth2UserRequest) {
        var accountAuthProvider = accountAuthProviderRepository.findAllByAccount_Id(existingUser.getId());
        accountAuthProvider.forEach(authProvider -> {
            if (authProvider.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))){
                authProvider.setProviderId(oAuth2UserInfo.getId());

            }
        });
        existingUser.setAuthProviders(accountAuthProvider);
        return userRepository.save(existingUser);
    }

}