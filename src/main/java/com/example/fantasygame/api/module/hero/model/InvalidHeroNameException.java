package com.example.fantasygame.api.module.hero.model;

public class InvalidHeroNameException extends RuntimeException {
    public InvalidHeroNameException(String message) {
        super(message);
    }
}
