package com.example.fantasygame.api.module.lobby.infrastructure;

import com.example.fantasygame.api.module.lobby.model.Lobby;
import com.example.fantasygame.api.module.lobby.model.LobbyRepository;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;

import java.util.HashMap;
import java.util.Map;

public class InMemoryLobbyRepository implements LobbyRepository {
    protected final Map<LobbyId, Lobby> lobbies = new HashMap<>();

    @Override
    public Lobby save(Lobby lobby) {
        lobbies.put(lobby.id(), lobby);
        return lobby;
    }

    @Override
    public boolean existsWithPlayer(PlayerId player) {
        return lobbies.values().stream().anyMatch(lobby -> lobby.contains(player));
    }
}
