package com.middleware.common.exception.handler;

import com.middleware.common.constant.ErrorCodes;
import com.middleware.common.exception.CommonCustomizedException;
import com.middleware.common.exception.CustomizedException;
import com.middleware.common.model.ResponseEntity;
import com.middleware.common.util.LogUtil;
import feign.codec.DecodeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomizedException.class)
    public ResponseEntity handleCustomException(HttpServletRequest request, CustomizedException ex) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorCode(ex.getErrorCode());
        responseEntity.setErrorMsg(ex.getMessage());
        return responseEntity;
    }

    @ExceptionHandler(DecodeException.class)
    public ResponseEntity handleCustomException(HttpServletRequest request, DecodeException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof CommonCustomizedException) {
            CommonCustomizedException customizedException = (CommonCustomizedException) cause;
            return handleCustomException(request, customizedException);
        } else {
            return handleUncaughtException(request, ex);
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingRequiredParameterException(HttpServletRequest request, MissingServletRequestParameterException ex) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorCode(ErrorCodes.MISSING_REQUIRED_PARAMETER);
        responseEntity.setErrorMsg("Missing required parameter");
        return responseEntity;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorCode(ErrorCodes.HTTP_REQUEST_METHOD_NOT_SUPPORTED);
        responseEntity.setErrorMsg("Http method not supported");
        return responseEntity;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorCode(ErrorCodes.INVALID_PARAM_TYPE);
        responseEntity.setErrorMsg("Invalid parameter type");
        return responseEntity;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorCode(ErrorCodes.INVALID_REQUEST_BODY);
        responseEntity.setErrorMsg("Invalid request body");
        return responseEntity;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorCode(ErrorCodes.PERMISSION_DENIED);
        responseEntity.setErrorMsg("Permission denied");
        return responseEntity;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleUncaughtException(HttpServletRequest request, Exception ex) {
        LogUtil.error(this.getClass(), ex.getMessage(), ex);
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setErrorCode(-1);
        responseEntity.setErrorMsg("Uncaught exception");
        return responseEntity;
    }

}
