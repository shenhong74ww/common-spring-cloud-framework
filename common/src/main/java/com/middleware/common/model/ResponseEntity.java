package com.middleware.common.model;

public class ResponseEntity<T> {

    private Integer errorCode;
    private String errorMsg;
    private T result;

    public ResponseEntity() {
        this.setErrorCode(0);
    }

    public ResponseEntity(T result) {
        this.setErrorCode(0);
        this.setResult(result);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
