package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidExperienceException;

public record Experience(int points) {

    // Total XP required for levels 1 to 5
    private static final int[] THRESHOLDS = {0, 300, 900, 2700, 6500};

    public Experience {
        if (points < 0) {
            throw new InvalidExperienceException("Experience cannot be negative.");
        }
    }

    public static Experience none() { return new Experience(0); }

    public Experience plus(int gained) {
        if (gained < 0) {
            throw new InvalidExperienceException("Gained experience cannot be negative.");
        }
        return new Experience(points + gained);
    }

    public int level() {
        int level = 1;
        for (int i = 1; i < THRESHOLDS.length; i++) {
            if (points >= THRESHOLDS[i]) level = i + 1;
        }
        return level;
    }
}
