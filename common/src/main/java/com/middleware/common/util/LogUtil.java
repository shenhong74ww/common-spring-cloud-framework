package com.middleware.common.util;

import org.slf4j.LoggerFactory;

public abstract class LogUtil {
    public static void info(Class<?> clazz, String log) {
        LoggerFactory.getLogger(clazz).info(log);
    }

    public static void info(Class<?> clazz, String log, Throwable e) {
        LoggerFactory.getLogger(clazz).info(log, e);
    }

    public static void warn(Class<?> clazz, String log) {
        LoggerFactory.getLogger(clazz).warn(log);
    }

    public static void warn(Class<?> clazz, String log, Throwable e) {
        LoggerFactory.getLogger(clazz).warn(log, e);
    }

    public static void error(Class<?> clazz, String log) {
        LoggerFactory.getLogger(clazz).error(log);
    }

    public static void error(Class<?> clazz, String log, Throwable e) {
        LoggerFactory.getLogger(clazz).error(log, e);
    }

    public static void debug(Class<?> clazz, String log) {
        LoggerFactory.getLogger(clazz).debug(log);
    }

    public static void debug(Class<?> clazz, String log, Throwable e) {
        LoggerFactory.getLogger(clazz).debug(log, e);
    }
}