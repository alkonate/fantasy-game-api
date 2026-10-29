package com.example.fantasygame.api.shared.contracts;

public interface IntegrationEventBus {
    public <T extends IntegrationEvent> void publish(T event);
}
