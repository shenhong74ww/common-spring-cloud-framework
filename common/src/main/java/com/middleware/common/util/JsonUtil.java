package com.middleware.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtil {
    public static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object source) {
        String result = "";

        if (source != null) {
            try {
                return objectMapper.writeValueAsString(source);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        T result;

        try {
            result = objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static <T> T fromJson(String json, Class<T> clazz, Class<?>... genericType) {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(clazz, genericType);
        T result;

        try {
            result = objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        T result;

        try {
            result = objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
