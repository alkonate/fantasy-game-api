package com.example.fantasygame.api.module.lobby.model;

import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;

public interface LobbyRepository {
    Lobby save(Lobby lobby);
    boolean existsWithPlayer(PlayerId player);
}
