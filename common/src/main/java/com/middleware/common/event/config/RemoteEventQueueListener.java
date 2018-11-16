package com.middleware.common.event.config;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.middleware.common.event.config.property.RemoteEventProperties;
import com.middleware.common.util.LogUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashMap;
import java.util.Properties;

public class RemoteEventQueueListener implements MessageListener, DisposableBean {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${spring.application.name}")
    private String applicationName;

    private RemoteEventMessageSerializer remoteEventMessageSerializer;
    private Consumer consumer;

    @Autowired
    public RemoteEventQueueListener(RemoteEventProperties remoteEventProperties,
                                    RemoteEventMessageSerializer remoteEventMessageSerializer) {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = new Properties();
        properties.setProperty("ConsumerId", remoteEventProperties.getMq().getConsumerId());
        properties.setProperty("AccessKey", remoteEventProperties.getMq().getAccessKey());
        properties.setProperty("SecretKey", remoteEventProperties.getMq().getSecretKey());
        properties.setProperty("ONSAddr", remoteEventProperties.getMq().getOnsAddr());
        properties.setProperty("ConsumeThreadNums", remoteEventProperties.getMq().getConsumeThreadNums().toString());
        consumerBean.setProperties(properties);
        HashMap<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        Subscription subscription = new Subscription();
        subscription.setTopic(remoteEventProperties.getMq().getTopic());
        subscription.setExpression("*");
        subscriptionTable.put(subscription, this);
        consumerBean.setSubscriptionTable(subscriptionTable);
        consumerBean.start();

        this.remoteEventMessageSerializer = remoteEventMessageSerializer;
    }

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try {
            RemoteApplicationEvent remoteApplicationEvent = remoteEventMessageSerializer.deSerialize(message.getBody());
            if (remoteApplicationEvent.getOriginService() != null
                    && !remoteApplicationEvent.getOriginService().equals(applicationName)) {
                applicationEventPublisher.publishEvent(remoteApplicationEvent);
            }
            return Action.CommitMessage;
        } catch (Exception ex) {
            LogUtil.error(RemoteEventQueueListener.class,
                    String.format("Process a remote event message failed, messageId: %s", message.getMsgID()),
                    ex);
            return Action.ReconsumeLater;
        }
    }

    @Override
    public void destroy() throws Exception {
        this.consumer.shutdown();
    }
}
