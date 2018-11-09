package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class InvalidRefreshTokenException extends CustomizedException {

    public InvalidRefreshTokenException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INVALID_REFRESH_TOKEN;
    }
}
