package com.example.fantasygame.api.module.hero.application;

import com.example.fantasygame.api.module.hero.model.Exception.HeroNameAlreadyExistsException;
import com.example.fantasygame.api.module.hero.model.Exception.PlayerNotFoundException;
import com.example.fantasygame.api.module.hero.model.HeroProfile;
import com.example.fantasygame.api.module.hero.model.HeroRepository;
import com.example.fantasygame.api.module.hero.model.valueobject.*;
import com.example.fantasygame.api.shared.contracts.IntegrationEventBus;
import com.example.fantasygame.api.shared.contracts.hero.HeroProfileCreatedIntegrationEvent;
import com.example.fantasygame.api.shared.contracts.hero.HeroProfileCreatedIntegrationEvent.Payload;


public class HeroProfileService implements CreateHeroProfileUseCase {
    private final HeroRepository repository;
    private final IntegrationEventBus bus;
    private final PlayerGateway playerGateway;

    public HeroProfileService(HeroRepository heroRepo, IntegrationEventBus publisher, PlayerGateway playerGateway) {
        this.repository = heroRepo;
        this.bus = publisher;
        this.playerGateway = playerGateway;
    }

    @Override
    public HeroProfileId create(CreateHeroProfileCommand cmd) {

        if(!playerGateway.exists(new PlayerId(cmd.playerId()))){
            throw new PlayerNotFoundException("Player with id " + cmd.playerId() + " not found");
        }

        var profileExist = repository.findByName(new Name(cmd.name()));
        if (profileExist.isPresent()) {
            throw new HeroNameAlreadyExistsException("HeroProfile with name " + cmd.name() + " already exists");
        }

        var profile = HeroProfile.create(
                new PlayerId(cmd.playerId()),
                new Name(cmd.name()),
                Species.from(cmd.species()),
                HeroClass.from(cmd.heroClass()),
                Abilities.chosenFromNames(cmd.abilities())
        );

        var profileId = repository.save(profile).id();

        bus.publish(new HeroProfileCreatedIntegrationEvent(new Payload(profileId.value())));

        return profileId;
    }
}
