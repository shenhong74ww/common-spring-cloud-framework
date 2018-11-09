package com.middleware.common.exception;

public abstract class CustomizedException extends RuntimeException {

    public CustomizedException() {
        super();
    }

    public CustomizedException(String msg) {
        super(msg);
    }

    public abstract int getErrorCode();
}
