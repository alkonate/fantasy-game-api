package com.example.fantasygame.api.module.lobby.application;

import com.example.fantasygame.api.module.hero.model.valueobject.PlayerId;

public interface PlayerGateway {
    boolean exists(PlayerId playerId);
}