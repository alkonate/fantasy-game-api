package com.example.fantasygame.api.module.lobby.model.exception;

public class LobbyNotFoundException extends RuntimeException {
    public LobbyNotFoundException(String message) {
        super(message);
    }
}
