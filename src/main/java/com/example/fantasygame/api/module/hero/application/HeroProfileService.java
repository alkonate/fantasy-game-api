package com.example.fantasygame.api.module.hero.application;

import com.example.fantasygame.api.module.hero.model.HeroProfile;
import com.example.fantasygame.api.module.hero.model.HeroRepository;
import com.example.fantasygame.api.module.hero.model.valueobject.*;
import com.example.fantasygame.api.shared.contracts.IntegrationEventBus;
import com.example.fantasygame.api.shared.contracts.hero.HeroProfileCreatedIntegrationEvent;
import com.example.fantasygame.api.shared.contracts.hero.HeroProfileCreatedIntegrationEvent.Payload;


public class HeroProfileService implements CreateHeroProfileUseCase {
    private final HeroRepository repository;
    private final IntegrationEventBus bus;

    public HeroProfileService(HeroRepository heroRepo, IntegrationEventBus publisher) {
        this.repository = heroRepo;
        this.bus = publisher;
    }

    @Override
    public HeroProfileId create(CreateHeroProfileCommand cmd) {

        var profile = HeroProfile.create(
                new PlayerId(cmd.playerId()),
                new Name(cmd.name()),
                Species.valueOf(cmd.species()),
                HeroClass.valueOf(cmd.heroClass()),
                Abilities.chosenFromNames(cmd.abilities())
        );

        var profileId = repository.save(profile).id();

        bus.publish(new HeroProfileCreatedIntegrationEvent(new Payload(profileId.value())));

        return profileId;
    }
}
