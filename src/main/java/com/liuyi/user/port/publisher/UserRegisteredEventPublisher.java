package com.liuyi.user.port.publisher;

import com.liuyi.user.application.event.UserRegisteredEvent;

public interface UserRegisteredEventPublisher {
    void publish(UserRegisteredEvent event);
}
