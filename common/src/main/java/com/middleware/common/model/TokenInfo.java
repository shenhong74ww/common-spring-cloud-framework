package com.middleware.common.model;

import com.middleware.common.constant.enums.RevokeReason;

import java.io.Serializable;

public class TokenInfo implements Serializable {

    private String authType;
    private Long userSid;
    private String clientId;
    private RevokeReason revokeReason;

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public Long getUserSid() {
        return userSid;
    }

    public void setUserSid(Long userSid) {
        this.userSid = userSid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public RevokeReason getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(RevokeReason revokeReason) {
        this.revokeReason = revokeReason;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "authType='" + authType + '\'' +
                ", userSid=" + userSid +
                ", clientId='" + clientId + '\'' +
                ", revokeReason=" + revokeReason +
                '}';
    }
}
