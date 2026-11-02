package com.example.fantasygame.api.module.hero.infrastructure;

import com.example.fantasygame.api.module.hero.model.HeroProfile;
import com.example.fantasygame.api.module.hero.model.valueobject.HeroProfileId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestInMemoryHeroProfileRepository extends InMemoryHeroProfileRepository {
    public TestInMemoryHeroProfileRepository assertIsEmpty() {
        assertTrue(heroes.isEmpty(), "Heroes should be empty");
        return this;
    }

    public TestInMemoryHeroProfileRepository assertCount(int count) {
        assertEquals(count, heroes.size(), "heroes size should be equal to " + count);
        return this;
    }

    public Optional<HeroProfile> findById(HeroProfileId id) {
        if (heroes.containsKey(id)) {
            return Optional.of(heroes.get(id));
        }

        return Optional.empty();
    }
}
