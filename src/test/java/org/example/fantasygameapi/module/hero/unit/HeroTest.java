package org.example.fantasygameapi.module.hero.unit;

import com.example.fantasygame.api.module.hero.model.Hero;
import com.example.fantasygame.api.module.hero.model.InvalidHeroClassException;
import com.example.fantasygame.api.module.hero.model.InvalidHeroNameException;
import com.example.fantasygame.api.module.hero.model.InvalidHeroSpeciesException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HeroTest {

    private static Hero heroNamed(String name) {
        return new Hero(UUID.randomUUID(), UUID.randomUUID(), name, "HUMAN", "WARRIOR");
    }

    // --- Valid names ---

    @Test
    void shouldCreateHero_whenNameHasMinimumLength() {
        assertDoesNotThrow(() -> heroNamed("Yo"));
    }

    @Test
    void shouldCreateHero_whenNameHasMaximumLength() {
        assertDoesNotThrow(() -> heroNamed("a".repeat(30)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Yaris", "Jean Pierre", "Éowyn"})
    void shouldKeepName_whenNameContainsOnlyLettersAndInnerSpaces(String name) {
        assertEquals(name, heroNamed(name).getName());
    }

    // --- Length ---

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowInvalidHeroNameException_whenNameIsNullOrEmpty(String name) {
        assertThrows(InvalidHeroNameException.class, () -> heroNamed(name));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameIsShorterThanMinimum() {
        assertThrows(InvalidHeroNameException.class, () -> heroNamed("A"));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameIsLongerThanMaximum() {
        assertThrows(InvalidHeroNameException.class, () -> heroNamed("a".repeat(31)));
    }

    // --- Allowed characters ---

    @ParameterizedTest
    @ValueSource(strings = {"Yaris2", "Ya_ris", "Ya-ris", "Yaris!", "Ya\tris"})
    void shouldThrowInvalidHeroNameException_whenNameContainsNonLetterCharacter(String name) {
        assertThrows(InvalidHeroNameException.class, () -> heroNamed(name));
    }

    // --- Leading and trailing spaces ---

    @Test
    void shouldThrowInvalidHeroNameException_whenNameStartsWithSpace() {
        assertThrows(InvalidHeroNameException.class, () -> heroNamed(" Yaris"));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameEndsWithSpace() {
        assertThrows(InvalidHeroNameException.class, () -> heroNamed("Yaris "));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameContainsOnlySpaces() {
        assertThrows(InvalidHeroNameException.class, () -> heroNamed("  "));
    }

    private static Hero heroOfSpecies(String species) {
        return new Hero(UUID.randomUUID(), UUID.randomUUID(), "Yaris", species, "WARRIOR");
    }

// --- Valid species ---

    @ParameterizedTest
    @ValueSource(strings = {"HUMAN", "ELF", "DWARF", "ORC"})
    void shouldCreateHero_whenSpeciesIsSupported(String species) {
        assertEquals(species, heroOfSpecies(species).getSpecies());
    }

// --- Invalid species ---

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void shouldThrowInvalidHeroSpeciesException_whenSpeciesIsNullOrBlank(String species) {
        assertThrows(InvalidHeroSpeciesException.class, () -> heroOfSpecies(species));
    }

    @ParameterizedTest
    @ValueSource(strings = {"DRAGON", "GOBLIN", "Human", "human", "HUMAN "})
    void shouldThrowInvalidHeroSpeciesException_whenSpeciesIsNotSupported(String species) {
        assertThrows(InvalidHeroSpeciesException.class, () -> heroOfSpecies(species));
    }

    private static Hero heroOfClass(String heroClass) {
        return new Hero(UUID.randomUUID(), UUID.randomUUID(), "Yaris", "HUMAN", heroClass);
    }

// --- Valid classes ---

    @ParameterizedTest
    @ValueSource(strings = {"WARRIOR", "RANGER", "MAGE", "CLERIC"})
    void shouldCreateHero_whenClassIsSupported(String heroClass) {
        assertEquals(heroClass, heroOfClass(heroClass).getHeroClass());
    }

// --- Invalid classes ---

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void shouldThrowInvalidHeroClassException_whenClassIsNullOrBlank(String heroClass) {
        assertThrows(InvalidHeroClassException.class, () -> heroOfClass(heroClass));
    }

    @ParameterizedTest
    @ValueSource(strings = {"PIRATE", "NINJA", "Warrior", "warrior", "WARRIOR "})
    void shouldThrowInvalidHeroClassException_whenClassIsNotSupported(String heroClass) {
        assertThrows(InvalidHeroClassException.class, () -> heroOfClass(heroClass));
    }

}