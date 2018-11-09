package com.middleware.common.model;

import com.middleware.common.constant.enums.OperationType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class EntityChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    private String entityPackageName;
    private String entityClassName;
    private Long entitySid;
    private String entityNewContent;
    private Date operationTime;
    private OperationType operationType;
    private Long operatorSid;
    private String authType;
    private String clientId;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public Long getEntitySid() {
        return entitySid;
    }

    public void setEntitySid(Long entitySid) {
        this.entitySid = entitySid;
    }

    public String getEntityNewContent() {
        return entityNewContent;
    }

    public void setEntityNewContent(String entityNewContent) {
        this.entityNewContent = entityNewContent;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Long getOperatorSid() {
        return operatorSid;
    }

    public void setOperatorSid(Long operatorSid) {
        this.operatorSid = operatorSid;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
