package com.example.fantasygame.api.module.hero.infrastructure;

import com.example.fantasygame.api.module.hero.model.HeroProfile;
import com.example.fantasygame.api.module.hero.model.HeroRepository;
import com.example.fantasygame.api.module.hero.model.valueobject.HeroProfileId;
import com.example.fantasygame.api.module.hero.model.valueobject.Name;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryHeroProfileRepository implements HeroRepository {
    protected final Map<HeroProfileId, HeroProfile> heroes = new HashMap<>();

    @Override
    public HeroProfile save(HeroProfile hero) {
        heroes.put(hero.id(), hero);
        return hero;
    }

    @Override
    public Optional<HeroProfile> findByName(Name name) {
      return heroes.values()
               .stream()
               .filter(hero -> hero.name().equals(name))
               .findFirst();
    }
}
