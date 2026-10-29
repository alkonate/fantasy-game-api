package org.example.fantasygameapi.module.hero.acceptance;

import com.example.fantasygame.api.module.hero.application.HeroService;
import org.example.fantasygameapi.module.hero.infrastructure.TestInMemoryEventBus;
import org.example.fantasygameapi.module.hero.infrastructure.TestInMemoryRepository;

import java.util.Optional;

public class HeroCucumberContext {

    private final TestInMemoryRepository repository;
    private final TestInMemoryEventBus bus;
    private final HeroService heroService;
    private Object lastResult;
    private Exception exception;

    public HeroCucumberContext() {
        this.repository = new TestInMemoryRepository();
        this.bus = new TestInMemoryEventBus();
        this.heroService = new HeroService(this.repository,  this.bus);
    }

    public TestInMemoryRepository getRepository() {
        return repository;
    }

    public TestInMemoryEventBus getBus() {
        return bus;
    }

    public HeroService getHeroService() {
        return heroService;
    }

    public void setCaughtException(Exception exception) {
        this.exception = exception;
    }

    public Object getCaughtException() {
        return exception;
    }

    public <T> Optional<T>  getLastResultAs(Class<T> resultClass) {
        if (resultClass.isInstance(lastResult)) {
            return Optional.of(resultClass.cast(lastResult));
        }
        return Optional.empty();
    }

    public void setLastResult(Object lastResult) {
        this.lastResult = lastResult;
    }

}
