package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class InvalidClientSecretException extends CustomizedException {

    public InvalidClientSecretException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INVALID_CLIENT_SECRET;
    }
}
