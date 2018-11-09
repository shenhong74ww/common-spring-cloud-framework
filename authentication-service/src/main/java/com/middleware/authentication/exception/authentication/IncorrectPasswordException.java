package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class IncorrectPasswordException extends CustomizedException {

    public IncorrectPasswordException(String msg) {
        super(msg);
    }
    
    @Override
    public int getErrorCode() {
        return ErrorCodes.PASSWORD_INCORRECT;
    }
}
