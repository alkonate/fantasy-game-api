package com.example.fantasygame.api.module.hero.model.Exception;

public class PlayerNotFoundException extends HeroValidationException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
