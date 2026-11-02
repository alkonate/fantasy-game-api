package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHeroNameException;
import com.example.fantasygame.api.module.hero.model.valueobject.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    // --- Valid names ---

    @ParameterizedTest
    @ValueSource(strings = {"Aria", "Thorin", "Eldrin", "Jean Pierre", "Éowyn"})
    void shouldKeepValue_whenNameIsValid(String value) {
        assertEquals(value, new Name(value).value());
    }

    @Test
    void shouldAccept_whenNameHasMinimumLength() {
        assertDoesNotThrow(() -> new Name("Yo"));
    }

    @Test
    void shouldAccept_whenNameHasMaximumLength() {
        assertDoesNotThrow(() -> new Name("a".repeat(30)));
    }

    // --- Length ---

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowInvalidHeroNameException_whenNameIsNullOrEmpty(String value) {
        assertThrows(InvalidHeroNameException.class, () -> new Name(value));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameIsShorterThanMinimum() {
        assertThrows(InvalidHeroNameException.class, () -> new Name("A"));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameIsLongerThanMaximum() {
        assertThrows(InvalidHeroNameException.class, () -> new Name("a".repeat(31)));
    }

    // --- Allowed characters (invalid examples come from the rules) ---

    @ParameterizedTest
    @ValueSource(strings = {"Mage123", "Thorin!", "_Gandalf_", "Player#42", "Ya-ris", "Ya\tris"})
    void shouldThrowInvalidHeroNameException_whenNameContainsNonLetterCharacter(String value) {
        assertThrows(InvalidHeroNameException.class, () -> new Name(value));
    }

    // --- Spaces ---

    @Test
    void shouldThrowInvalidHeroNameException_whenNameStartsWithSpace() {
        assertThrows(InvalidHeroNameException.class, () -> new Name(" Yaris"));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameEndsWithSpace() {
        assertThrows(InvalidHeroNameException.class, () -> new Name("Yaris "));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameContainsOnlySpaces() {
        assertThrows(InvalidHeroNameException.class, () -> new Name("  "));
    }

    @Test
    void shouldThrowInvalidHeroNameException_whenNameContainsDoubleSpace() {
        // Open question for the client: the rules do not say whether this is allowed.
        assertThrows(InvalidHeroNameException.class, () -> new Name("Jean  Pierre"));
    }

    // --- Equality ---

    @Test
    void shouldBeEqual_whenValuesAreEqual() {
        assertEquals(new Name("Yal"), new Name("Yal"));
    }
}
