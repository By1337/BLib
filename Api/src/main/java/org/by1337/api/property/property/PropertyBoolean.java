package org.by1337.api.property.property;

import org.by1337.api.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Boolean values.
 */
public class PropertyBoolean extends Property<Boolean>{

    /**
     * Constructor to create a PropertyBoolean instance with an initial Boolean value.
     *
     * @param value The initial Boolean value to set for the property.
     */
    public PropertyBoolean(@Nullable Boolean value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a Boolean value.
     *
     * @param str The string to parse.
     * @return The parsed Boolean value.
     */
    @Override
    public Boolean parse(@NotNull String str) {
        return Boolean.parseBoolean(str);
    }

    /**
     * Get the property type for this instance, which is Boolean.
     *
     * @return The PropertyType representing the Boolean type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.BOOLEAN;
    }
}
