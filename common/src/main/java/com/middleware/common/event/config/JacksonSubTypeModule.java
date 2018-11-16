package com.middleware.common.event.config;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonSubTypeModule extends SimpleModule {
    private Class<?>[] subtypes;

    public JacksonSubTypeModule(Class<?>... subtypes) {
        this.subtypes = subtypes;
    }

    @Override
    public void setupModule(SetupContext context) {
        context.registerSubtypes(subtypes);
        super.setupModule(context);
    }
}
