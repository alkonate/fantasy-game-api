package com.example.fantasygame.api.module.hero.infrastructure;

import com.example.fantasygame.api.module.hero.model.HeroRepository;
import com.example.fantasygame.api.module.hero.model.Hero;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryHeroRepository implements HeroRepository {
    protected final Map<UUID, Hero> heroes = new HashMap<>();

    @Override
    public Hero save(Hero hero) {
        heroes.put(hero.getId(),hero);
        return  hero;
    }

    @Override
    public Optional<Hero> findByName(String name) {
        if(name == null){
            return Optional.empty();
        }

        return heroes.values()
                .stream()
                .filter(hero -> hero.getName().equals(name))
                .findFirst();
    }
}
