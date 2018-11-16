package com.middleware.common.event.config;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.middleware.common.event.config.property.RemoteEventProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class RemoteEventPublisher implements DisposableBean {

    private Producer producer;
    private RemoteEventMessageSerializer remoteEventMessageSerializer;
    private RemoteEventProperties remoteEventProperties;

    @Autowired
    public RemoteEventPublisher(RemoteEventProperties remoteEventProperties,
                                RemoteEventMessageSerializer remoteEventMessageSerializer) {
        ProducerBean producer = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty("ProducerId", remoteEventProperties.getMq().getProducerId());
        properties.setProperty("AccessKey", remoteEventProperties.getMq().getAccessKey());
        properties.setProperty("SecretKey", remoteEventProperties.getMq().getSecretKey());
        properties.setProperty("ONSAddr", remoteEventProperties.getMq().getOnsAddr());
        producer.setProperties(properties);
        producer.start();
        this.producer = producer;

        this.remoteEventProperties = remoteEventProperties;
        this.remoteEventMessageSerializer = remoteEventMessageSerializer;
    }

    public void push(RemoteApplicationEvent remoteApplicationEvent) {
        Message message = new Message(remoteEventProperties.getMq().getTopic(),
                remoteEventProperties.getMq().getTag(),
                remoteEventMessageSerializer.serialize(remoteApplicationEvent));
        this.producer.send(message);
    }

    @Override
    public void destroy() throws Exception {
        this.producer.shutdown();
    }
}
