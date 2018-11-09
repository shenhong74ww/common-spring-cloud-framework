package com.middleware.common.exception;

import com.middleware.common.constant.ErrorCodes;

public class ServerNotRespondException extends CustomizedException {
    public ServerNotRespondException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVER_NOT_RESPONSE;
    }
}
