package org.example.fantasygameapi.module.hero.acceptance;

import com.example.fantasygame.api.module.hero.application.CreateHeroCommand;
import com.example.fantasygame.api.module.hero.model.Hero;
import com.example.fantasygame.api.module.hero.model.HeroNameAlreadyExistsException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.UUID;

public class HeroSteps {

    private  final HeroCucumberContext ctx;

    public HeroSteps(HeroCucumberContext ctx) {
        this.ctx = ctx;
    }


    @Given("Aucun héros n'existe encore")
    public void ZeroHero() {
        ctx.getRepository().clear();
        ctx.getBus().clear();
        ctx.getRepository().assertIsEmpty();
        ctx.getBus().assertIsEmpty();
    }

    @When("Je crée un héros de nom {string}, espèce {string} et de classe {string}")
    public void createNewHero(String name, String species, String clazz) {
        createHero(name, species, clazz);
    }

    @When("Je crée un héros de nom {string}")
    public void createNewHero(String name) {
        createHero(name, "HUMAN", "WARRIOR");
    }

    @Then("l'identité du héros est enregistrée avec le nom {string}, l'espèce {string} et la classe {string}")
    public void checkIdentityHeroRecorded(String name, String species, String clazz) {

        var heroId = ctx.getLastResultAs(UUID.class)
                .orElseThrow(() -> new AssertionError("The result heroId is not set in the test context."));

        ctx.getRepository()
                .assertCount(1)
                .assertHeroIdentity(heroId,name,species,clazz);
    }


    @Given("J'ai créé un héros de nom {string}")
    public void HeroCreated(String name) {
        ctx.getRepository().save(new Hero(UUID.randomUUID(),
                UUID.randomUUID()
                ,name,"HUMAN",
                "WARRIOR")
        );
        ctx.getRepository().assertCount(1);
    }


    @Then("Le nouveau héros n'est pas créé")
    public void HeroNotCreated() {
        ctx.getRepository()
                .assertCount(1);
        ctx.getBus()
                .assertIsEmpty();
    }

    @And("Une erreur d'unicité est levée")
    public void ExceptionThrown() {
      assertInstanceOf(HeroNameAlreadyExistsException.class,
              ctx.getCaughtException(),"Exception thrown should be of type HeroCreatedIntegrationEvent");
    }

    private void createHero(String name, String species, String clazz){
        String joueurId = UUID.randomUUID().toString();
        CreateHeroCommand cmd = new CreateHeroCommand(joueurId,name,species,clazz);
        try {
            var result = ctx.getHeroService().create(cmd);
            ctx.setLastResult(result);
        }catch (Exception e){
            ctx.setCaughtException(e);
        }
    }
}

