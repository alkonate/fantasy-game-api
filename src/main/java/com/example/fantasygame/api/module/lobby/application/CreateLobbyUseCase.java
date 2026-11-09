package com.example.fantasygame.api.module.lobby.application;

import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;

public interface CreateLobbyUseCase {
    LobbyId create(CreateLobbyCommand cmd);
}
