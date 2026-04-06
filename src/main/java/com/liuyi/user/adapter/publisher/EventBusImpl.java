package com.liuyi.user.adapter.publisher;

import lombok.RequiredArgsConstructor;
import org.liuyi.use_api.event.UserRegisteredEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventBusImpl implements EventBus {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public void publish(UserRegisteredEvent event) {
        // 序列化并发送
        kafkaTemplate.send(UserRegisteredEvent.TOPIC, event);
    }
}
