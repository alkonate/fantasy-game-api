package com.example.fantasygame.api.module.lobby.infrastructure;

import com.example.fantasygame.api.module.lobby.application.PlayerGateway;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FakePlayerGateway implements PlayerGateway {
    private final Set<PlayerId> known = new HashSet<>();

    public void register(PlayerId id)   { known.add(id); }
    public void unregister(PlayerId id) { known.remove(id); }

    public void assertNoPlayerRegistered() {
        assertEquals(0, known.size(), "Registered player(s) was/were found");
    }

    @Override public boolean exists(PlayerId id) { return known.contains(id); }
}
