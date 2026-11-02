package com.example.fantasygame.api.module.hero.step;

import com.example.fantasygame.api.module.hero.dsl.HeroProfileDsl;
import io.cucumber.java.en.Then;

public class HeroProfileEventSteps {
    HeroProfileDsl dsl;
    public HeroProfileEventSteps(HeroProfileDsl dsl){
        this.dsl = dsl;
    }

    @Then("L'événement {string} est publié avec l'identité du profil")
    public void checkEventPublished(String expectedEventType) {
       dsl.assertProfileCreatedEventIsPublishedWithProfileId(expectedEventType);
    }
}
