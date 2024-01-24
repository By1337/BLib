package org.by1337.blib.property.property;

import org.bukkit.util.Vector;
import org.by1337.blib.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Vector values.
 */
public class PropertyVector extends Property<Vector> {

    /**
     * Constructor to create a PropertyVector instance with an initial Vector value.
     *
     * @param value The initial Vector value to set for the property.
     */
    public PropertyVector(@Nullable Vector value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a Vector value.
     *
     * @param str The string to parse in the format '<x> <y> <z>'.
     * @return The parsed Vector value.
     * @throws IllegalArgumentException if the string cannot be parsed or doesn't have the expected format.
     */
    @Override
    public Vector parse(@NotNull String str) {
        String[] args = str.split(" ");
        if (args.length < 3) {
            throw new IllegalArgumentException("Expected '<x> <y> <z>'");
        }

        return new Vector(
                parseDouble(args[0]),
                parseDouble(args[1]),
                parseDouble(args[2])
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
        if (Double.isNaN(val)) {
            throw new NumberFormatException("NaN '" + src + "'");
        }
        return val;
    }

    /**
     * Get the property type for this instance, which is Vector.
     *
     * @return The PropertyType representing the Vector type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.VECTOR;
    }
}
