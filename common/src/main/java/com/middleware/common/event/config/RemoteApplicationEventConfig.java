package com.middleware.common.event.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

@Import({RemoteEventPublisher.class, RemoteEventQueueListener.class})
public class RemoteApplicationEventConfig {

    @Autowired
    private RemoteEventPublisher remoteEventPublisher;

    @Autowired
    @Value("${spring.application.name}")
    private String applicationName;

    @EventListener
    public void onRemoteApplicationEvent(RemoteApplicationEvent remoteApplicationEvent) {
        if (applicationName.equals(remoteApplicationEvent.getOriginService())) {
            this.remoteEventPublisher.push(remoteApplicationEvent);
        }
    }

}
