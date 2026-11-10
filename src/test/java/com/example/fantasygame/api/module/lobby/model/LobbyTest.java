package com.example.fantasygame.api.module.lobby.model;

import com.example.fantasygame.api.module.lobby.model.exception.LobbyClosedException;
import com.example.fantasygame.api.module.lobby.model.exception.LobbyFullException;
import com.example.fantasygame.api.module.lobby.model.exception.LobbyLockedException;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyStatus;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LobbyTest {

    private final PlayerId host = PlayerId.generate();
    private final PlayerId newcomer = PlayerId.generate();

    // --- Helpers ---

    private Lobby lobbyWithPlayerCount(int count) {
        var lobby = Lobby.create(host);
        for (int i = 1; i < count; i++) lobby = lobby.join(PlayerId.generate());
        return lobby;
    }

    private Lobby lobbyWithStatus(LobbyStatus status) {
        return Lobby.restore(LobbyId.generate(), host, List.of(host), status);
    }

    // --- Creation ---

    @Test
    void shouldBeOpen_whenLobbyIsCreated() {
        assertEquals(LobbyStatus.OPEN, Lobby.create(host).status());
    }

    // --- Join ---

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

    @Test
    void shouldAcceptFourthPlayer_whenLobbyHasThreePlayers() {
        var lobby = lobbyWithPlayerCount(3).join(newcomer);

        assertEquals(4, lobby.players().size());
    }

    @Test
    void shouldRefusePlayer_whenLobbyHasFourPlayers() {
        var lobby = lobbyWithPlayerCount(4);

        assertThrows(LobbyFullException.class, () -> lobby.join(newcomer));
    }

    @Test
    void shouldRefusePlayer_whenLobbyIsLocked() {
        var lobby = lobbyWithStatus(LobbyStatus.LOCKED);

        assertThrows(LobbyLockedException.class, () -> lobby.join(newcomer));
    }

    @Test
    void shouldRefusePlayer_whenLobbyIsClosed() {
        var lobby = lobbyWithStatus(LobbyStatus.CLOSED);

        assertThrows(LobbyClosedException.class, () -> lobby.join(newcomer));
    }

    // --- Restore ---

    @Test
    void shouldKeepStoredState_whenLobbyIsRestored() {
        var id = LobbyId.generate();
        var players = new ArrayList<>(List.of(host, newcomer));

        var lobby = Lobby.restore(id, host, players, LobbyStatus.CLOSED);

        assertEquals(id, lobby.id());
        assertEquals(host, lobby.host());
        assertEquals(List.of(host, newcomer), lobby.players());
        assertEquals(LobbyStatus.CLOSED, lobby.status());
    }
}
