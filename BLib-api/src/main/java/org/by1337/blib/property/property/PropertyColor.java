package org.by1337.blib.property.property;

import org.bukkit.Color;
import org.by1337.blib.chat.ChatColor;
import org.by1337.blib.property.PropertyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A specific implementation of the Property class for storing Color values.
 */
public class PropertyColor extends Property<Color> {

    /**
     * Constructor to create a PropertyColor instance with an initial Color value.
     *
     * @param value The initial Color value to set for the property.
     */
    public PropertyColor(@Nullable Color value) {
        super(value);
    }

    /**
     * Parse a string and convert it into a Color value.
     *
     * @param str The string to parse. If the string starts with "#", it's assumed to be in hex format.
     *            Otherwise, it's expected to be in the format "R G B".
     * @return The parsed Color value.
     * @throws IllegalArgumentException if the string cannot be parsed or doesn't have the expected format.
     */
    @Override
    public Color parse(@NotNull String str) {
        if (str.startsWith("#")) return Color.fromRGB(ChatColor.fromHex(str).getColor().getRGB());

        String[] args = str.split(" ");

        if (args.length < 3) {
            throw new IllegalArgumentException("Insufficient arguments to create a color. Expected 3 arguments, received " + args.length);
        }
        return Color.fromRGB(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2])
        );
    }


    /**
     * Get the property type for this instance, which is Color.
     *
     * @return The PropertyType representing the Color type.
     */
    @Override
    public PropertyType<?> getType() {
        return PropertyType.COLOR;
    }
}
