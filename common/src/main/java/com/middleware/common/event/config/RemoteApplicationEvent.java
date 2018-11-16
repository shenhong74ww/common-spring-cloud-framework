package com.middleware.common.event.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.middleware.common.util.ApplicationUtil;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class RemoteApplicationEvent extends ApplicationEvent {

    public RemoteApplicationEvent() {
        this("unknown");
    }

    public RemoteApplicationEvent(Object source) {
        super(source);
        this.originService = ApplicationUtil.getProperty("spring.application.name");
        this.eventId = UUID.randomUUID().toString();
    }

    private String type;
    private String originService;
    private String destinationService;
    private String eventId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginService() {
        return originService;
    }

    public void setOriginService(String originService) {
        this.originService = originService;
    }

    public String getDestinationService() {
        return destinationService;
    }

    public void setDestinationService(String destinationService) {
        this.destinationService = destinationService;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
