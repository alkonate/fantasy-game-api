package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHeroSpeciesException;

import java.util.Map;

import static com.example.fantasygame.api.module.hero.model.valueobject.AbilityType.*;

public enum Species {
    HUMAN(Map.of(STRENGTH, 1, DEXTERITY, 1, CONSTITUTION, 1, INTELLIGENCE, 1, WISDOM, 1, CHARISMA, 1)),
    ELF(Map.of(DEXTERITY, 2, INTELLIGENCE, 1)),
    DWARF(Map.of(CONSTITUTION, 2, STRENGTH, 1)),
    ORC(Map.of(STRENGTH, 2, CONSTITUTION, 1, INTELLIGENCE, -1));

    private final Map<AbilityType, Integer> modifiers;

    Species(Map<AbilityType, Integer> modifiers) { this.modifiers = modifiers; }

    public Map<AbilityType, Integer> modifiers() { return modifiers; }

    public static Species from(String value) {
        if (value == null) {
            throw new InvalidHeroSpeciesException("Species is required.");
        }
        try {
            return valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidHeroSpeciesException("Unknown species: " + value);
        }
    }
}
