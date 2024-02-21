package org.by1337.blib.property.property;

import org.by1337.blib.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Double values.
 */
@Deprecated(forRemoval = true)
public class PropertyDouble extends Property<Double> {

    /**
     * Constructor to create a PropertyDouble instance with an initial Double value.
     *
     * @param value The initial Double value to set for the property.
     */
    public PropertyDouble(@Nullable Double value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a Double value.
     *
     * @param str The string to parse.
     * @return The parsed Double value.
     * @throws NumberFormatException if the string cannot be parsed into a valid Double value.
     */
    @Override
    public Double parse(@NotNull String str) {
        double val = Double.parseDouble(str);
        if (Double.isNaN(val)) throw new NumberFormatException("Nan '" + str + "'");
        return val;
    }


    /**
     * Get the property type for this instance, which is Double.
     *
     * @return The PropertyType representing the Double type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.DOUBLE;
    }
}
