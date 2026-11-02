package com.example.fantasygame.api.module.hero.model.valueobject;

import java.util.Objects;
import java.util.UUID;

public record PlayerId(UUID value) {
    public PlayerId {
        Objects.requireNonNull(value, "PlayerId value is required");
    }

    public static PlayerId generate() { return new PlayerId(UUID.randomUUID()); }
}
