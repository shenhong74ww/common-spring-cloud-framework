package com.middleware.demo.exception.member;

import com.middleware.common.exception.CustomizedException;
import com.middleware.demo.constant.ErrorCodes;

public class UserExistsException extends CustomizedException {
    public UserExistsException(String msg) {
            super(msg);
        }

    @Override
    public int getErrorCode() {
            return ErrorCodes.MEMBER_EXISTS;
        }
}
