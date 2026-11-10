package com.example.fantasygame.api.module.lobby.model.valueobject;

/**
 * Launching a game is an orchestrated saga: the lobby is locked, the game is created, then the lobby
 * is closed (or unlocked back to open if the creation fails). The lobby is also closed when its creator leaves.
 */
public enum LobbyStatus {
    OPEN,
    LOCKED,
    CLOSED
}
