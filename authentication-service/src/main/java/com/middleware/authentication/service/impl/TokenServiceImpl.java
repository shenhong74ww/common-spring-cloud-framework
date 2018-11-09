package com.middleware.authentication.service.impl;

import com.middleware.authentication.exception.authentication.IncorrectPasswordException;
import com.middleware.authentication.exception.authentication.InvalidClientSecretException;
import com.middleware.authentication.exception.authentication.InvalidRefreshTokenException;
import com.middleware.authentication.exception.authentication.InvalidTokenException;
import com.middleware.authentication.exception.authentication.UserNotExistsException;
import com.middleware.authentication.feign.response.Member;
import com.middleware.authentication.feign.MemberClient;
import com.middleware.authentication.model.Client;
import com.middleware.authentication.service.ClientService;
import com.middleware.authentication.service.TokenService;
import com.middleware.common.constant.enums.RevokeReason;
import com.middleware.common.exception.InvalidClientIdException;
import com.middleware.common.model.TokenInfo;
import com.middleware.common.model.TokenResult;
import com.middleware.common.util.ApplicationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.middleware.common.constant.Constants.AUTH_TYPE_CLIENT;
import static com.middleware.common.constant.Constants.AUTH_TYPE_MEMBER;

@Service
public class TokenServiceImpl extends DefaultTokenServices implements TokenService {

    private Integer tokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    private TokenStore tokenStore;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TokenServiceImpl(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
        this.setTokenStore(tokenStore);
        this.setSupportRefreshToken(true);
        this.setReuseRefreshToken(false);
    }

