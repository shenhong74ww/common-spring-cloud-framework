package com.middleware.gateway.model;

public class ResponseInfo {
    
    private int status;
    private String responseStr;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }
}
