package com.middleware.common.exception;

import com.middleware.common.constant.ErrorCodes;

public class UnsupportedSortFieldException extends CustomizedException{

    public UnsupportedSortFieldException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.UNSUPPORTED_SORT_FIELD;
    }
}
