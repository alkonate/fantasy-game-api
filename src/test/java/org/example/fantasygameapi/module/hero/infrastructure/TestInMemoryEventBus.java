package org.example.fantasygameapi.module.hero.infrastructure;

import com.example.fantasygame.api.shared.contracts.InMemoryEventBus;
import com.example.fantasygame.api.shared.contracts.IntegrationEvent;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestInMemoryEventBus extends InMemoryEventBus {

    private final List<IntegrationEvent> publishedEvents =  new ArrayList<>();

    @Override
    public void publish(IntegrationEvent event) {
        publishedEvents.add(event);
        super.publish(event);
    }

    public void clear(){
        publishedEvents.clear();
    }

    public List<IntegrationEvent> getPublishedEvents() {
        return publishedEvents;
    }

    public TestInMemoryEventBus assertIsEmpty() {
        assertTrue(publishedEvents.isEmpty(), "Published events should be empty");
        return this;
    }

    public TestInMemoryEventBus assertEventCount(int count) {
        assertEquals(count, publishedEvents.size(), "Event count not equal to expected count.");
        return this;
    }

    public IntegrationEvent getLastEvent() {
        assertFalse(publishedEvents.isEmpty(), "Aucun événement n'a été publié.");
        return publishedEvents.getLast();
    }


    public <T> T assertLastEventOfType(Class<T> type) {
        var event = getLastEvent();
        assertInstanceOf(type, event,
                "Event type expected : " + event.getEventType()
                        + ", received : " + event.getClass().getSimpleName());
        return type.cast(event);
    }

}
