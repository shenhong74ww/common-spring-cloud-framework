package com.middleware.common.event.common;

import com.middleware.common.event.config.RemoteApplicationEvent;
import com.middleware.common.util.JsonUtil;
import com.middleware.common.util.LogUtil;
import com.middleware.common.util.StringUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;

public abstract class AbstractEventListener<T extends RemoteApplicationEvent> implements ApplicationListener<T> {

    private StringRedisTemplate redisTemplate;
    private String lastProcessedEventIdKey = String.format("remoteEvent.listener.%s.lastProcessedEventId", this.getClass());

    public AbstractEventListener(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onApplicationEvent(T event) {
        LogUtil.info(this.getClass(), String.format("Receive a remote application event, %s", JsonUtil.toJson(event)));
        if (checkDuplicate(event)) {
            LogUtil.info(this.getClass(), String.format("Duplicate event received, eventId: %s", event.getEventId()));
            return;
        }
        processEvent(event);
        storeLastProcessedEventId(event);
    }

    public abstract void processEvent(T event);

    private boolean checkDuplicate(T event) {
        if (redisTemplate.hasKey(lastProcessedEventIdKey)) {
            String lastProcessedEventId = redisTemplate.opsForValue().get(lastProcessedEventIdKey);
            if (!StringUtil.isEmpty(lastProcessedEventId) && lastProcessedEventId.equals(event.getEventId())) {
                return true;
            }
        }
        return false;
    }

    private void storeLastProcessedEventId(T event) {
        redisTemplate.opsForValue().set(lastProcessedEventIdKey, event.getEventId());
    }
}
