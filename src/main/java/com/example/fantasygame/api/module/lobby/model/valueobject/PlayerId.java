package com.example.fantasygame.api.module.lobby.model.valueobject;

import java.util.UUID;

public record PlayerId(UUID value) {
    public static PlayerId generate() {
        return new PlayerId(UUID.randomUUID());
    }
}
