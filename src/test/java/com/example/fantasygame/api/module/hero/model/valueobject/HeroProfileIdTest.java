package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.valueobject.HeroProfileId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HeroProfileIdTest {

    @Test
    void shouldBeEqual_whenUuidsAreEqual() {
        var uuid = UUID.randomUUID();
        assertEquals(new HeroProfileId(uuid), new HeroProfileId(uuid));
    }

    @Test
    void shouldGenerateDifferentIds_whenGeneratedTwice() {
        assertNotEquals(HeroProfileId.generate(), HeroProfileId.generate());
    }

    @Test
    void shouldThrowNullPointerException_whenValueIsNull() {
        assertThrows(NullPointerException.class, () -> new HeroProfileId(null));
    }
}
