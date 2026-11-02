package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidAbilityException;

public enum AbilityType {
    STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA;

    public static AbilityType from(String value) {
        if (value == null) {
            throw new InvalidAbilityException("Ability type is required.");
        }
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidAbilityException("Unknown ability type: " + value);
        }
    }
}
