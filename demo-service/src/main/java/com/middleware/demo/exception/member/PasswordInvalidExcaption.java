package com.middleware.demo.exception.member;

import com.middleware.common.exception.CustomizedException;
import com.middleware.demo.constant.ErrorCodes;

public class PasswordInvalidExcaption extends CustomizedException {
    public PasswordInvalidExcaption(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PASSWORD_INVALID;
    }
}
