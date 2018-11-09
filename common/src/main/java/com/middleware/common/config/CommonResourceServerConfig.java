package com.middleware.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

@Configuration
public class CommonResourceServerConfig extends ResourceServerConfigurerAdapter {

    OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        oAuth2AuthenticationEntryPoint.setExceptionRenderer(new CustomizedOAuth2ExceptionRenderer());
        resources.authenticationEntryPoint(oAuth2AuthenticationEntryPoint);
    }
}