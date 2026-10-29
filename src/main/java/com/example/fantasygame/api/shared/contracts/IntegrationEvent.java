package com.example.fantasygame.api.shared.contracts;

import java.time.Instant;
import java.util.UUID;

public interface IntegrationEvent {
    UUID getEventId();
    Instant getOccurredOn();
    String getEventType();
}
