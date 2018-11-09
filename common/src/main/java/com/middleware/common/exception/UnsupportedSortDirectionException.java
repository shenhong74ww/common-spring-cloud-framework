package com.middleware.common.exception;


import com.middleware.common.constant.ErrorCodes;

public class UnsupportedSortDirectionException extends CustomizedException {

    public UnsupportedSortDirectionException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.UNSUPPORTED_SORT_DIRECTION;
    }
}
