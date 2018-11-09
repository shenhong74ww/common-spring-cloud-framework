package com.middleware.authentication.service;

import com.middleware.common.constant.enums.RevokeReason;
import com.middleware.common.model.TokenInfo;
import com.middleware.common.model.TokenResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

public interface TokenService extends AuthorizationServerTokenServices, ResourceServerTokenServices {

    TokenResult generateTokenForClient(String clientId, String clientSecret);

    TokenResult generateToken(String clientId, Integer regionCode, String phone, String password);


    TokenResult refreshToken(String refreshToken, String clientId);

    void revokeToken(OAuth2Authentication oAuth2Authentication, RevokeReason revokeReason);

    void revokeAllTokens(TokenInfo tokenInfo, RevokeReason revokeReason);

    TokenInfo getCurrentToken(Authentication authentication);
}
