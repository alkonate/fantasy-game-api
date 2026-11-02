package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidAbilityException;

import java.util.Objects;

public record Ability(AbilityType type, int score) {

    public static final int MIN_CHOSEN = 3;
    public static final int MAX_CHOSEN = 18;
    public static final int MAX_FINAL = 20;

    public Ability {
        Objects.requireNonNull(type, "type is required");
        if (score > MAX_FINAL) {
            throw new InvalidAbilityException(type + " cannot exceed " + MAX_FINAL + ".");
        }
    }

    /** Score chosen by the player: between 3 and 18. */
    public static Ability chosen(AbilityType type, int score) {
        if (score < MIN_CHOSEN || score > MAX_CHOSEN) {
            throw new InvalidAbilityException(
                    type + " must be between " + MIN_CHOSEN + " and " + MAX_CHOSEN + ".");
        }
        return new Ability(type, score);
    }

    /** Bonus or penalty (from the species), capped at 20. */
    public Ability withBonus(int bonus) {
        return new Ability(type, Math.min(MAX_FINAL, score + bonus));
    }

    /** (score - 10) / 2 rounded down: floorDiv, not Java's integer division. */
    public int modifier() {
        return Math.floorDiv(score - 10, 2);
    }
}