    @Override
    public TokenResult generateTokenForClient(String clientId, String clientSecret) {
        Client client = clientService.getByClientId(clientId);
        if (client == null) {
            throw new InvalidClientIdException("Invalid client id");
        }
        Boolean allowDuplicate = client.getAllowDuplicate();
        if (client.getClientSecret().equals(clientSecret)) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setAuthType(AUTH_TYPE_CLIENT);
            tokenInfo.setUserSid(client.getSid());
            tokenInfo.setClientId(client.getClientId());
            return generateToken(tokenInfo, clientId, allowDuplicate);
        } else {
            throw new InvalidClientSecretException("Invalid client secret");
        }
    }

    @Override
    public TokenResult generateToken(String clientId, Integer regionCode, String phone, String password) {
        Client client = clientService.getByClientId(clientId);
        if (client == null) {
            throw new InvalidClientIdException("Invalid client id");
        }
        Boolean allowDuplicate = client.getAllowDuplicate();
        Member member = memberClient.getUser(phone, regionCode).getResult();

        if (member == null) {
            // Member not find
            throw new UserNotExistsException("Member not exists.");
        } else if (member != null) {

            if (!passwordEncoder.matches(password, member.getPassword())) {
                // Wrong password
                throw new IncorrectPasswordException("Current password is incorrected.");
            }

        }

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserSid(member.getSid());
        tokenInfo.setClientId(clientId);
        tokenInfo.setAuthType(AUTH_TYPE_MEMBER);

        TokenResult tokenResult = generateToken(tokenInfo, clientId, allowDuplicate);

        return tokenResult;
    }


    @Override
    public TokenResult refreshToken(String refreshToken, String clientId) {
        Client client = clientService.getByClientId(clientId);

        if (client == null) {
            throw new InvalidClientIdException("Invalid client id");
        }
        TokenRequest tokenRequest = new TokenRequest(null, clientId, null, "");

        OAuth2AccessToken accessToken = null;
        try {
            accessToken = this.refreshAccessToken(refreshToken, tokenRequest);
        } catch (RuntimeException e) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
        TokenResult result = convertToTokenResult(accessToken);

        return result;
    }

    @Override
    public void revokeToken(OAuth2Authentication oAuth2Authentication, RevokeReason revokeReason) {
        OAuth2AccessToken oAuth2AccessToken = tokenStore.getAccessToken(oAuth2Authentication);
        this.revokeToken(oAuth2AccessToken.getValue());
    }

    @Override
    public void revokeAllTokens(TokenInfo tokenInfo, RevokeReason revokeReason) {
        Collection<OAuth2AccessToken> oAuth2AccessTokens = tokenStore.findTokensByClientIdAndUserName(tokenInfo.getClientId(), tokenInfo.toString());

        for (OAuth2AccessToken oAuth2AccessToken : oAuth2AccessTokens) {
            if (oAuth2AccessToken != null) {
                if (revokeReason != null) {
                    revokeExistingToken(revokeReason, tokenInfo, oAuth2AccessToken);
                } else {
                    tokenStore.removeAccessToken(oAuth2AccessToken);
                }
            }
        }
    }

    private OAuth2Authentication getOAuth2Authentication(TokenInfo tokenInfo, String clientId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        HashMap<String, String> requestParameters = new HashMap<>();
        OAuth2Request storedRequest = new OAuth2Request(requestParameters, clientId, authorities, true, null, null,
                null, null, null);

        UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(tokenInfo,
                null, authorities);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedRequest, userAuthentication);
        oAuth2Authentication.setAuthenticated(true);
        return oAuth2Authentication;
    }

    @Override
    public TokenInfo getCurrentToken(Authentication authentication) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;

        if (authentication == null || oAuth2Authentication.getOAuth2Request() == null) {
            throw new InvalidTokenException("Invalid token");
        }

        OAuth2AccessToken oAuth2AccessToken = tokenStore.getAccessToken(oAuth2Authentication);
        TokenInfo currentTokenInfo = (TokenInfo) authentication.getPrincipal();

        if (currentTokenInfo.getRevokeReason() != null && oAuth2AccessToken != null) {
            this.revokeToken(oAuth2AccessToken.getValue());
        }

        return currentTokenInfo;
    }

    @Override
    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        if (this.tokenValiditySeconds == null) {
            this.tokenValiditySeconds = ApplicationUtil.getProperty("security.oauth2.authentication.tokenValiditySeconds", Integer.class);
        }
        return this.tokenValiditySeconds;
    }

    @Override
    protected int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
        if (this.refreshTokenValiditySeconds == null) {
            this.refreshTokenValiditySeconds = ApplicationUtil.getProperty("security.oauth2.authentication.refreshTokenValiditySeconds", Integer.class);
        }
        return this.refreshTokenValiditySeconds;
    }

    private TokenResult generateToken(TokenInfo tokenInfo, String clientId, Boolean allowDuplicate) {
        OAuth2Authentication auth2Authentication = getOAuth2Authentication(tokenInfo, clientId);
        OAuth2AccessToken accessToken = createAccessToken(tokenInfo, auth2Authentication, allowDuplicate);

        TokenResult result = convertToTokenResult(accessToken);

        return result;
    }

    private TokenResult convertToTokenResult(OAuth2AccessToken accessToken) {
        TokenResult result = new TokenResult();
        result.setAccessToken(accessToken.getValue());
        result.setExpiresIn(accessToken.getExpiresIn());
        result.setRefreshToken(accessToken.getRefreshToken().getValue());
        result.setTokenType(accessToken.getTokenType());

        return result;
    }

    private OAuth2AccessToken createAccessToken(TokenInfo tokenInfo, OAuth2Authentication authentication, Boolean allowDuplicate) throws AuthenticationException {
        OAuth2RefreshToken refreshToken = null;
        if (allowDuplicate == null || !allowDuplicate) {
            OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
            if (existingAccessToken != null) {
                revokeExistingToken(RevokeReason.REMOTE_LOGIN, tokenInfo, existingAccessToken);

                if (existingAccessToken.getRefreshToken() != null) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    tokenStore.removeRefreshToken(refreshToken);
                }

                tokenInfo.setRevokeReason(null);
            }
        }
        refreshToken = createRefreshToken(authentication);
        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        return accessToken;
    }

    private void revokeExistingToken(RevokeReason revokeReason, TokenInfo tokenInfo, OAuth2AccessToken oAuth2AccessToken) {
        tokenStore.removeAccessToken(oAuth2AccessToken);
        tokenStore.removeRefreshToken(oAuth2AccessToken.getRefreshToken());
        tokenInfo.setRevokeReason(revokeReason);
        OAuth2Authentication oAuth2Authentication = getOAuth2Authentication(tokenInfo, tokenInfo.getClientId());
        tokenStore.storeAccessToken(oAuth2AccessToken, oAuth2Authentication);
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String value = UUID.randomUUID().toString();
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(value);
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());
        return token;
    }
}
