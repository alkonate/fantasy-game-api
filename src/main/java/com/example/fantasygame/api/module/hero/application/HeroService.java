package com.example.fantasygame.api.module.hero.application;

import com.example.fantasygame.api.module.hero.model.Hero;
import com.example.fantasygame.api.module.hero.model.HeroNameAlreadyExistsException;
import com.example.fantasygame.api.module.hero.model.HeroRepository;
import com.example.fantasygame.api.shared.contracts.IntegrationEventBus;
import com.example.fantasygame.api.shared.contracts.hero.HeroCreatedEvent;

import java.util.UUID;


public class HeroService implements CreateHeroUseCase {
    private final HeroRepository repository;
    private final IntegrationEventBus bus;

    public HeroService(HeroRepository heroRepo, IntegrationEventBus publisher) {
        this.repository = heroRepo;
        this.bus = publisher;
    }

    @Override
    public UUID create(CreateHeroCommand cmd) {
       var existingHero  = repository.findByName(cmd.name());
       if(existingHero.isPresent()){
           throw new HeroNameAlreadyExistsException();
       }
        Hero heroSaved = repository.save(
                new Hero(
                        UUID.randomUUID(),
                        UUID.fromString(cmd.playerId()),
                        cmd.name(),
                        cmd.species(),
                        cmd.heroClass()
                )
        );

        bus.publish(new HeroCreatedEvent(new HeroCreatedEvent.Payload(
                heroSaved.getId(),
                heroSaved.getSpecies(),
                heroSaved.getHeroClass()
        )));

        return heroSaved.getId();
    }
}
