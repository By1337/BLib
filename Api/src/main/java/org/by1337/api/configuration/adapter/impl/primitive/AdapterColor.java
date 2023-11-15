package org.by1337.api.configuration.adapter.impl.primitive;

import org.bukkit.Color;
import org.by1337.api.chat.ChatColor;
import org.by1337.api.configuration.adapter.PrimitiveAdapter;

/**
 * A primitive adapter for serializing and deserializing Color objects.
 */
public class AdapterColor implements PrimitiveAdapter<Color> {

    /**
     * Serialize a Color object to its hexadecimal representation.
     *
     * @param obj The Color object to be serialized.
     * @return The hexadecimal representation of the Color.
     */
    @Override
    public Object serialize(Color obj) {
        return ChatColor.toHex(obj);
    }

    /**
     * Deserialize a Color object from its hexadecimal representation.
     *
     * @param src The hexadecimal representation of the Color to be deserialized.
     * @return The deserialized Color object.
     */
    @Override
    public Color deserialize(Object src) {
        return ChatColor.fromHex(String.valueOf(src)).toBukkitColor();
    }
}
