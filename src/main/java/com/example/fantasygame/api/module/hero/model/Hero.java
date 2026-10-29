package com.example.fantasygame.api.module.hero.model;

import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class Hero {

    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 30;
    private static final Set<String> SUPPORTED_SPECIES = Set.of("HUMAN", "ELF", "DWARF", "ORC");
    private static final Set<String> SUPPORTED_CLASSES = Set.of("WARRIOR", "RANGER", "MAGE", "CLERIC");
    private static final Pattern NAME_PATTERN = Pattern.compile("^\\p{L}+( \\p{L}+)*$");

    private final UUID id;
    private final UUID playerId;
    private final String name;
    private final String species;
    private final String heroClass;

    public Hero(UUID id, UUID playerId, String name, String species, String heroClass) {
        validateName(name);
        validateSpecies(species);
        validateClass(heroClass);

        this.id = id;
        this.playerId = playerId;
        this.name = name;
        this.species = species;
        this.heroClass = heroClass;
    }

    private static void validateName(String name) {
        if (name == null || name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new InvalidHeroNameException("Hero name must contain between " + NAME_MIN_LENGTH + " and " + NAME_MAX_LENGTH + " characters.");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidHeroNameException(
                    "Hero name must contain only letters and spaces, and must not start or end with a space.");
        }
    }

    private static void validateClass(String heroClass) {
        if (heroClass == null || !SUPPORTED_CLASSES.contains(heroClass)) {
            throw new InvalidHeroClassException(
                    "Hero class must be one of: " + String.join(", ", SUPPORTED_CLASSES));
        }
    }

    private static void validateSpecies(String species) {
        if (species == null || !SUPPORTED_SPECIES.contains(species)) {
            throw new InvalidHeroSpeciesException(
                    "Hero species must be one of: " + String.join(", ", SUPPORTED_SPECIES));
        }
    }

    public UUID getId() { return id; }
    public UUID getPlayerId() { return playerId; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getHeroClass() { return heroClass; }
}