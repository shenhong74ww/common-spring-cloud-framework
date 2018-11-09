package com.middleware.common.exception;

import com.middleware.common.constant.ErrorCodes;

public class ResetPasswordReloginException extends CommonOAuth2Exception {

    public ResetPasswordReloginException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.RESET_PASSWORD_RELOGIN;
    }


}
