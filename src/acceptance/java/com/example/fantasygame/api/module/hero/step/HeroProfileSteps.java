package com.example.fantasygame.api.module.hero.step;

import com.example.fantasygame.api.module.hero.dsl.HeroProfileDsl;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class HeroProfileSteps {

    private final HeroProfileDsl dsl;

    public HeroProfileSteps(HeroProfileDsl heroes) {
        this.dsl = heroes;
    }

    // --- Given ---

    @Given("Je suis enregistré comme joueur")
    public void JeSuisRegisterCommeJoueur() {
        dsl.registerDefaultPlayer();
    }

    @Given("Aucun profil de héros n'existe encore")
    public void aucunProfilDeHeros() {
        dsl.noHeroProfileExists();
    }

    @Given("Un profil de héros de nom {string} existe déjà")
    public void unProfilDeHerosDeNomExisteDeja(String name) {
        dsl.saveHeroProfileWithName(name);
    }

    @Given("Je ne suis pas enregistré comme joueur")
    public void jeNeSuisPasEnregistreCommeJoueur() {
        dsl.unregisterDefaultPlayer();
    }

    // --- When ---

    @When("Je crée un profil de héros avec:")
    public void jeCreeUnProfil(DataTable table) {
        dsl.createHeroProfile(table.asMap(String.class, String.class));
    }

    @When("Je crée un profil de héros de nom {string}")
    public void jeCreeUnProfilDeNom(String name) {
        dsl.attemptsToCreateProfileNamed(name);
    }

    @When("Je crée un profil de héros d'espèce {string}")
    public void jeCreeUnProfilDEspece(String species) {
        dsl.attemptsToCreateProfileOfSpecies(species);
    }

    // --- Then ---

    @Then("Le profil du héros est enregistré avec:")
    public void leProfilEstEnregistre(DataTable table) {
        dsl.assertProfileIsSavedWith(table.asMap(String.class, String.class));
    }

    @Then("Le profil n'est pas créé")
    public void leProfilNEstPasCree() {
        dsl.assertProfileIsNotCreated();
    }


    @And("Une erreur d'unicité est levée")
    public void uneErreurDUniciteEstLevee() {
        dsl.assertNameUniquenessErrorIsRaised();
    }

    @And("Une erreur de joueur introuvable est levée")
    public void uneErreurDeJoueurIntrouvableEstLevee() {
        dsl.assertPlayerNotFoundErrorIsRaised();
    }

    @And("Une erreur d'espèce inconnue est levée")
    public void uneErreurDEspeceInconnueEstLevee() {
        dsl.assertUnknownSpeciesErrorIsRaised();
    }

}