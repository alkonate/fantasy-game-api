package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHeroClassException;
import com.example.fantasygame.api.module.hero.model.valueobject.AbilityType;
import com.example.fantasygame.api.module.hero.model.valueobject.ArmorType;
import com.example.fantasygame.api.module.hero.model.valueobject.HeroClass;
import com.example.fantasygame.api.module.hero.model.valueobject.WeaponType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HeroClassTest {

    // --- Class table: one test per column ---

    @ParameterizedTest
    @CsvSource({"WARRIOR,12", "RANGER,10", "MAGE,6", "CLERIC,10"})
    void shouldHaveBaseHp_whenClassIsDefined(HeroClass heroClass, int expected) {
        assertEquals(expected, heroClass.baseHp());
    }

    @ParameterizedTest
    @CsvSource({"WARRIOR,0", "RANGER,0", "MAGE,10", "CLERIC,8"})
    void shouldHaveMaxMagic_whenClassIsDefined(HeroClass heroClass, int expected) {
        assertEquals(expected, heroClass.maxMagic());
    }

    @ParameterizedTest
    @CsvSource({"WARRIOR,STRENGTH", "RANGER,DEXTERITY", "MAGE,INTELLIGENCE", "CLERIC,WISDOM"})
    void shouldHaveDominantAbility_whenClassIsDefined(HeroClass heroClass, AbilityType expected) {
        assertEquals(expected, heroClass.dominantAbility());
    }

    @ParameterizedTest
    @CsvSource({"WARRIOR,LONGSWORD", "RANGER,BOW", "MAGE,STAFF", "CLERIC,MACE"})
    void shouldHaveStartingWeapon_whenClassIsDefined(HeroClass heroClass, WeaponType expected) {
        assertEquals(expected, heroClass.startingWeapon());
    }

    @ParameterizedTest
    @CsvSource({"WARRIOR,CHAIN_MAIL", "RANGER,LEATHER", "MAGE,NONE", "CLERIC,CHAIN_MAIL"})
    void shouldHaveStartingArmor_whenClassIsDefined(HeroClass heroClass, ArmorType expected) {
        assertEquals(expected, heroClass.startingArmor());
    }

    // --- Consistency: a class can use what it starts with ---

    @ParameterizedTest
    @EnumSource(HeroClass.class)
    void shouldAllowItsStartingWeapon_whenClassIsDefined(HeroClass heroClass) {
        assertTrue(heroClass.canUse(heroClass.startingWeapon()));
    }

    @ParameterizedTest
    @EnumSource(HeroClass.class)
    void shouldAllowItsStartingArmor_whenClassIsDefined(HeroClass heroClass) {
        assertTrue(heroClass.canWear(heroClass.startingArmor()));
    }

    // --- Equipment restrictions ---

    @Test
    void shouldNotUseBow_whenClassIsWarrior() {
        assertFalse(HeroClass.WARRIOR.canUse(WeaponType.BOW));
    }

    @Test
    void shouldWearHeavyArmor_whenClassIsWarrior() {
        assertTrue(HeroClass.WARRIOR.canWear(ArmorType.CHAIN_MAIL));
    }

    @Test
    void shouldUseBowDaggerAndSword_whenClassIsRanger() {
        assertTrue(HeroClass.RANGER.canUse(WeaponType.BOW));
        assertTrue(HeroClass.RANGER.canUse(WeaponType.DAGGER));
        assertTrue(HeroClass.RANGER.canUse(WeaponType.LONGSWORD));
    }

    @Test
    void shouldNotWearHeavyArmor_whenClassIsRanger() {
        assertFalse(HeroClass.RANGER.canWear(ArmorType.CHAIN_MAIL));
    }

    @Test
    void shouldUseOnlyStaffAndDagger_whenClassIsMage() {
        assertTrue(HeroClass.MAGE.canUse(WeaponType.STAFF));
        assertTrue(HeroClass.MAGE.canUse(WeaponType.DAGGER));
        assertFalse(HeroClass.MAGE.canUse(WeaponType.LONGSWORD));
    }

    @Test
    void shouldWearNoArmor_whenClassIsMage() {
        assertFalse(HeroClass.MAGE.canWear(ArmorType.LEATHER));
        assertFalse(HeroClass.MAGE.canWear(ArmorType.CHAIN_MAIL));
    }

    @Test
    void shouldUseMaceAndStaffOnly_whenClassIsCleric() {
        assertTrue(HeroClass.CLERIC.canUse(WeaponType.MACE));
        assertTrue(HeroClass.CLERIC.canUse(WeaponType.STAFF));
        assertFalse(HeroClass.CLERIC.canUse(WeaponType.LONGSWORD));
    }

    @Test
    void shouldWearHeavyArmor_whenClassIsCleric() {
        assertTrue(HeroClass.CLERIC.canWear(ArmorType.CHAIN_MAIL));
    }

    // --- Parsing ---

    @ParameterizedTest
    @ValueSource(strings = {"WARRIOR", "RANGER", "MAGE", "CLERIC"})
    void shouldParseClass_whenCodeIsSupported(String code) {
        assertEquals(code, HeroClass.from(code).name());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void shouldThrowInvalidHeroClassException_whenCodeIsNullOrBlank(String code) {
        assertThrows(InvalidHeroClassException.class, () -> HeroClass.from(code));
    }

    @ParameterizedTest
    @ValueSource(strings = {"PIRATE", "NINJA", "Warrior", "warrior", "WARRIOR "})
    void shouldThrowInvalidHeroClassException_whenCodeIsNotSupported(String code) {
        assertThrows(InvalidHeroClassException.class, () -> HeroClass.from(code));
    }
}
