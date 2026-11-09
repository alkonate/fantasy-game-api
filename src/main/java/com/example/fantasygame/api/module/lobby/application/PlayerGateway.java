package com.example.fantasygame.api.module.lobby.application;

import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;

public interface PlayerGateway {
    boolean exists(PlayerId playerId);
}
