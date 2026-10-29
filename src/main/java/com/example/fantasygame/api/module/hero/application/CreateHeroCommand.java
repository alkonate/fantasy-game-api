package com.example.fantasygame.api.module.hero.application;

public record CreateHeroCommand (String playerId,String name,String species,String heroClass) {}
