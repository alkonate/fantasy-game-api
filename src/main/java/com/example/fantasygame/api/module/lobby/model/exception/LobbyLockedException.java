package com.example.fantasygame.api.module.lobby.model.exception;

public class LobbyLockedException extends RuntimeException {
    public LobbyLockedException(String message) {
        super(message);
    }
}
