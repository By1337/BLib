package org.by1337.api.property.property;

import org.bukkit.Material;
import org.by1337.api.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Material values.
 */
public class PropertyMaterial extends Property<Material> {

    /**
     * Constructor to create a PropertyMaterial instance with an initial Material value.
     *
     * @param value The initial Material value to set for the property.
     */
    public PropertyMaterial(@Nullable Material value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a Material value.
     *
     * @param str The string to parse.
     * @return The parsed Material value.
     * @throws IllegalArgumentException if the string cannot be parsed into a valid Material value.
     */
    @Override
    public Material parse(@NotNull String str) {
        return Material.valueOf(str);
    }

    /**
     * Get the property type for this instance, which is Material.
     *
     * @return The PropertyType representing the Material type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.MATERIAL;
    }
}
