package com.example.fantasygame.api.module.hero.model.valueobject;

import com.example.fantasygame.api.module.hero.model.Exception.InvalidHeroNameException;

import java.util.regex.Pattern;

public record Name(String value) {

    public static final int MIN_LENGTH = 2;
    public static final int MAX_LENGTH = 30;

    // Letters (accents included), words separated by a single space:
    // rules out leading/trailing space, digits, punctuation and tabs.
    private static final Pattern FORMAT = Pattern.compile("^\\p{L}+( \\p{L}+)*$");

    public Name {
        if (value == null || value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new InvalidHeroNameException(
                    "Name must contain between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters.");
        }
        if (!FORMAT.matcher(value).matches()) {
            throw new InvalidHeroNameException(
                    "Name must contain only letters and spaces, and must not start or end with a space.");
        }
    }
}
