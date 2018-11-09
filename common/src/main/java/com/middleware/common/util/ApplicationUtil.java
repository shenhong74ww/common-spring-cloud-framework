package com.middleware.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtil implements ApplicationContextAware, EnvironmentAware {

    static ApplicationContext applicationContext;
    static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        ApplicationUtil.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationUtil.applicationContext;
    }

    public static <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static String getProperty(String propertyName) {
        return environment.getProperty(propertyName);
    }

    public static <T> T getProperty(String propertyName, Class<T> targetType) {
        return environment.getProperty(propertyName, targetType);
    }
}