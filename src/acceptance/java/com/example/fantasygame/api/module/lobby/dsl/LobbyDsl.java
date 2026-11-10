package com.example.fantasygame.api.module.lobby.dsl;

import com.example.fantasygame.api.module.lobby.application.CreateLobbyCommand;
import com.example.fantasygame.api.module.lobby.application.JoinLobbyCommand;
import com.example.fantasygame.api.module.lobby.context.LobbyCucumberContext;
import com.example.fantasygame.api.module.lobby.model.exception.PlayerAlreadyInLobbyException;
import com.example.fantasygame.api.module.lobby.model.exception.PlayerNotFoundException;
import com.example.fantasygame.api.module.lobby.model.Lobby;
import com.example.fantasygame.api.module.lobby.model.valueobject.LobbyId;
import com.example.fantasygame.api.module.lobby.model.valueobject.PlayerId;
import com.example.fantasygame.api.shared.contracts.IntegrationEvent;
import com.example.fantasygame.api.shared.contracts.lobby.LobbyCreatedIntegrationEvent;
import com.example.fantasygame.api.shared.contracts.lobby.PlayerJoinedLobbyIntegrationEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class LobbyDsl {

    private static final String ME = "moi";

    private final LobbyCucumberContext ctx;
    private final PlayerId currentPlayer = PlayerId.generate();
    private final Map<String, PlayerId> namedPlayers = new LinkedHashMap<>();
    private int lobbyCountBeforeCreation;
    private LobbyId lobbyUnderTest;     // the lobby the scenario is about (created or given)

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
    public void savePlayerInLobby() {
        ctx.getRepository().save(Lobby.create(currentPlayer));
    }

    /** Goes through the repository, not the use case: no event is published. */
    public void saveLobbyCreatedBy(String hostName) {
        var lobby = ctx.getRepository().save(Lobby.create(namedPlayer(hostName)));
        lobbyUnderTest = lobby.id();
    }

    // --- Actions ---

    public void createLobby() {
        lobbyCountBeforeCreation = ctx.getRepository().count();
        var cmd = new CreateLobbyCommand(currentPlayer.value());
        try {
            var lobbyId = ctx.getLobbyService().create(cmd);
            ctx.setLastResult(lobbyId);
            lobbyUnderTest = lobbyId;
        } catch (Exception e) {
            ctx.setCaughtException(e);
        }
    }

    public void joinLobby() {
        var cmd = new JoinLobbyCommand(lobbyUnderTest.value(), currentPlayer.value());
        try {
            ctx.getLobbyService().join(cmd);
        } catch (Exception e) {
            ctx.setCaughtException(e);
        }
    }

    // --- Assertions ---

    public void assertLobbyIsCreatedWith(Map<String, String> expected) {
        assertSnapshotMatches(expected, snapshot(lastLobby()));
    }

    private static void assertSnapshotMatches(Map<String, String> expected, Map<String, String> actual) {
        expected.forEach((field, value) -> {
            assertTrue(actual.containsKey(field), "Unknown field: " + field);
            assertEquals(value, actual.get(field), field);
        });
    }

    public void assertLobbyContains(Map<String, String> expected) {
        assertEquals(null, ctx.getCaughtException(), "Unexpected exception");
        var lobby = ctx.getRepository().findById(lobbyUnderTest)
                .orElseThrow(() -> new AssertionError("No lobby saved with id " + lobbyUnderTest));
        assertSnapshotMatches(expected, snapshot(lobby));
    }

    public void assertLobbyIsNotCreated() {
        assertEquals(lobbyCountBeforeCreation, ctx.getRepository().count(),
                "A lobby was created although it should have been refused. Caught exception: "
                        + ctx.getCaughtException());
        assertTrue(ctx.getLastResultAs(LobbyId.class).isEmpty(),
                "A lobby id was returned although the creation should have been refused.");
    }

    public void assertEventIsPublishedWithLobbyId(String expectedType) {
        var event = assertEventIsPublished(expectedType);

        assertEquals(lobbyUnderTest.value(), lobbyIdOf(event), "Lobby id in the event payload");
    }

    public void assertPlayerNotFoundErrorIsRaised() {
        assertInstanceOf(PlayerNotFoundException.class, ctx.getCaughtException(),
                "Expected a PlayerNotFoundException");
    }

    public void assertPlayerAlreadyInLobbyErrorIsRaised() {
        assertInstanceOf(PlayerAlreadyInLobbyException.class, ctx.getCaughtException(),
                "Expected a PlayerAlreadyInLobbyException");
    }

    public void assertNoEventIsPublished() {
        ctx.getBus().assertIsEmpty();
    }

    // --- Access ---

    public LobbyId lastLobbyId() {
        return ctx.getLastResultAs(LobbyId.class)
                .orElseThrow(() -> new AssertionError(
                        "No lobby id in the context. Caught exception: " + ctx.getCaughtException(),
                        ctx.getCaughtException()));
    }

    private Lobby lastLobby() {
        LobbyId id = lastLobbyId();                    // fails with the real cause first
        return ctx.getRepository().assertCount(1).findById(id)
                .orElseThrow(() -> new AssertionError("No lobby saved."));
    }

    // --- Events ---

    private IntegrationEvent assertEventIsPublished(String expectedType) {
        var event = ctx.getBus()
                .assertEventCount(1)
                .getLastEvent()
                .orElseThrow(() -> new AssertionError("No last event"));

        assertEquals(expectedType, event.getEventType(), "Event type");
        return event;
    }

    private static UUID lobbyIdOf(IntegrationEvent event) {
        return switch (event) {
            case LobbyCreatedIntegrationEvent e -> e.payload().lobbyId();
            case PlayerJoinedLobbyIntegrationEvent e -> e.payload().lobbyId();
            default -> fail("Not a lobby event: " + event.getClass().getSimpleName());
        };
    }

    // --- Domain -> feature text ---

    private Map<String, String> snapshot(Lobby lobby) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("host", nameOf(lobby.host()));
        m.put("players", lobby.players().stream().map(this::nameOf).collect(Collectors.joining(", ")));
        return m;
    }

    /** "moi" is the current player; any other name gets its own player id. */
    private PlayerId namedPlayer(String name) {
        return namedPlayers.computeIfAbsent(name, n -> PlayerId.generate());
    }

    private String nameOf(PlayerId player) {                    // "moi", or the raw id if unknown
        return namedPlayers.entrySet().stream()
                .filter(e -> e.getValue().equals(player))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(player.value().toString());
    }
}
