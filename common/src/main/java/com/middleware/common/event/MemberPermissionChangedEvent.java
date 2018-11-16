package com.middleware.common.event;



import org.springframework.cloud.bus.event.RemoteApplicationEvent;

import java.io.Serializable;

public class MemberPermissionChangedEvent extends RemoteApplicationEvent implements Serializable {

    private Long memberSid;
    private String oldType;
    private String newType;

    public MemberPermissionChangedEvent() {
    }

    public MemberPermissionChangedEvent(Object source, String originService, String destinationService, Long memberSid, String oldType, String newType) {
        super(source, originService, destinationService);
        this.memberSid = memberSid;
        this.oldType = oldType;
        this.newType = newType;
    }

    public Long getMemberSid() {
        return memberSid;
    }

    public void setMemberSid(Long memberSid) {
        this.memberSid = memberSid;
    }

    public String getOldType() {
        return oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }
}
