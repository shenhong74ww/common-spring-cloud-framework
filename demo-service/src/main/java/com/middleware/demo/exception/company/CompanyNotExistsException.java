package com.middleware.demo.exception.company;

import com.middleware.common.exception.CustomizedException;
import com.middleware.demo.constant.ErrorCodes;

public class CompanyNotExistsException extends CustomizedException {

    public CompanyNotExistsException(String msg) {
        super(msg);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.COMPANY_NOT_EXISTS;
    }
}
