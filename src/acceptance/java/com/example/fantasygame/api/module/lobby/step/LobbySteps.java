package com.example.fantasygame.api.module.lobby.step;

import com.example.fantasygame.api.module.lobby.dsl.LobbyDsl;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LobbySteps {

    private final LobbyDsl dsl;

    public LobbySteps(LobbyDsl dsl) {
        this.dsl = dsl;
    }

    // --- Given ---

    @Given("Je suis enregistré comme joueur")
    public void jeSuisEnregistreCommeJoueur() {
        dsl.registerDefaultPlayer();
    }

    @Given("Je ne suis pas enregistré comme joueur")
    public void jeNeSuisPasEnregistreCommeJoueur() {
        dsl.unregisterDefaultPlayer();
    }

    @And("Je suis déjà dans un lobby")
    public void jeSuisDejaDansUnLobby() {
        dsl.givenPlayerIsAlreadyInALobby();
    }

    // --- When ---

    @When("Je crée un lobby")
    public void jeCreeUnLobby() {
        dsl.createLobby();
    }

    // --- Then ---

    @Then("Le lobby est créé avec:")
    public void leLobbyEstCree(DataTable table) {
        dsl.assertLobbyIsCreatedWith(table.asMap(String.class, String.class));
    }

    @Then("Le lobby n'est pas créé")
    public void leLobbyNEstPasCree() {
        dsl.assertLobbyIsNotCreated();
    }

    @And("Une erreur de joueur introuvable est levée")
    public void uneErreurDeJoueurIntrouvableEstLevee() {
        dsl.assertPlayerNotFoundErrorIsRaised();
    }

    @And("Une erreur de joueur déjà dans un lobby est levée")
    public void uneErreurDeJoueurDejaDansUnLobbyEstLevee() {
        dsl.assertPlayerAlreadyInLobbyErrorIsRaised();
    }
}