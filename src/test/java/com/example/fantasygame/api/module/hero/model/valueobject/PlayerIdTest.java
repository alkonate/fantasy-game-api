package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.valueobject.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerIdTest {

    @Test
    void shouldBeEqual_whenUuidsAreEqual() {
        var uuid = UUID.randomUUID();
        assertEquals(new PlayerId(uuid), new PlayerId(uuid));
    }

    @Test
    void shouldGenerateDifferentIds_whenGeneratedTwice() {
        assertNotEquals(PlayerId.generate(), PlayerId.generate());
    }

    @Test
    void shouldThrowNullPointerException_whenValueIsNull() {
        assertThrows(NullPointerException.class, () -> new PlayerId(null));
    }
}
