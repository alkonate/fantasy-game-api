package com.example.fantasygame.api.module.lobby.model.valueobject;

import java.util.Objects;
import java.util.UUID;

public record LobbyId(UUID value) {
    public LobbyId {
        Objects.requireNonNull(value, "LobbyId value is required");
    }

    public static LobbyId generate() { return new LobbyId(UUID.randomUUID()); }
}
