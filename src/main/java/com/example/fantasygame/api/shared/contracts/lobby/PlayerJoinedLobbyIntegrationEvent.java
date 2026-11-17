package com.example.fantasygame.api.shared.contracts.lobby;

import com.example.fantasygame.api.shared.contracts.IntegrationEvent;

import java.time.Instant;
import java.util.UUID;

public record PlayerJoinedLobbyIntegrationEvent(UUID eventId,
                                                String eventType,
                                                Instant occurredOn,
                                                Payload payload) implements IntegrationEvent {

    public PlayerJoinedLobbyIntegrationEvent(Payload payload) {
        this(UUID.randomUUID(),
                "PlayerJoinedLobbyEvent",
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
