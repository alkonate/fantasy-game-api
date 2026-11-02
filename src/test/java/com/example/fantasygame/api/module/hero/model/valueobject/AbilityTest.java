package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidAbilityException;
import com.example.fantasygame.api.module.hero.model.valueobject.Ability;
import com.example.fantasygame.api.module.hero.model.valueobject.AbilityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AbilityTest {

    // --- Modifier: one row per line of the rules table ---

    @ParameterizedTest
    @CsvSource({
            "3,-4", "4,-3", "5,-3", "6,-2", "7,-2", "8,-1", "9,-1",
            "10,0", "11,0", "12,1", "13,1", "14,2", "15,2",
            "16,3", "17,3", "18,4", "19,4", "20,5"
    })
    void shouldComputeModifier_whenScoreIsGiven(int score, int expected) {
        assertEquals(expected, new Ability(AbilityType.STRENGTH, score).modifier());
    }

    // --- Chosen score ---

    @ParameterizedTest
    @ValueSource(ints = {3, 10, 18})
    void shouldAcceptChosenScore_whenBetweenThreeAndEighteen(int score) {
        assertEquals(score, Ability.chosen(AbilityType.STRENGTH, score).score());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 19, 0, -1})
    void shouldThrowInvalidAbilityException_whenChosenScoreIsOutOfRange(int score) {
        assertThrows(InvalidAbilityException.class, () -> Ability.chosen(AbilityType.STRENGTH, score));
    }

    // --- Bonus ---

    @Test
    void shouldAddBonus_whenBonusIsPositive() {
        assertEquals(12, Ability.chosen(AbilityType.DEXTERITY, 10).withBonus(2).score());
    }

    @Test
    void shouldSubtractPenalty_whenBonusIsNegative() {
        assertEquals(9, Ability.chosen(AbilityType.INTELLIGENCE, 10).withBonus(-1).score());
    }

    @Test
    void shouldCapAtTwenty_whenBonusExceedsLimit() {
        assertEquals(20, Ability.chosen(AbilityType.STRENGTH, 18).withBonus(3).score());   // 21 -> 20
    }

    @Test
    void shouldKeepType_whenBonusIsApplied() {
        assertEquals(AbilityType.WISDOM, Ability.chosen(AbilityType.WISDOM, 10).withBonus(1).type());
    }

    @Test
    void shouldNotModifyOriginal_whenBonusIsApplied() {
        var original = Ability.chosen(AbilityType.STRENGTH, 10);
        original.withBonus(2);
        assertEquals(10, original.score());
    }

    @Test
    void shouldAllowScoreBelowThree_whenPenaltyIsApplied() {
        // Open question for the client: the rules give no floor after a species penalty.
        assertEquals(2, Ability.chosen(AbilityType.INTELLIGENCE, 3).withBonus(-1).score());
    }

    // --- Construction ---

    @Test
    void shouldThrowInvalidAbilityException_whenScoreIsBuiltAboveTwenty() {
        assertThrows(InvalidAbilityException.class, () -> new Ability(AbilityType.STRENGTH, 21));
    }

    @Test
    void shouldThrowNullPointerException_whenTypeIsNull() {
        assertThrows(NullPointerException.class, () -> new Ability(null, 10));
    }
}
