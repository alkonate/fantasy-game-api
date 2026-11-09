package com.example.fantasygame.api.module.lobby.model;

import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;

import java.util.List;
import java.util.Objects;

public final class Lobby {

    private final LobbyId id;
    private final PlayerId host;
    private final List<PlayerId> players;   // join order, host first

    private Lobby(LobbyId id, PlayerId host, List<PlayerId> players) {
        this.id = id;
        this.host = host;
        this.players = List.copyOf(players);
    }

    /** New lobby: the creator is the host and the first player. */
    public static Lobby create(PlayerId host) {
        Objects.requireNonNull(host, "host is required");
        return new Lobby(LobbyId.generate(), host, List.of(host));
    }

    public LobbyId id()             { return id; }
    public PlayerId host()          { return host; }
    public List<PlayerId> players() { return players; }

    public boolean contains(PlayerId player) { return players.contains(player); }

    @Override public boolean equals(Object o) {
        return o instanceof Lobby other && id.equals(other.id);
    }
    @Override public int hashCode() { return id.hashCode(); }
}
