package com.example.fantasygame.api.module.hero.infrastructure;

import com.example.fantasygame.api.module.hero.model.HeroProfile;
import com.example.fantasygame.api.module.hero.model.valueobject.HeroProfileId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestInMemoryHeroProfileRepository extends InMemoryHeroProfileRepository {

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

    public void clear(){
        heroes.clear();
    }

    public int count(){
        return heroes.size();
    }

}
