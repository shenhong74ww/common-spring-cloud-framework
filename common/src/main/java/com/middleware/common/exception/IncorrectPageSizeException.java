package com.middleware.common.exception;


import com.middleware.common.constant.ErrorCodes;

public class IncorrectPageSizeException extends CustomizedException {

    public IncorrectPageSizeException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INCORRECT_PAGE_SIZE;
    }
}
