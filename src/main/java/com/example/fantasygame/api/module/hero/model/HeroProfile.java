package com.example.fantasygame.api.module.hero.model;

import com.example.fantasygame.api.module.hero.model.valueobject.*;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Objects;

@Accessors(fluent = true)
@Getter
public final class HeroProfile {

    private final HeroProfileId id;
    private final PlayerId playerId;
    private final Name name;
    private final Species species;
    private final HeroClass heroClass;
    private final Abilities abilities;      // final values: chosen + species bonus, max 20
    private final Experience experience;
    private final MaxHitPoints maxHitPoints;

    private HeroProfile(HeroProfileId id, PlayerId playerId, Name name, Species species,
                        HeroClass heroClass, Abilities abilities, Experience experience,
                        MaxHitPoints maxHp) {
        this.id = id;
        this.playerId = playerId;
        this.name = name;
        this.species = species;
        this.heroClass = heroClass;
        this.abilities = abilities;
        this.experience = experience;
        this.maxHitPoints = maxHp;
    }

    /** New profile: level 1, 0 XP. */
    public static HeroProfile create(PlayerId playerId, Name name, Species species,
                                     HeroClass heroClass, Abilities chosenAbilities) {
        Objects.requireNonNull(playerId, "playerId is required");
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(species, "species is required");
        Objects.requireNonNull(heroClass, "heroClass is required");
        Objects.requireNonNull(chosenAbilities, "chosenAbilities is required");

        Abilities abilities = chosenAbilities.withBonuses(species.modifiers());   // bonuses, cap at 20

        MaxHitPoints maxHp = MaxHitPoints.atLevelOne(
                heroClass.baseHp(), abilities.modifier(AbilityType.CONSTITUTION));

        return new HeroProfile(HeroProfileId.generate(), playerId, name, species, heroClass,
                abilities, Experience.none(), maxHp);
    }

    // Derived from the class: never stored, so never inconsistent
    public int maxMagic()                 { return heroClass.maxMagic(); }
    public AbilityType dominantAbility()  { return heroClass.dominantAbility(); }
    public WeaponType startingWeapon()    { return heroClass.startingWeapon(); }
    public ArmorType startingArmor()      { return heroClass.startingArmor(); }

    @Override public boolean equals(Object o) {
        return o instanceof HeroProfile other && id.equals(other.id);
    }
    @Override public int hashCode() { return id.hashCode(); }
}