package com.example.fantasygame.api.module.hero.dsl;

import com.example.fantasygame.api.module.hero.application.CreateHeroProfileCommand;
import com.example.fantasygame.api.module.hero.model.HeroProfile;
import com.example.fantasygame.api.module.hero.model.valueobject.Abilities;
import com.example.fantasygame.api.module.hero.model.valueobject.Ability;
import com.example.fantasygame.api.module.hero.model.valueobject.HeroProfileId;
import com.example.fantasygame.api.shared.contracts.IntegrationEvent;
import com.example.fantasygame.api.shared.contracts.hero.HeroProfileCreatedIntegrationEvent;
import com.example.fantasygame.api.module.hero.context.HeroProfileCucumberContext;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class HeroProfileDsl {

    private final HeroProfileCucumberContext ctx;

    public HeroProfileDsl(HeroProfileCucumberContext ctx) {
        this.ctx = ctx;
    }

    // --- Actions ---

    public void createsProfile(Map<String, String> input) {
        var cmd = new CreateHeroProfileCommand(
                UUID.randomUUID(),
                input.get("name"),
                input.get("species"),
                input.get("heroClass"),
                chosenAbilities(input.get("abilities")));
        ctx.setLastResult(ctx.getHeroProfileService().create(cmd));
    }

    // --- Assertions ---

    public void assertNoProfileExists() {
        ctx.getRepository().assertIsEmpty();
        ctx.getBus().assertIsEmpty();
    }

    public void assertProfileIsSavedWith(Map<String, String> expected) {
        Map<String, String> actual = snapshot(lastProfile());
        expected.forEach((field, valueExpected) -> {
            assertTrue(actual.containsKey(field), "Champ inconnu : " + field);
            assertEquals(valueExpected, actual.get(field), "Champ " + field + " : not equal to " + valueExpected);
        });
    }

    public void assertProfileCreatedEventIsPublishedWithProfileId(String expectedType) {
        var event = assertEventIsPublished(expectedType, HeroProfileCreatedIntegrationEvent.class);

        assertEquals(new HeroProfileCreatedIntegrationEvent.Payload(lastProfileId().value()),
                event.payload(), "Event payload");
    }

    private <T extends IntegrationEvent> T assertEventIsPublished(String expectedType, Class<T> type) {
        var event = ctx.getBus()
                .assertEventCount(1)
                .getLastEvent()
                .orElseThrow(() -> new AssertionError("No last event"));

        assertEquals(expectedType, event.getEventType(), "Event type");
        return assertInstanceOf(type, event, "Unexpected event class");
    }

    // --- Accès ---

    public HeroProfileId lastProfileId() {
        return ctx.getLastResultAs(HeroProfileId.class)
                .orElseThrow(() -> new AssertionError("The result profileId is not set in the test context."));
    }

    private HeroProfile lastProfile() {
        return ctx.getRepository()
                .assertCount(1)
                .findById(lastProfileId())
                .orElseThrow(() -> new AssertionError("No profile found with id."));
    }

    // --- Texte du feature -> commande ---

    private static Map<String, Integer> chosenAbilities(String text) {   // "STRENGTH 4, DEXTERITY 9, ..."
        Map<String, Integer> abilities = new LinkedHashMap<>();
        for (String part : text.split(",\\s*")) {
            int i = part.lastIndexOf(' ');
            abilities.put(part.substring(0, i), Integer.parseInt(part.substring(i + 1)));
        }
        return abilities;
    }

    // --- Domaine -> texte du feature ---

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
                .sorted(Comparator.comparing(Ability::type))        // un Set n'a pas d'ordre : on trie par enum
                .map(a -> a.type().name() + " " + a.score())
                .collect(Collectors.joining(", "));
    }
}