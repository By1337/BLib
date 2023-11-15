package org.by1337.api.property.property;

import org.by1337.api.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing String values.
 */
public class PropertyString extends Property<String> {

    /**
     * Constructor to create a PropertyString instance with an initial String value.
     *
     * @param value The initial String value to set for the property.
     */
    public PropertyString(@Nullable String value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a String value.
     *
     * @param str The string to parse.
     * @return The parsed String value.
     */
    @Override
    public String parse(@NotNull String str) {
        return str;
    }

    /**
     * Get the property type for this instance, which is String.
     *
     * @return The PropertyType representing the String type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.STRING;
    }
}
