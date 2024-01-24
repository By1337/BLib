package org.by1337.blib.property.property;

import org.by1337.blib.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Long values.
 */
public class PropertyLong extends Property<Long> {

    /**
     * Constructor to create a PropertyLong instance with an initial Long value.
     *
     * @param value The initial Long value to set for the property.
     */
    public PropertyLong(@Nullable Long value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a Long value.
     *
     * @param str The string to parse.
     * @return The parsed Long value.
     * @throws NumberFormatException if the string cannot be parsed into a valid Long value.
     */
    @Override
    public Long parse(@NotNull String str) {
        return Long.parseLong(str);
    }

    /**
     * Get the property type for this instance, which is Long.
     *
     * @return The PropertyType representing the Long type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.LONG;
    }
}
