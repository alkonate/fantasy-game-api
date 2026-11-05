package com.example.fantasygame.api.module.hero.step;

import com.example.fantasygame.api.module.hero.dsl.HeroProfileDsl;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class HeroProfileEventSteps {
    HeroProfileDsl dsl;
    public HeroProfileEventSteps(HeroProfileDsl dsl){
        this.dsl = dsl;
    }

    @Then("L'événement {string} est publié avec l'identité du profil")
    public void VerifieEvenementPublie(String expectedEventType) {
       dsl.assertProfileCreatedEventIsPublishedWithProfileId(expectedEventType);
    }

    @And("Aucun événement n'est publié")
    public void AffirmeAucunEventPublie() {
        dsl.assertNoEventIsPublished();
    }
}
