package com.example.fantasygame.api.module.lobby.model.exception;

public class LobbyClosedException extends RuntimeException {
    public LobbyClosedException(String message) {
        super(message);
    }
}
