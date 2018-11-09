package com.middleware.common.exception;

import com.middleware.common.constant.ErrorCodes;

public class SpecificationException extends CustomizedException {

    public SpecificationException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INVALID_SPECIFICATION_EXCEPTION;
    }
}
