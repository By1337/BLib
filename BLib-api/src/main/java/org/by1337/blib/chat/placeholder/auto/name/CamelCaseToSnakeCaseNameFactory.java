package org.by1337.blib.chat.placeholder.auto.name;

import org.by1337.blib.chat.placeholder.auto.PlaceholderNameFactory;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CamelCaseToSnakeCaseNameFactory implements PlaceholderNameFactory {
    public static final CamelCaseToSnakeCaseNameFactory INSTANCE = new CamelCaseToSnakeCaseNameFactory();

    private static final Pattern PATTERN = Pattern.compile("([a-z0-9])([A-Z])");

    @Override
    public String toName(Field field) {
        String fieldName = field.getName();
        StringBuilder snakeCaseName = new StringBuilder();
        Matcher matcher = PATTERN.matcher(fieldName);
        while (matcher.find()) {
            matcher.appendReplacement(snakeCaseName, matcher.group(1) + "_" + matcher.group(2).toLowerCase());
        }
        matcher.appendTail(snakeCaseName);

        return snakeCaseName.toString().toLowerCase();
    }
}
