package com.example.fantasygame.api.module.lobby.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestInMemoryLobbyRepository extends InMemoryLobbyRepository {

    public TestInMemoryLobbyRepository assertCount(int count) {
        assertEquals(count, lobbies.size(), "lobbies size should be equal to " + count);
        return this;
    }

    public int count() {
        return lobbies.size();
    }
}
