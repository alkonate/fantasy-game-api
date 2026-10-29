package org.example.fantasygameapi.module.hero.acceptance;

import com.example.fantasygame.api.shared.contracts.hero.HeroCreatedEvent;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class HeroEventSteps {
    final private  HeroCucumberContext ctx;
    public HeroEventSteps(HeroCucumberContext ctx){
        this.ctx = ctx;
    }

    @Then("l'événement {string} est publié avec l'identité du héros, l'espèce {string} et la classe {string}")
    public void checkEventPublished(String expectedEventType, String expectedSpecies, String expectedClass) {

        var expectedHeroId = ctx.getLastResultAs(UUID.class)
                .orElseThrow(() -> new AssertionError("The result heroId is not set in the test context."));

        var event = ctx.getBus()
                .assertEventCount(1)
                .assertLastEventOfType(HeroCreatedEvent.class);

        assertEquals(expectedEventType, event.getEventType(),
                "The event type is not the same as the expected type.");

        assertEquals(new HeroCreatedEvent.Payload(expectedHeroId,expectedSpecies,expectedClass),
                event.payload(),
                "The event payload is not the same as the expected type.");
    }

    @And("Aucun évènement n'est publié")
    public void noEventPublished(){
        ctx.getBus().assertIsEmpty();
    }
}
