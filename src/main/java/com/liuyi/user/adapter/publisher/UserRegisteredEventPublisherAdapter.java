package com.liuyi.user.adapter.publisher;

import com.liuyi.user.port.publisher.UserRegisteredEventPublisher;
import lombok.RequiredArgsConstructor;
import org.liuyi.use_api.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventPublisherAdapter implements UserRegisteredEventPublisher {
    private final EventBus eventBus;

    @Override
    public void publish(com.liuyi.user.application.event.UserRegisteredEvent event) {
        UserRegisteredEvent infraEvent = UserRegisteredEvent.builder().userId(event.getUserId()).phone(event.getPhone()).hashedPassword(event.getHashedPassword()).build();
        eventBus.publish(infraEvent);
    }
}
