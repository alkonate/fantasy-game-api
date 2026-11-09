package com.example.fantasygame.api.module.lobby.infrastructure;

import com.example.fantasygame.api.module.lobby.model.Lobby;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestInMemoryLobbyRepository extends InMemoryLobbyRepository {

    public TestInMemoryLobbyRepository assertCount(int count) {
        assertEquals(count, lobbies.size(), "lobbies size should be equal to " + count);
        return this;
    }

    public Optional<Lobby> findById(LobbyId id) {
        return Optional.ofNullable(lobbies.get(id));
    }

    public int count() {
        return lobbies.size();
    }
}
