package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidAbilityException;

import java.util.*;
import java.util.stream.Collectors;

public record Abilities(Set<Ability> values) {

    public Abilities {
        Objects.requireNonNull(values, "values is required");
        Set<AbilityType> types = EnumSet.noneOf(AbilityType.class);
        values.forEach(a -> types.add(a.type()));
        int expected = AbilityType.values().length;
        if (values.size() != expected || types.size() != expected) {
            throw new InvalidAbilityException("Exactly one score for each of the six abilities is required.");
        }
        values = Set.copyOf(values);
    }

    /** Domain entry point: scores keyed by ability type. */
    public static Abilities chosen(Map<AbilityType, Integer> chosen) {
        Objects.requireNonNull(chosen, "chosen is required");
        return new Abilities(chosen.entrySet().stream()
                .map(e -> Ability.chosen(e.getKey(), scoreOf(e.getValue())))
                .collect(Collectors.toSet()));
    }

    private static int scoreOf(Integer value) {
        return Objects.requireNonNull(value, "value is required");
    }

    /** DTO entry point: scores keyed by ability name, as received in the command. */
    public static Abilities chosenFromNames(Map<String, Integer> chosen) {
        Objects.requireNonNull(chosen, "chosen is required");
        Map<AbilityType, Integer> typed = new EnumMap<>(AbilityType.class);
        chosen.forEach((name, score) -> typed.put(AbilityType.from(name), score));
        return chosen(typed);
    }


    public Abilities withBonuses(Map<AbilityType, Integer> bonuses) {
        return new Abilities(values.stream()
                .map(a -> a.withBonus(bonuses.getOrDefault(a.type(), 0)))
                .collect(Collectors.toSet()));
    }

    public Ability get(AbilityType type) {
        return values.stream().filter(a -> a.type() == type).findFirst().orElseThrow();
    }

    public int modifier(AbilityType type) {
        return get(type).modifier();
    }
}
