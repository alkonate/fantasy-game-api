package com.example.fantasygame.api.module.lobby.model;

import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;

import java.util.Optional;

public interface LobbyRepository {
    Lobby save(Lobby lobby);
    Optional<Lobby> findById(LobbyId id);
    boolean existsWithPlayer(PlayerId player);
}
