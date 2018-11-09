package com.middleware.authentication.model;

import com.middleware.authentication.constant.enums.ClientType;
import com.middleware.common.model.BaseModel;

import javax.persistence.Entity;

@Entity
public class Client extends BaseModel {

    private String clientId;
    private String clientSecret;
    private ClientType clientType;
    private String description;
    private Boolean allowDuplicate;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAllowDuplicate() {
        return allowDuplicate;
    }

    public void setAllowDuplicate(Boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }
}
