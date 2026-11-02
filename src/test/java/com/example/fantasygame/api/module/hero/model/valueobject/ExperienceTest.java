package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidExperienceException;
import com.example.fantasygame.api.module.hero.model.valueobject.Experience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ExperienceTest {

    // --- Level derived from total XP ---

    @ParameterizedTest
    @CsvSource({
            "0,1", "299,1",
            "300,2", "899,2",
            "900,3", "2699,3",
            "2700,4", "6499,4",
            "6500,5", "99999,5"        // level 5 is the maximum in version 1
    })
    void shouldDeriveLevel_whenPointsAreGiven(int points, int expectedLevel) {
        assertEquals(expectedLevel, new Experience(points).level());
    }

    // --- Start ---

    @Test
    void shouldStartAtZeroPoints_whenNoExperienceYet() {
        assertEquals(0, Experience.none().points());
    }

    @Test
    void shouldStartAtLevelOne_whenNoExperienceYet() {
        assertEquals(1, Experience.none().level());
    }

    // --- Gaining experience ---

    @Test
    void shouldAddPoints_whenExperienceIsGained() {
        assertEquals(350, new Experience(300).plus(50).points());
    }

    @Test
    void shouldRaiseLevel_whenGainedPointsReachNextThreshold() {
        assertEquals(2, Experience.none().plus(300).level());
    }

    @Test
    void shouldSkipLevels_whenGainIsLargeEnough() {
        assertEquals(3, Experience.none().plus(900).level());
    }

    @Test
    void shouldNotModifyOriginal_whenExperienceIsGained() {
        var original = new Experience(100);
        original.plus(50);
        assertEquals(100, original.points());
    }

    // --- Invalid values ---

    @Test
    void shouldThrowInvalidExperienceException_whenPointsAreNegative() {
        assertThrows(InvalidExperienceException.class, () -> new Experience(-1));
    }

    @Test
    void shouldThrowInvalidExperienceException_whenGainedPointsAreNegative() {
        assertThrows(InvalidExperienceException.class, () -> new Experience(100).plus(-1));
    }
}
