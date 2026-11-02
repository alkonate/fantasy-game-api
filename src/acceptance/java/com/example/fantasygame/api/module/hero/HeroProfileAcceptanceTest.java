package com.example.fantasygame.api.module.hero;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SelectClasspathResource("feature")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class HeroProfileAcceptanceTest {
    // Cette classe reste vide. Elle sert uniquement de point d'entrée pour JUnit.
}
