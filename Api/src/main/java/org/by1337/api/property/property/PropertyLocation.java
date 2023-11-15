package org.by1337.api.property.property;

import org.by1337.api.property.PropertyType;
import org.by1337.api.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing location values.
 */
public class PropertyLocation extends Property<BLocation> {

    /**
     * Constructor to create a PropertyLocation instance with an initial BLocation value.
     *
     * @param value The initial BLocation value to set for the property.
     */
    public PropertyLocation(@Nullable BLocation value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a BLocation value.
     *
     * @param str The string to parse in the format 'world x y z'.
     * @return The parsed BLocation value.
     * @throws IllegalArgumentException if the string cannot be parsed or doesn't have the expected format.
     */
    @Override
    public BLocation parse(@NotNull String str) {
        String[] args = str.split(" ");
        if (args.length < 4) throw new IllegalArgumentException("Expected 'world <x> <y> <z>'");
        return new BLocation(
                parseDouble(args[1]),
                parseDouble(args[2]),
                parseDouble(args[3]),
                args[0]
        );
    }

    /**
     * Parse a string and convert it into a double value.
     *
     * @param src The string to parse as a double.
     * @return The parsed double value.
     * @throws NumberFormatException if the string cannot be parsed into a valid double value.
     */
    private double parseDouble(String src) {
        double val = Double.parseDouble(src);
        if (Double.isNaN(val)) throw new NumberFormatException("Nan '" + src + "'");
        return val;
    }

    /**
     * Get the property type for this instance, which is Location.
     *
     * @return The PropertyType representing the Location type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.LOCATION;
    }
}
