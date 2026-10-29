package com.example.fantasygame.api.module.hero.model;

import java.util.Optional;

public interface HeroRepository {
    Hero save(Hero hero);
    Optional<Hero> findByName(String name);
}
