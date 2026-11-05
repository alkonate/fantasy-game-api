package com.example.fantasygame.api.module.hero.model.Exception;

public class HeroNameAlreadyExistsException extends HeroValidationException{
    public HeroNameAlreadyExistsException(String message) {
        super(message);
    }
}
