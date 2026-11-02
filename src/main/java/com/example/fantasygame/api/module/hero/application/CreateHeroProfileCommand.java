package com.example.fantasygame.api.module.hero.application;

import java.util.Map;
import java.util.UUID;

public record CreateHeroProfileCommand(UUID playerId, String name, String species, String heroClass, Map<String, Integer> abilities) {}
