package com.example.fantasygame.api.module.lobby.application;

import com.example.fantasygame.api.module.lobby.model.Lobby;
import com.example.fantasygame.api.module.lobby.model.LobbyRepository;
import com.example.fantasygame.api.module.lobby.model.exception.PlayerAlreadyInLobbyException;
import com.example.fantasygame.api.module.lobby.model.exception.PlayerNotFoundException;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;
import com.example.fantasygame.api.shared.contracts.IntegrationEventBus;
import com.example.fantasygame.api.shared.contracts.lobby.LobbyCreatedIntegrationEvent;
import com.example.fantasygame.api.shared.contracts.lobby.LobbyCreatedIntegrationEvent.Payload;

public class LobbyService implements CreateLobbyUseCase {
    private final LobbyRepository repository;
    private final IntegrationEventBus bus;
    private final PlayerGateway playerGateway;

    public LobbyService(LobbyRepository repository, IntegrationEventBus bus, PlayerGateway playerGateway) {
        this.repository = repository;
        this.bus = bus;
        this.playerGateway = playerGateway;
    }

    @Override
    public LobbyId create(CreateLobbyCommand cmd) {
        var player = new PlayerId(cmd.playerId());

        if (!playerGateway.exists(player)) {
            throw new PlayerNotFoundException("Player with id " + cmd.playerId() + " not found");
        }

        // Hypothesis Q3 (open question): a player can only be in one lobby at a time
        if (repository.existsWithPlayer(player)) {
            throw new PlayerAlreadyInLobbyException("Player with id " + cmd.playerId() + " is already in a lobby");
        }

        var lobbyId = repository.save(Lobby.create(player)).id();

        bus.publish(new LobbyCreatedIntegrationEvent(new Payload(lobbyId.value())));

        return lobbyId;
    }
}
