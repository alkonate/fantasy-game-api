package com.example.fantasygame.api.module.lobby.application;

import java.util.UUID;

public record JoinLobbyCommand(UUID lobbyId, UUID playerId) {}
