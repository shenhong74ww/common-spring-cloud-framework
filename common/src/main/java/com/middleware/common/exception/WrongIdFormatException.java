package com.middleware.common.exception;


import com.middleware.common.constant.ErrorCodes;

public class WrongIdFormatException extends CustomizedException {
    public WrongIdFormatException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.WRONG_ID_FORMAT;
    }

}
