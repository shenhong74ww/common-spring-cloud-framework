package com.middleware.demo.exception.member;

import com.middleware.common.exception.CustomizedException;
import com.middleware.demo.constant.ErrorCodes;

public class PhoneInvalidExcaption extends CustomizedException {
    public PhoneInvalidExcaption(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PHONE_INVALID;
    }
}
