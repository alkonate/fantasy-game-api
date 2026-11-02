package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.valueobject.AbilityType;
import com.example.fantasygame.api.module.hero.model.valueobject.WeaponType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeaponTypeTest {

    // One row per line of the weapons table: damage dice and governing ability

    @ParameterizedTest
    @CsvSource({"LONGSWORD,8", "AXE,10", "MACE,6", "DAGGER,4", "BOW,8", "STAFF,6"})
    void shouldRollTheDieOfTheTable_whenDamageIsRolled(WeaponType weapon, int sides) {
        assertEquals(sides, weapon.sides());
    }

    @ParameterizedTest
    @CsvSource({"LONGSWORD,1", "AXE,1", "MACE,1", "DAGGER,1", "BOW,1", "STAFF,1"})
    void shouldRollTheNumberOfDiceOfTheTable_whenDamageIsRolled(WeaponType weapon, int count) {
        assertEquals(count, weapon.diceCount());
    }

    @ParameterizedTest
    @CsvSource({"LONGSWORD,STRENGTH", "AXE,STRENGTH", "MACE,STRENGTH",
                "DAGGER,DEXTERITY", "BOW,DEXTERITY", "STAFF,STRENGTH"})
    void shouldUseTheAbilityOfTheTable_whenAttackIsComputed(WeaponType weapon, AbilityType expected) {
        assertEquals(expected, weapon.ability());
    }
}
