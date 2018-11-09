package com.middleware.common.exception;

public class CommonCustomizedException extends CustomizedException {

    private int errorCode;

    public CommonCustomizedException(String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }
}
