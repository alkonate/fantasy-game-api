package com.example.fantasygame.api.module.hero.model;

import java.util.Optional;

public interface HeroRepository {
    HeroProfile save(HeroProfile hero);
    Optional<HeroProfile> findByName(String name);
}
