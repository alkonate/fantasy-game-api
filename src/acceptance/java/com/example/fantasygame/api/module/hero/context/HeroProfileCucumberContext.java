package com.example.fantasygame.api.module.hero.context;

import com.example.fantasygame.api.module.hero.application.HeroProfileService;
import com.example.fantasygame.api.module.hero.infrastructure.FakePlayerGateway;
import com.example.fantasygame.api.module.hero.infrastructure.TestInMemoryEventBus;
import com.example.fantasygame.api.module.hero.infrastructure.TestInMemoryHeroProfileRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class HeroProfileCucumberContext {

    @Getter
    private final TestInMemoryHeroProfileRepository repository;
    @Getter
    private final FakePlayerGateway playerGateway;
    @Getter
    private final TestInMemoryEventBus bus;
    private final HeroProfileService heroService;
    @Setter
    private Object lastResult;

    @Setter
    @Getter
    private Exception caughtException;

    public HeroProfileCucumberContext() {
        this.playerGateway = new FakePlayerGateway();
        this.repository = new TestInMemoryHeroProfileRepository();
        this.bus = new TestInMemoryEventBus();
        this.heroService = new HeroProfileService(this.repository,  this.bus, this.playerGateway);
    }

    public HeroProfileService getHeroProfileService() {
        return heroService;
    }

    public <T> Optional<T>  getLastResultAs(Class<T> resultClass) {
        if (resultClass.isInstance(lastResult)) {
            return Optional.of(resultClass.cast(lastResult));
        }
        return Optional.empty();
    }

}
