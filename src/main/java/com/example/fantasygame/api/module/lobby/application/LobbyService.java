package com.example.fantasygame.api.module.lobby.application;

import com.example.fantasygame.api.module.lobby.model.LobbyRepository;

public class LobbyService implements CreateLobbyUseCase {
    private final LobbyRepository repository;
    private final PlayerGateway playerGateway;

    @Override
    public void execute(CreateLobbyCommand command) {

    }
}
