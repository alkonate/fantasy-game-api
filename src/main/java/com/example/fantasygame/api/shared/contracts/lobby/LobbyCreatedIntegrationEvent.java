package com.example.fantasygame.api.shared.contracts.lobby;

import com.example.fantasygame.api.shared.contracts.IntegrationEvent;

import java.time.Instant;
import java.util.UUID;

public record LobbyCreatedIntegrationEvent(UUID eventId,
                                           String eventType,
                                           Instant occurredOn,
                                           Payload payload) implements IntegrationEvent {

    public LobbyCreatedIntegrationEvent(Payload payload) {
        this(UUID.randomUUID(),
                "LobbyCreatedEvent",
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

    public record Payload(UUID lobbyId) { }
}
