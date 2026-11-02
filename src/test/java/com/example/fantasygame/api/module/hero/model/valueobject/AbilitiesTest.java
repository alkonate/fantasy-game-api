package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidAbilityException;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AbilitiesTest {

    private static Map<AbilityType, Integer> all() {
        Map<AbilityType, Integer> map = new EnumMap<>(AbilityType.class);
        for (AbilityType type : AbilityType.values()) map.put(type, 10);
        return map;
    }

    // --- Completeness: exactly one ability of each type ---

    @Test
    void shouldCreateAbilities_whenAllSixAbilitiesAreGiven() {
        assertEquals(6, Abilities.chosen(all()).values().size());
    }

    @Test
    void shouldThrowInvalidAbilityException_whenAnAbilityIsMissing() {
        var incomplete = all();
        incomplete.remove(AbilityType.CHARISMA);
        assertThrows(InvalidAbilityException.class, () -> Abilities.chosen(incomplete));
    }

    @Test
    void shouldThrowInvalidAbilityException_whenAnAbilityTypeIsDuplicated() {
        Set<Ability> duplicated = new HashSet<>(Abilities.chosen(all()).values());
        duplicated.removeIf(a -> a.type() == AbilityType.STRENGTH);
        duplicated.add(new Ability(AbilityType.DEXTERITY, 12));       // two DEXTERITY, no STRENGTH
        assertThrows(InvalidAbilityException.class, () -> new Abilities(duplicated));
    }

    @Test
    void shouldThrowInvalidAbilityException_whenAChosenScoreIsOutOfRange() {
        var invalid = all();
        invalid.put(AbilityType.STRENGTH, 19);
        assertThrows(InvalidAbilityException.class, () -> Abilities.chosen(invalid));
    }

    // --- Bonuses ---

    @Test
    void shouldApplyBonusToTheRightAbility_whenBonusesAreGiven() {
        var result = Abilities.chosen(all()).withBonuses(Map.of(AbilityType.DEXTERITY, 2));

        assertEquals(12, result.get(AbilityType.DEXTERITY).score());
    }

    @Test
    void shouldLeaveOtherAbilitiesUntouched_whenBonusesAreGiven() {
        var result = Abilities.chosen(all()).withBonuses(Map.of(AbilityType.DEXTERITY, 2));

        assertEquals(10, result.get(AbilityType.STRENGTH).score());
    }

    @Test
    void shouldCapAtTwenty_whenBonusExceedsLimit() {
        var chosen = all();
        chosen.put(AbilityType.STRENGTH, 18);

        var result = Abilities.chosen(chosen).withBonuses(Map.of(AbilityType.STRENGTH, 3));

        assertEquals(20, result.get(AbilityType.STRENGTH).score());
    }

    @Test
    void shouldNotModifyOriginal_whenBonusesAreApplied() {
        var original = Abilities.chosen(all());
        original.withBonuses(Map.of(AbilityType.STRENGTH, 2));
        assertEquals(10, original.get(AbilityType.STRENGTH).score());
    }

    // --- Modifier ---

    @Test
    void shouldReturnModifierOfTheRequestedAbility_whenModifierIsAsked() {
        var chosen = all();
        chosen.put(AbilityType.CONSTITUTION, 16);

        assertEquals(3, Abilities.chosen(chosen).modifier(AbilityType.CONSTITUTION));
    }
}
