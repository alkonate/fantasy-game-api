package com.example.fantasygame.api.module.hero.model.valueobject;

public enum WeaponType {
    LONGSWORD(1, 8, AbilityType.STRENGTH),
    AXE(1, 10, AbilityType.STRENGTH),
    MACE(1, 6, AbilityType.STRENGTH),
    DAGGER(1, 4, AbilityType.DEXTERITY),
    BOW(1, 8, AbilityType.DEXTERITY),
    STAFF(1, 6, AbilityType.STRENGTH);

    private final int diceCount;
    private final int sides;
    private final AbilityType ability;

    WeaponType(int diceCount, int sides, AbilityType ability) {
        this.diceCount = diceCount;
        this.sides = sides;
        this.ability = ability;
    }

    public int diceCount()        { return diceCount; }
    public int sides()            { return sides; }
    public AbilityType ability()  { return ability; }
}
