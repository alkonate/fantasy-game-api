package org.example.fantasygameapi.module.hero.acceptance;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SelectClasspathResource("features/hero")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class HeroAcceptanceTest {
    // Cette classe reste vide. Elle sert uniquement de point d'entrée pour JUnit.
}
