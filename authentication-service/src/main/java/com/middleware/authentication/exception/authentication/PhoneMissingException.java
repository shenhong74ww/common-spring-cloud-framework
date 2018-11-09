package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class PhoneMissingException extends CustomizedException {

    public PhoneMissingException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.MISS_PHONE;
    }
}
