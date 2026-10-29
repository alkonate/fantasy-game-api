package com.example.fantasygame.api.module.hero.application;

import java.util.UUID;

public interface CreateHeroUseCase {
    UUID create(CreateHeroCommand cmd);
}
