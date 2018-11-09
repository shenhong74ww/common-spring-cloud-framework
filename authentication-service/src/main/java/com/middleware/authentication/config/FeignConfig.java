package com.middleware.authentication.config;

import com.middleware.authentication.service.TokenService;
import com.middleware.common.model.TokenResult;
import com.middleware.common.util.HttpRequestUtil;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

@Configuration
public class FeignConfig {

    @Autowired
    TokenService tokenService;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String tokenValue = null;
            if (authentication instanceof AnonymousAuthenticationToken) {
                TokenResult tokenResult = tokenService.generateTokenForClient(clientId, clientSecret);
                tokenValue = tokenResult.getAccessToken();
            } else {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
                tokenValue = details.getTokenValue();
            }
            requestTemplate.header("Authorization", "bearer " + tokenValue);
            requestTemplate.header("request-id", HttpRequestUtil.getRequestId());
        };
    }
}
