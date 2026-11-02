package com.example.fantasygame.api.shared;

import com.example.fantasygame.api.shared.contracts.IntegrationEvent;
import com.example.fantasygame.api.shared.contracts.IntegrationEventBus;

public class InMemoryEventBus implements IntegrationEventBus {

    @Override
    public <T extends IntegrationEvent> void publish(T event) {

    }
}
