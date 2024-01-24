package org.by1337.blib.chat.util;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public class InvalidCharacters {
    private static final Pattern pattern = Pattern.compile("^[a-zA-Z0-9-_]+$");

    public static String getInvalidCharacters(String input) {
        StringBuilder invalidCharacters = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (!pattern.matcher(String.valueOf(c)).matches()) {
                invalidCharacters.append(c);
            }
        }
        return invalidCharacters.toString();
    }

    public static void validate(String input, String message) {
        validate(input, () -> message);
    }

    public static void validate(String input) {
        validate(input, () -> String.format("Invalid name. Must be [a-zA-Z0-9._-]: '%s'", input));
    }

    public static void validate(String input, Supplier<String> message) {
        if (!pattern.matcher(input).matches()) {
            throw new IllegalArgumentException(message.get());
        }
    }
}
