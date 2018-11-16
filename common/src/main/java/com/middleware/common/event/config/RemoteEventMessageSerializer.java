package com.middleware.common.event.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RemoteEventMessageSerializer {

    private ObjectMapper objectMapper;

    public RemoteEventMessageSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JacksonSubTypeModule(findSubTypes()));
    }

    public RemoteApplicationEvent deSerialize(byte[] message) {
        try {
            return objectMapper.readValue(message, RemoteApplicationEvent.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] serialize(RemoteApplicationEvent remoteApplicationEvent) {
        if (remoteApplicationEvent != null) {
            try {
                return objectMapper.writeValueAsBytes(remoteApplicationEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new byte[0];
    }

    private Class<?>[] findSubTypes() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                false);
        provider.addIncludeFilter(
                new AssignableTypeFilter(RemoteApplicationEvent.class));
        List<Class<?>> types = new ArrayList<>();
        Set<BeanDefinition> components = provider.findCandidateComponents("com.singbridge.middleware.common.event");
        for (BeanDefinition component : components) {
            try {
                types.add(Class.forName(component.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to scan classpath for remote event classes", e);
            }
        }
        return types.toArray(new Class<?>[0]);
    }

}
