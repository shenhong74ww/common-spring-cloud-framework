package com.middleware.authentication.exception.authentication;

import com.middleware.authentication.constant.ErrorCodes;
import com.middleware.common.exception.CustomizedException;

public class PhoneInvalidExcaption extends CustomizedException {

    public PhoneInvalidExcaption(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PHONE_INVALID;
    }
}
