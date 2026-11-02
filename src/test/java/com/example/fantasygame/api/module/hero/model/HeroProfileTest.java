package com.example.fantasygame.api.module.hero.model;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidAbilityException;
import com.example.fantasygame.api.module.hero.model.valueobject.*;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Assembly only: the rules of each value object (name format, 3 to 18, cap at 20,
 * modifier, HP minimum...) are tested in their own test class.
 */
class HeroProfileTest {

    // --- Helpers ---

    private static Map<AbilityType, Integer> all() {
        Map<AbilityType, Integer> map = new EnumMap<>(AbilityType.class);
        for (AbilityType type : AbilityType.values()) map.put(type, 10);
        return map;
    }

    private static Map<AbilityType, Integer> allExcept(AbilityType type, int value) {
        var map = all();
        map.put(type, value);
        return map;
    }

    private static HeroProfile profile(Species species, HeroClass heroClass, Map<AbilityType, Integer> abilities) {
        return HeroProfile.create(PlayerId.generate(), new Name("Yal"), species, heroClass, Abilities.chosen(abilities));
    }

    // --- Starting state ---

    @Test
    void shouldStartAtLevelOne_whenProfileIsCreated() {
        assertEquals(1, profile(Species.HUMAN, HeroClass.WARRIOR, all()).experience().level());
    }

    @Test
    void shouldStartWithNoExperience_whenProfileIsCreated() {
        assertEquals(0, profile(Species.HUMAN, HeroClass.WARRIOR, all()).experience().points());
    }

    // --- Identity and chosen data ---

    @Test
    void shouldKeepChosenIdentity_whenProfileIsCreated() {
        var playerId = PlayerId.generate();

        var profile = HeroProfile.create(playerId, new Name("Aria"), Species.ELF, HeroClass.MAGE, Abilities.chosen(all()));

        assertEquals(playerId, profile.playerId());
        assertEquals(new Name("Aria"), profile.name());
        assertEquals(Species.ELF, profile.species());
        assertEquals(HeroClass.MAGE, profile.heroClass());
    }

    @Test
    void shouldGenerateDifferentIds_whenTwoProfilesAreCreated() {
        assertNotEquals(
                profile(Species.HUMAN, HeroClass.WARRIOR, all()).id(),
                profile(Species.HUMAN, HeroClass.WARRIOR, all()).id());
    }

    // --- Species bonuses are applied after the chosen scores ---

    @Test
    void shouldApplySpeciesBonus_whenProfileIsCreated() {
        var profile = profile(Species.ELF, HeroClass.MAGE, all());

        assertEquals(12, profile.abilities().get(AbilityType.DEXTERITY).score());      // +2
        assertEquals(11, profile.abilities().get(AbilityType.INTELLIGENCE).score());   // +1
    }

    @Test
    void shouldLeaveAbilitiesWithoutBonusUntouched_whenProfileIsCreated() {
        var profile = profile(Species.ELF, HeroClass.MAGE, all());

        assertEquals(10, profile.abilities().get(AbilityType.STRENGTH).score());
    }

    @Test
    void shouldApplySpeciesPenalty_whenSpeciesIsOrc() {
        var profile = profile(Species.ORC, HeroClass.WARRIOR, all());

        assertEquals(9, profile.abilities().get(AbilityType.INTELLIGENCE).score());    // -1
    }

    // --- Max HP: class base + modifier of the FINAL Constitution ---

    @Test
    void shouldComputeMaxHp_whenExampleFromTheRulesIsUsed() {
        // Warrior (12), final Constitution 16 => +3 => 15. Human adds +1: choose 15.
        var profile = profile(Species.HUMAN, HeroClass.WARRIOR, allExcept(AbilityType.CONSTITUTION, 15));

        assertEquals(15, profile.maxHitPoints().value());
    }

    @Test
    void shouldUseConstitutionAfterSpeciesBonus_whenMaxHpIsComputed() {
        // Dwarf: chosen 14 + 2 = 16 => +3 => 15 (using 14 directly would give +2 => 14)
        var profile = profile(Species.DWARF, HeroClass.WARRIOR, allExcept(AbilityType.CONSTITUTION, 14));

        assertEquals(15, profile.maxHitPoints().value());
    }

    @Test
    void shouldRoundModifierDown_whenFinalConstitutionIsOdd() {
        // Scenario of the feature: Human, chosen 4 + 1 = 5 => floor(-2.5) = -3 => 12 - 3 = 9
        var profile = profile(Species.HUMAN, HeroClass.WARRIOR, allExcept(AbilityType.CONSTITUTION, 4));

        assertEquals(9, profile.maxHitPoints().value());
    }

    @Test
    void shouldUseClassBaseHp_whenClassIsMage() {
        // Elf has no Constitution bonus; Constitution 10 => +0 => 6
        assertEquals(6, profile(Species.ELF, HeroClass.MAGE, all()).maxHitPoints().value());
    }

    // --- Data derived from the class ---

    @Test
    void shouldExposeMaxMagicOfTheClass_whenProfileIsCreated() {
        assertEquals(10, profile(Species.ELF, HeroClass.MAGE, all()).maxMagic());
    }

    @Test
    void shouldExposeDominantAbilityOfTheClass_whenProfileIsCreated() {
        assertEquals(AbilityType.INTELLIGENCE, profile(Species.ELF, HeroClass.MAGE, all()).dominantAbility());
    }

    @Test
    void shouldExposeStartingWeaponOfTheClass_whenProfileIsCreated() {
        assertEquals(WeaponType.STAFF, profile(Species.ELF, HeroClass.MAGE, all()).startingWeapon());
    }

    @Test
    void shouldExposeStartingArmorOfTheClass_whenProfileIsCreated() {
        assertEquals(ArmorType.NONE, profile(Species.ELF, HeroClass.MAGE, all()).startingArmor());
    }

    // --- Rejections (the rule itself is tested on the value object) ---

    @Test
    void shouldRejectChosenScoreAboveEighteen_whenProfileIsCreated() {
        assertThrows(InvalidAbilityException.class,
                () -> profile(Species.HUMAN, HeroClass.WARRIOR, allExcept(AbilityType.STRENGTH, 19)));
    }

    @Test
    void shouldRejectChosenScoreBelowThree_whenProfileIsCreated() {
        assertThrows(InvalidAbilityException.class,
                () -> profile(Species.HUMAN, HeroClass.WARRIOR, allExcept(AbilityType.STRENGTH, 2)));
    }

    @Test
    void shouldRejectMissingAbility_whenProfileIsCreated() {
        var incomplete = all();
        incomplete.remove(AbilityType.CHARISMA);

        assertThrows(InvalidAbilityException.class,
                () -> profile(Species.HUMAN, HeroClass.WARRIOR, incomplete));
    }

    @Test
    void shouldRequireEveryArgument_whenProfileIsCreated() {
        var playerId = PlayerId.generate();
        var name = new Name("Yal");
        var abilities = Abilities.chosen(all());
        assertThrows(NullPointerException.class,
                () -> HeroProfile.create(null, name, Species.HUMAN, HeroClass.WARRIOR, abilities));
        assertThrows(NullPointerException.class,
                () -> HeroProfile.create(playerId, null, Species.HUMAN, HeroClass.WARRIOR, abilities));
        assertThrows(NullPointerException.class,
                () -> HeroProfile.create(playerId, name, null, HeroClass.WARRIOR, abilities));
        assertThrows(NullPointerException.class,
                () -> HeroProfile.create(playerId, name, Species.HUMAN, null, abilities));
    }
}
