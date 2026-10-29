package com.example.fantasygame.api.module.hero.model;

public class HeroNameAlreadyExistsException extends RuntimeException {
    public HeroNameAlreadyExistsException() {
        super("Hero registration failed");
    }
}
