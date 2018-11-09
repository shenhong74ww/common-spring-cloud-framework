package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class PasswordMissingException extends CustomizedException {

    public PasswordMissingException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.MISS_PASSWORD;
    }

}
