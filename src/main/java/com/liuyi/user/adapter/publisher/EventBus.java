package com.liuyi.user.adapter.publisher;

import org.liuyi.use_api.event.UserRegisteredEvent;
import org.springframework.context.annotation.Bean;

public interface EventBus {
    public void publish(UserRegisteredEvent event);
}

