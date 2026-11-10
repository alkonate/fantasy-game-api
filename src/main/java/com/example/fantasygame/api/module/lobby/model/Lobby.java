package com.example.fantasygame.api.module.lobby.model;

import com.example.fantasygame.api.module.lobby.model.exception.LobbyClosedException;
import com.example.fantasygame.api.module.lobby.model.exception.LobbyFullException;
import com.example.fantasygame.api.module.lobby.model.exception.LobbyLockedException;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyStatus;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Lobby {

    public static final int MAX_PLAYERS = 4;

    private final LobbyId id;
    private final PlayerId host;
    private final List<PlayerId> players;   // join order, host first
    private final LobbyStatus status;

    private Lobby(LobbyId id, PlayerId host, List<PlayerId> players, LobbyStatus status) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.host = Objects.requireNonNull(host, "host is required");
        this.players = List.copyOf(players);
        this.status = Objects.requireNonNull(status, "status is required");
    }

    /** New lobby: the creator is the host and the first player. */
    public static Lobby create(PlayerId host) {
        Objects.requireNonNull(host, "host is required");
        return new Lobby(LobbyId.generate(), host, List.of(host), LobbyStatus.OPEN);
    }

    /** Rebuilds a lobby already in storage. No rule is checked: the state was valid when it was saved. */
    public static Lobby restore(LobbyId id, PlayerId host, List<PlayerId> players, LobbyStatus status) {
        return new Lobby(id, host, players, status);
    }

    /** Returns the lobby with the player added after the players already present. */
    public Lobby join(PlayerId player) {
        Objects.requireNonNull(player, "player is required");
        if (status == LobbyStatus.LOCKED) {
            throw new LobbyLockedException("Lobby " + id.value() + " is locked: its game is being launched");
        }
        if (status == LobbyStatus.CLOSED) {
            throw new LobbyClosedException("Lobby " + id.value() + " is closed");
        }
        if (players.size() >= MAX_PLAYERS) {
            throw new LobbyFullException("Lobby " + id.value() + " already has " + MAX_PLAYERS + " players");
        }
        var joined = new ArrayList<>(players);
        joined.add(player);
        return new Lobby(id, host, joined, status);
    }

    public LobbyId id()             { return id; }
    public PlayerId host()          { return host; }
    public List<PlayerId> players() { return players; }
    public LobbyStatus status()     { return status; }

    public boolean contains(PlayerId player) { return players.contains(player); }

    @Override public boolean equals(Object o) {
        return o instanceof Lobby other && id.equals(other.id);
    }
    @Override public int hashCode() { return id.hashCode(); }
}
