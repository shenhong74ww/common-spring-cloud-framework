package com.middleware.authentication.exception.authentication;

import com.middleware.common.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class InvalidTokenException extends CustomizedException {

    public InvalidTokenException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INVALID_TOKEN;
    }
}
