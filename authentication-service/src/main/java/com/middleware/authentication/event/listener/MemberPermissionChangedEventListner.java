package com.middleware.authentication.event.listener;

import com.middleware.common.event.MemberPermissionChangedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MemberPermissionChangedEventListner implements ApplicationListener<MemberPermissionChangedEvent> {

    @Override
    public void onApplicationEvent(MemberPermissionChangedEvent memberPermissionChangedEvent) {
        System.out.println(memberPermissionChangedEvent.getNewType());
    }
}
