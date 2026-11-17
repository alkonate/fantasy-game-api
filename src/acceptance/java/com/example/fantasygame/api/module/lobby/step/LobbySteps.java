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
        dsl.savePlayerInLobby();
    }

    @And("Un lobby créé par {string} existe")
    public void unLobbyCreeParExiste(String hostName) {
        dsl.saveLobbyCreatedBy(hostName);
    }

    @And("Un lobby existe")
    public void unLobbyExiste() {
        dsl.saveLobby();
    }

    @And("Un lobby existe avec les joueurs {string}")
    public void unLobbyExisteAvecLesJoueurs(String names) {
        dsl.saveLobbyWithPlayers(names);
    }

    @And("Un lobby verrouillé existe")
    public void unLobbyVerrouilleExiste() {
        dsl.saveLockedLobby();
    }

    @And("Un lobby fermé existe")
    public void unLobbyFermeExiste() {
        dsl.saveClosedLobby();
    }

    // --- When ---

    @When("Je crée un lobby")
    public void jeCreeUnLobby() {
        dsl.createLobby();
    }

    @When("Je rejoins le lobby")
    public void jeRejoinsLeLobby() {
        dsl.joinLobby();
    }

    @When("Je rejoins un lobby qui n'existe pas")
    public void jeRejoinsUnLobbyQuiNExistePas() {
        dsl.joinUnknownLobby();
    }

    // --- Then ---

    @Then("Le lobby est créé avec:")
    public void leLobbyEstCree(DataTable table) {
        dsl.assertLobbyIsCreatedWith(table.asMap(String.class, String.class));
    }

    @Then("Le lobby contient:")
    public void leLobbyContient(DataTable table) {
        dsl.assertLobbyContains(table.asMap(String.class, String.class));
    }

    @Then("Le lobby n'est pas modifié")
    public void leLobbyNEstPasModifie() {
        dsl.assertLobbyIsNotModified();
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

    @And("Une erreur de lobby plein est levée")
    public void uneErreurDeLobbyPleinEstLevee() {
        dsl.assertLobbyFullErrorIsRaised();
    }

    @And("Une erreur de lobby verrouillé est levée")
    public void uneErreurDeLobbyVerrouilleEstLevee() {
        dsl.assertLobbyLockedErrorIsRaised();
    }

    @And("Une erreur de lobby fermé est levée")
    public void uneErreurDeLobbyFermeEstLevee() {
        dsl.assertLobbyClosedErrorIsRaised();
    }

    @And("Une erreur de lobby introuvable est levée")
    public void uneErreurDeLobbyIntrouvableEstLevee() {
        dsl.assertLobbyNotFoundErrorIsRaised();
    }
}
