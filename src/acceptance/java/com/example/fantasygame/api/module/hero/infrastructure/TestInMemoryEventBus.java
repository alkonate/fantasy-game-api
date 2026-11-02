package com.example.fantasygame.api.module.hero.infrastructure;

import com.example.fantasygame.api.shared.InMemoryEventBus;
import com.example.fantasygame.api.shared.contracts.IntegrationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestInMemoryEventBus extends InMemoryEventBus {
    private List<IntegrationEvent> publishedEvents = new ArrayList<>();

    @Override
    public <T extends IntegrationEvent> void publish(T event) {
        publishedEvents.add(event);
        super.publish(event);
    }

    public Optional<IntegrationEvent> getLastEvent(){
        return Optional.ofNullable(publishedEvents.getLast());
    }

    public TestInMemoryEventBus assertEventCount(int expectedCount) {
        assertEquals(expectedCount, publishedEvents.size(), "Incorrect number of events");
        return this;
    }

    public TestInMemoryEventBus assertIsEmpty() {
        assertTrue(publishedEvents.isEmpty(), "Should be empty");
        return this;
    }

    public <T extends IntegrationEvent> T assertLastEventOfType(Class<T> expectedEventType) {
        assertInstanceOf(expectedEventType, publishedEvents.getLast().getClass(),"Incorrect type of event");
        return expectedEventType.cast(publishedEvents.getLast());
    }
}
