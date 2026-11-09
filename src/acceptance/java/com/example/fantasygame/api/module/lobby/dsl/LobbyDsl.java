package com.example.fantasygame.api.module.lobby.dsl;

import com.example.fantasygame.api.module.lobby.application.CreateLobbyCommand;
import com.example.fantasygame.api.module.lobby.context.LobbyCucumberContext;
import com.example.fantasygame.api.module.lobby.model.exception.PlayerNotFoundException;
import com.example.fantasygame.api.module.lobby.model.Lobby;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;
import com.example.fantasygame.api.shared.contracts.lobby.LobbyCreatedIntegrationEvent;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LobbyDsl {

    private static final String ME = "moi";

    private final LobbyCucumberContext ctx;
    private final PlayerId currentPlayer = PlayerId.generate();
    private final Map<String, PlayerId> namedPlayers = new LinkedHashMap<>();
    private int lobbyCountBeforeCreation;

    public LobbyDsl(LobbyCucumberContext ctx) {
        this.ctx = ctx;
        namedPlayers.put(ME, currentPlayer);
    }

    // --- Givens ---

    public void registerDefaultPlayer() {
        ctx.getPlayerGateway().register(currentPlayer);
    }

    public void unregisterDefaultPlayer() {
        ctx.getPlayerGateway().unregister(currentPlayer);
        ctx.getPlayerGateway().assertNoPlayerRegistered();
    }

    /** Goes through the repository, not the use case: no event is published. */
    public void givenPlayerIsAlreadyInALobby() {
        ctx.getRepository().save(Lobby.create(currentPlayer));
    }

    // --- Actions ---

    public void createLobby() {
        lobbyCountBeforeCreation = ctx.getRepository().count();
        var cmd = new CreateLobbyCommand(currentPlayer.value());
        try {
            ctx.setLastResult(ctx.getLobbyService().create(cmd));
        } catch (Exception e) {
            ctx.setCaughtException(e);
        }
    }

    // --- Assertions ---

    public void assertLobbyIsCreatedWith(Map<String, String> expected) {
        Map<String, String> actual = snapshot(lastLobby());
        expected.forEach((field, value) -> {
            assertTrue(actual.containsKey(field), "Unknown field: " + field);
            assertEquals(value, actual.get(field), field);
        });
    }

    public void assertLobbyIsNotCreated() {
        assertEquals(lobbyCountBeforeCreation, ctx.getRepository().count(),
                "A lobby was created although it should have been refused. Caught exception: "
                        + ctx.getCaughtException());
        assertTrue(ctx.getLastResultAs(LobbyId.class).isEmpty(),
                "A lobby id was returned although the creation should have been refused.");
    }

    public void assertLobbyCreatedEventIsPublishedWithLobbyId(String expectedType) {
        var event = assertEventIsPublished(expectedType, LobbyCreatedIntegrationEvent.class);

        assertEquals(new LobbyCreatedIntegrationEvent.Payload(lastLobbyId().value()),
                event.payload(), "Event payload");
    }

    public void assertPlayerNotFoundErrorIsRaised() {
        assertInstanceOf(PlayerNotFoundException.class, ctx.getCaughtException(),
                "Expected a PlayerNotFoundException");
    }

    public void assertPlayerAlreadyInLobbyErrorIsRaised() {