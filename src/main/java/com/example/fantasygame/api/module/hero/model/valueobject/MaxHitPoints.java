package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHitPointsException;

public record MaxHitPoints(int value) {

    public MaxHitPoints {
        if (value < 1) {
            throw new InvalidHitPointsException("Max hit points cannot be below 1.");
        }
    }

    /** Class base HP + Constitution modifier, never below 1. */
    public static MaxHitPoints atLevelOne(int classBaseHp, int constitutionModifier) {
        return new MaxHitPoints(Math.max(1, classBaseHp + constitutionModifier));
    }

    /** Gain per level: class base HP / 2 + Constitution modifier, never below 1. */
    public MaxHitPoints afterLevelUp(int classBaseHp, int constitutionModifier) {
        return new MaxHitPoints(value + Math.max(1, classBaseHp / 2 + constitutionModifier));
    }
}
