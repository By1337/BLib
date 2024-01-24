package org.by1337.blib.property.property;

import org.bukkit.Particle;
import org.by1337.blib.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Particle values.
 */
public class PropertyParticle extends Property<Particle> {

    /**
     * Constructor to create a PropertyParticle instance with an initial Particle value.
     *
     * @param value The initial Particle value to set for the property.
     */
    public PropertyParticle(@Nullable Particle value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a Particle value.
     *
     * @param str The string to parse.
     * @return The parsed Particle value.
     * @throws IllegalArgumentException if the string cannot be parsed into a valid Particle value.
     */
    @Override
    public Particle parse(@NotNull String str) {
        return Particle.valueOf(str);
    }

    /**
     * Get the property type for this instance, which is Particle.
     *
     * @return The PropertyType representing the Particle type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.PARTICLE;
    }
}
