package com.middleware.common.event.config;

import com.middleware.common.event.config.property.RemoteEventProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RemoteApplicationEventConfig.class, RemoteEventPublisher.class,
        RemoteEventQueueListener.class, RemoteEventMessageSerializer.class,
        RemoteEventProperties.class})
public @interface EnableRemoteApplicationEvent {
}
