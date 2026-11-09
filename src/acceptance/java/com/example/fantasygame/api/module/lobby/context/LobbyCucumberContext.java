package com.example.fantasygame.api.module.lobby.context;

import com.example.fantasygame.api.module.lobby.application.LobbyService;
import com.example.fantasygame.api.module.lobby.infrastructure.FakePlayerGateway;
import com.example.fantasygame.api.module.lobby.infrastructure.TestInMemoryEventBus;
import com.example.fantasygame.api.module.lobby.infrastructure.TestInMemoryLobbyRepository;

import java.util.Optional;

public class LobbyCucumberContext {

    // Created once per scenario by PicoContainer. The service receives these same instances,
    // so what the DSL does on the fakes is what the service sees.
    private final TestInMemoryLobbyRepository repository = new ();
    private final TestInMemoryEventBus bus = new TestInMemoryEventBus();
    private final FakePlayerGateway playerGateway = new FakePlayerGateway();
    private final LobbyService lobbyService = new LobbyService(repository, bus, playerGateway);

    private Object lastResult;
    private Exception caughtException;

    public InMemoryLobbyRepository getRepository() { return repository; }
    public TestEventBus getBus()                    { return bus; }
    public FakePlayerGateway getPlayerGateway()     { return playerGateway; }
    public LobbyService getLobbyService()           { return lobbyService; }

    public void setLastResult(Object result)        { this.lastResult = result; }
    public Exception getCaughtException()           { return caughtException; }
    public void setCaughtException(Exception e)     { this.caughtException = e; }

    public <T> Optional<T> getLastResultAs(Class<T> type) {
        return Optional.ofNullable(lastResult)
                .filter(type::isInstance)
                .map(type::cast);
    }
}