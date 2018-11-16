package com.middleware.common.event.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "remoteEvent")
public class RemoteEventProperties {

    private MqConfigProperties mq;

    public MqConfigProperties getMq() {
        return mq;
    }

    public void setMq(MqConfigProperties mq) {
        this.mq = mq;
    }
}
