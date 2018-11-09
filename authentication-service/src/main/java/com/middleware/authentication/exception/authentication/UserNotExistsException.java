package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class UserNotExistsException extends CustomizedException {

    public UserNotExistsException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.USER_NOT_EXISTS;
    }
}
