package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class WrongEmailFormatException extends CustomizedException {

    public WrongEmailFormatException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.WRONG_EMAIL_FORMAT;
    }

}
