package com.example.fantasygame.api.module.hero.dsl;


import com.example.fantasygame.api.module.hero.application.CreateHeroProfileCommand;
import com.example.fantasygame.api.module.hero.context.HeroProfileCucumberContext;
import com.example.fantasygame.api.module.hero.model.Exception.HeroNameAlreadyExistsException;
import com.example.fantasygame.api.module.hero.model.Exception.InvalidHeroSpeciesException;
import com.example.fantasygame.api.module.hero.model.Exception.PlayerNotFoundException;
import com.example.fantasygame.api.module.hero.model.HeroProfile;
import com.example.fantasygame.api.module.hero.model.valueobject.*;
import com.example.fantasygame.api.shared.contracts.IntegrationEvent;
import com.example.fantasygame.api.shared.contracts.hero.HeroProfileCreatedIntegrationEvent;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeroProfileDsl {

    // Valid values that don't matter to the scenarios using them (hidden noise)
    private static final String DEFAULT_ABILITIES =
            "STRENGTH 10, DEXTERITY 10, CONSTITUTION 10, INTELLIGENCE 10, WISDOM 10, CHARISMA 10";

    private static final Map<String, String> DEFAULT_INPUT = Map.of(
            "name", "Yal",
            "species", "HUMAN",
            "heroClass", "WARRIOR",
            "abilities", DEFAULT_ABILITIES);

    private final HeroProfileCucumberContext ctx;
    private final PlayerId currentPlayer = PlayerId.generate();
    private int heroCountBeforeCreation;

    public HeroProfileDsl(HeroProfileCucumberContext ctx) {
        this.ctx = ctx;
    }

    // --- Actions ---

    public void registerDefaultPlayer(){
        ctx.getPlayerGateway().register(currentPlayer);
    }

    public void createHeroProfile(Map<String, String> input) {
        heroCountBeforeCreation = ctx.getRepository().count();
        var cmd = new CreateHeroProfileCommand(
                currentPlayer.value(),
                input.get("name"),
                input.get("species"),
                input.get("heroClass"),
                chosenAbilities(input.get("abilities")));
        try {
            ctx.setLastResult(ctx.getHeroProfileService().create(cmd));
        } catch (Exception e) {
            ctx.setCaughtException(e);
        }
    }

    public void attemptsToCreateProfileNamed(String name) {
        Map<String, String> input = new LinkedHashMap<>(DEFAULT_INPUT);
        input.put("name", name);
        createHeroProfile(input);
    }

    public void attemptsToCreateProfileOfSpecies(String species) {
        Map<String, String> input = new LinkedHashMap<>(DEFAULT_INPUT);
        input.put("species", species);
        createHeroProfile(input);
    }

    // --- Givens ---

    public void unregisterDefaultPlayer() {
        ctx.getPlayerGateway().unregister(currentPlayer);
        ctx.getPlayerGateway().assertNoPlayerRegistered();
    }

    /** Goes through the repository, not the use case: no event is published. */
    public void saveHeroProfileWithName(String name) {
        ctx.getRepository().save(HeroProfile.create(
               currentPlayer,
                new Name(name),
                Species.from(DEFAULT_INPUT.get("species")),
                HeroClass.from(DEFAULT_INPUT.get("heroClass")),
                Abilities.chosenFromNames(chosenAbilities(DEFAULT_ABILITIES))));
    }

    // --- Assertions ---

    public void noHeroProfileExists() {
        ctx.getRepository().clear();
        ctx.getRepository().assertIsEmpty();
        ctx.getBus().assertIsEmpty();
    }

    public void assertProfileIsSavedWith(Map<String, String> expected) {
        Map<String, String> actual = snapshot(lastProfile());
        expected.forEach((field, value) -> {
            assertTrue(actual.containsKey(field), "Unknown field: " + field);
            assertEquals(value, actual.get(field), field);
        });
    }

    public void assertProfileIsNotCreated() {
        assertEquals(heroCountBeforeCreation, ctx.getRepository().count(), "Number of profiles");
        assertTrue(ctx.getLastResultAs(HeroProfileId.class).isEmpty(),
                "A profile id was returned although the creation should have been refused.");
    }

    public void assertProfileCreatedEventIsPublishedWithProfileId(String expectedType) {
        var event = assertEventIsPublished(expectedType, HeroProfileCreatedIntegrationEvent.class);

        assertEquals(new HeroProfileCreatedIntegrationEvent.Payload(lastProfileId().value()),
                event.payload(), "Event payload");
    }

    public void assertNameUniquenessErrorIsRaised() {
        assertInstanceOf(HeroNameAlreadyExistsException.class, ctx.getCaughtException(),
                "Expected a HeroNameAlreadyExistsException");
    }

    public void assertPlayerNotFoundErrorIsRaised() {
        assertInstanceOf(PlayerNotFoundException.class, ctx.getCaughtException(),
                "Expected a PlayerNotFoundException");
    }

    public void assertUnknownSpeciesErrorIsRaised() {
        assertInstanceOf(InvalidHeroSpeciesException.class, ctx.getCaughtException(),
                "Expected an InvalidHeroSpeciesException");
    }

    public void assertNoEventIsPublished() {
        ctx.getBus().assertIsEmpty();
    }

    // --- Access ---

    public HeroProfileId lastProfileId() {
        return ctx.getLastResultAs(HeroProfileId.class)
                .orElseThrow(() -> new AssertionError(
                        "No profile id in the context. Caught exception: " + ctx.getCaughtException(),
                        ctx.getCaughtException()));
    }

    private HeroProfile lastProfile() {
        HeroProfileId id = lastProfileId();                    // fails with the real cause first
        return ctx.getRepository().assertCount(1).findById(id)
                .orElseThrow(() -> new AssertionError("No hero profile saved."));
    }

    // --- Events ---

    private <T extends IntegrationEvent> T assertEventIsPublished(String expectedType, Class<T> type) {
        var event = ctx.getBus()
                .assertEventCount(1)
                .getLastEvent()
                .orElseThrow(() -> new AssertionError("No last event"));

        assertEquals(expectedType, event.getEventType(), "Event type");
        return assertInstanceOf(type, event, "Unexpected event class");
    }

    // --- Feature text -> command ---

    private static Map<String, Integer> chosenAbilities(String text) {   // "STRENGTH 4, DEXTERITY 9, ..."
        Map<String, Integer> abilities = new LinkedHashMap<>();
        for (String part : text.split(",\\s*")) {
            int i = part.lastIndexOf(' ');
            abilities.put(part.substring(0, i), Integer.parseInt(part.substring(i + 1)));
        }
        return abilities;
    }

    // --- Domain -> feature text ---

    private static Map<String, String> snapshot(HeroProfile p) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("name", p.name().value());
        m.put("species", p.species().name());
        m.put("heroClass", p.heroClass().name());
        m.put("abilities", describe(p.abilities()));
        m.put("level", String.valueOf(p.experience().level()));
        m.put("xp", String.valueOf(p.experience().points()));
        m.put("maxHp", String.valueOf(p.maxHitPoints().value()));
        m.put("maxMagic", String.valueOf(p.maxMagic()));
        m.put("dominantAbility", p.dominantAbility().name());
        m.put("startingWeapon", p.startingWeapon().name());
        m.put("startingArmor", p.startingArmor().name());
        return m;
    }

    private static String describe(Abilities abilities) {          // "STRENGTH 5, DEXTERITY 10, ..."
        return abilities.values().stream()
                .sorted(Comparator.comparing(Ability::type))        // a Set has no order: sort by enum order
                .map(a -> a.type().name() + " " + a.score())
                .collect(Collectors.joining(", "));
    }
}