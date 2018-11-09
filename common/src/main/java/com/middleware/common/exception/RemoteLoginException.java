package com.middleware.common.exception;

import com.middleware.common.constant.ErrorCodes;

public class RemoteLoginException extends CommonOAuth2Exception {

    public RemoteLoginException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.REMOTE_LOGIN;
    }


}
