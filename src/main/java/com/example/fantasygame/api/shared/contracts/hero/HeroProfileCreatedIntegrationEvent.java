package com.example.fantasygame.api.shared.contracts.hero;

import com.example.fantasygame.api.shared.contracts.IntegrationEvent;

import java.time.Instant;
import java.util.UUID;

public record HeroProfileCreatedIntegrationEvent(UUID eventId,
                                                 String eventType,
                                                 Instant occurredOn,
                                                 Payload payload) implements IntegrationEvent {

    public HeroProfileCreatedIntegrationEvent(Payload payload) {
        this(UUID.randomUUID(),
                "HeroProfileCreatedEvent",
                Instant.now(),
                payload);
    }
    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    public record Payload(UUID heroProfileId) { }
}
