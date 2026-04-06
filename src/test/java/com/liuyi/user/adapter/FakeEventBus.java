package com.liuyi.user.adapter;

import com.liuyi.user.adapter.publisher.EventBus;
import org.liuyi.use_api.event.UserRegisteredEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.liuyi.common.domain.event.Event;
import org.springframework.stereotype.Component;
import scala.Option;
import scala.collection.mutable.HashMap;

@Component
@Profile("test")
@Primary
public class FakeEventBus implements EventBus {
    private final HashMap<String, Event> events = new HashMap<>();
    @Override
    public void publish(UserRegisteredEvent event) {
        events.put(event.getTopic(), event);
    }

    public Option<Event> getEvent(String topic)
    {
        return events.get(topic);
    }

    public void reset()
    {
        events.clear();
    }
}
