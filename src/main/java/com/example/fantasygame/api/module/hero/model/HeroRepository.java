package com.example.fantasygame.api.module.hero.model;

import com.example.fantasygame.api.module.hero.model.valueobject.Name;

import java.util.Optional;

public interface HeroRepository {
    HeroProfile save(HeroProfile hero);
    Optional<HeroProfile> findByName(Name name);
}
