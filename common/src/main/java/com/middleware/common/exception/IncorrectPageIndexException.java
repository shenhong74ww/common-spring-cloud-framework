package com.middleware.common.exception;

import com.middleware.common.constant.ErrorCodes;

public class IncorrectPageIndexException extends CustomizedException {

    public IncorrectPageIndexException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.INCORRECT_PAGE_INDEX;
    }
}
