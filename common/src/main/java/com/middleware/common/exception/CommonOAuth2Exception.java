package com.middleware.common.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public abstract class CommonOAuth2Exception extends OAuth2Exception {

    public CommonOAuth2Exception(String msg) {
        super(msg);
    }

    public abstract int getErrorCode();
}
