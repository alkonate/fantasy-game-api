package com.example.fantasygame.api.module.lobby.model.exception;

public class LobbyFullException extends RuntimeException {
    public LobbyFullException(String message) {
        super(message);
    }
}
