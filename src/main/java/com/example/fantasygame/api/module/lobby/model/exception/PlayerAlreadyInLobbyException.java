package com.example.fantasygame.api.module.lobby.model.exception;

public class PlayerAlreadyInLobbyException extends RuntimeException {
    public PlayerAlreadyInLobbyException(String message) {
        super(message);
    }
}
