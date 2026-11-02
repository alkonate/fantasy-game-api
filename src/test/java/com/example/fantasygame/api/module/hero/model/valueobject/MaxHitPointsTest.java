package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHitPointsException;
import com.example.fantasygame.api.module.hero.model.valueobject.MaxHitPoints;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaxHitPointsTest {

    // --- Level 1 ---

    @Test
    void shouldAddConstitutionModifierToClassBase_whenCreatedAtLevelOne() {
        // Example from the rules: Warrior (12) with Constitution 16 (+3) => 15
        assertEquals(15, MaxHitPoints.atLevelOne(12, 3).value());
    }

    @Test
    void shouldSubtractConstitutionPenalty_whenModifierIsNegative() {
        assertEquals(9, MaxHitPoints.atLevelOne(12, -3).value());
    }

    @Test
    void shouldNeverBeBelowOne_whenPenaltyExceedsClassBase() {
        assertEquals(1, MaxHitPoints.atLevelOne(2, -4).value());
    }

    // --- Level up ---

    @Test
    void shouldGainHalfClassBasePlusModifier_whenLevelingUp() {
        // Warrior: 12 / 2 + 3 = 9
        assertEquals(24, new MaxHitPoints(15).afterLevelUp(12, 3).value());
    }

    @Test
    void shouldRoundHalfClassBaseDown_whenClassBaseIsOdd() {
        // base 5 / 2 = 2, + 0
        assertEquals(12, new MaxHitPoints(10).afterLevelUp(5, 0).value());
    }

    @Test
    void shouldGainAtLeastOne_whenModifierWouldCancelTheGain() {
        // Mage: 6 / 2 - 4 = -1 => minimum gain is 1
        assertEquals(8, new MaxHitPoints(7).afterLevelUp(6, -4).value());
    }

    @Test
    void shouldNotModifyOriginal_whenLevelingUp() {
        var original = new MaxHitPoints(15);
        original.afterLevelUp(12, 3);
        assertEquals(15, original.value());
    }

    // --- Invalid values ---

    @Test
    void shouldThrowInvalidHitPointsException_whenValueIsBelowOne() {
        assertThrows(InvalidHitPointsException.class, () -> new MaxHitPoints(0));
    }
}
