package com.example.fantasygame.api.module.hero.application;


import com.example.fantasygame.api.module.hero.model.valueobject.HeroProfileId;

public interface CreateHeroProfileUseCase {
    HeroProfileId create(CreateHeroProfileCommand cmd);
}
