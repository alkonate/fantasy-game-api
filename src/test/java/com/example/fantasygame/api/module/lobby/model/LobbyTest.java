package com.example.fantasygame.api.module.lobby.model;

import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LobbyTest {

    private final PlayerId host = PlayerId.generate();
    private final PlayerId newcomer = PlayerId.generate();

    @Test
    void shouldAddPlayerAfterPlayersAlreadyPresent_whenPlayerJoins() {
        var lobby = Lobby.create(host).join(newcomer);

        assertEquals(List.of(host, newcomer), lobby.players());
    }

    @Test
    void shouldKeepHostAndIdentity_whenPlayerJoins() {
        var lobby = Lobby.create(host);

        var joined = lobby.join(newcomer);

        assertEquals(host, joined.host());
        assertEquals(lobby.id(), joined.id());
    }
}
