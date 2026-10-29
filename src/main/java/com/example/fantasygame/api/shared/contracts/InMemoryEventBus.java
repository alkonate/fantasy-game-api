package com.example.fantasygame.api.shared.contracts;

public class InMemoryEventBus implements IntegrationEventBus {

    @Override
    public <T extends IntegrationEvent> void publish(T event) {

    }
}
