package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHeroSpeciesException;
import com.example.fantasygame.api.module.hero.model.valueobject.Species;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static com.example.fantasygame.api.module.hero.model.valueobject.AbilityType.*;
import static org.junit.jupiter.api.Assertions.*;

class SpeciesTest {

    // --- Modifiers: one test per species, straight from the rules table ---

    @Test
    void shouldGiveOnePointToEveryAbility_whenSpeciesIsHuman() {
        assertEquals(Map.of(STRENGTH, 1, DEXTERITY, 1, CONSTITUTION, 1,
                INTELLIGENCE, 1, WISDOM, 1, CHARISMA, 1), Species.HUMAN.modifiers());
    }

    @Test
    void shouldBoostDexterityAndIntelligence_whenSpeciesIsElf() {
        assertEquals(Map.of(DEXTERITY, 2, INTELLIGENCE, 1), Species.ELF.modifiers());
    }

    @Test
    void shouldBoostConstitutionAndStrength_whenSpeciesIsDwarf() {
        assertEquals(Map.of(CONSTITUTION, 2, STRENGTH, 1), Species.DWARF.modifiers());
    }

    @Test
    void shouldBoostStrengthAndConstitutionButPenalizeIntelligence_whenSpeciesIsOrc() {
        assertEquals(Map.of(STRENGTH, 2, CONSTITUTION, 1, INTELLIGENCE, -1), Species.ORC.modifiers());
    }

    // --- Parsing ---

    @ParameterizedTest
    @ValueSource(strings = {"HUMAN", "ELF", "DWARF", "ORC"})
    void shouldParseSpecies_whenCodeIsSupported(String code) {
        assertEquals(code, Species.from(code).name());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void shouldThrowInvalidHeroSpeciesException_whenCodeIsNullOrBlank(String code) {
        assertThrows(InvalidHeroSpeciesException.class, () -> Species.from(code));
    }

    @ParameterizedTest
    @ValueSource(strings = {"DRAGON", "GOBLIN", "Human", "human", "HUMAN "})
    void shouldThrowInvalidHeroSpeciesException_whenCodeIsNotSupported(String code) {
        assertThrows(InvalidHeroSpeciesException.class, () -> Species.from(code));
    }
}
