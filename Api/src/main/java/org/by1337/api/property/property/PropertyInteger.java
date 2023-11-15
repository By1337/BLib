package org.by1337.api.property.property;

import org.by1337.api.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Integer values.
 */
public class PropertyInteger extends Property<Integer> {

    /**
     * Constructor to create a PropertyInteger instance with an initial Integer value.
     *
     * @param value The initial Integer value to set for the property.
     */
    public PropertyInteger(@Nullable Integer value) {
        super(value);
    }

    /**
     * Parse a string and convert it into an Integer value.
     *
     * @param str The string to parse.
     * @return The parsed Integer value.
     * @throws NumberFormatException if the string cannot be parsed into a valid Integer value.
     */
    @Override
    public Integer parse(@NotNull String str) {
        return Integer.parseInt(str);
    }

    /**
     * Get the property type for this instance, which is Integer.
     *
     * @return The PropertyType representing the Integer type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.INTEGER;
    }
}
