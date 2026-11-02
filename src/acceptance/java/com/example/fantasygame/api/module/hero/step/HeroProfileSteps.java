package com.example.fantasygame.api.module.hero.step;

import com.example.fantasygame.api.module.hero.dsl.HeroProfileDsl;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HeroProfileSteps {

    private  final HeroProfileDsl dsl;

    public HeroProfileSteps(HeroProfileDsl dsl) {
        this.dsl = dsl;
    }


    @Given("Aucun profil de héros n'existe encore")
    public void ZeroHero() {
        dsl.assertNoProfileExists();
    }

    @When("Je crée un profil de héros avec:")
    public void createNewHero(DataTable dataTable) {
        dsl.createsProfile(dataTable.asMap());
    }

    @Then("Le profil du héros est enregistré avec:")
    public void verifyHeroProfile(DataTable dataTable) {
        dsl.assertProfileIsSavedWith(dataTable.asMap());
    }
}

