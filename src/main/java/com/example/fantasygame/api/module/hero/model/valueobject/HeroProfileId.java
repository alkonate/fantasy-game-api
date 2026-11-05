package com.example.fantasygame.api.module.hero.model.valueobject;

import java.util.Objects;
import java.util.UUID;

public record HeroProfileId(UUID value) {
    public HeroProfileId {
        Objects.requireNonNull(value, "HeroProfileId value is required");
    }

    @Override
    public String toString() {
        return "HeroProfileId{" + "value=" + value + '}';
    }

    public static HeroProfileId generate() { return new HeroProfileId(UUID.randomUUID()); }
}
