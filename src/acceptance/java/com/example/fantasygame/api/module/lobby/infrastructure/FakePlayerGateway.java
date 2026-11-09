package com.example.fantasygame.api.module.lobby.infrastructure;


import com.example.fantasygame.api.module.hero.model.valueobject.PlayerId;
import com.example.fantasygame.api.module.lobby.application.PlayerGateway;

public class FakePlayerGateway implements PlayerGateway {
    @Override
    public boolean exists(PlayerId playerId) {
        return false;
    }
}
