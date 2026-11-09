package com.example.fantasygame.api.module.lobby.step;

import com.example.fantasygame.api.module.lobby.dsl.LobbyDsl;
import io.cucumber.java.en.And;

public class LobbyEventSteps {

    private final LobbyDsl dsl;

    public LobbyEventSteps(LobbyDsl dsl) {
        this.dsl = dsl;
    }

    @And("L'événement {string} est publié avec l'identité du lobby")
    public void evenementPublieAvecIdentiteDuLobby(String expectedEventType) {
        dsl.assertLobbyCreatedEventIsPublishedWithLobbyId(expectedEventType);
    }

    @And("Aucun événement n'est publié")
    public void aucunEvenementPublie() {
        dsl.assertNoEventIsPublished();
    }
}
