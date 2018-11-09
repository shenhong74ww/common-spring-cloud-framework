package com.middleware.common.exception;

import com.middleware.common.constant.ErrorCodes;

public class InvalidClientIdException extends CustomizedException {

    public InvalidClientIdException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INVALID_CLIENT_ID;
    }
}
