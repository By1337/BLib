package org.by1337.blib.configuration.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.BLib;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.adapter.ClassAdapter;

/**
 * A class adapter for serializing and deserializing ItemStack objects.
 */
public class AdapterItemStack implements ClassAdapter<ItemStack> {
    /**
     * Serialize an ItemStack object to a ConfigurationSection in the specified YamlContext.
     *
     * @param obj     The ItemStack object to be serialized.
     * @param context The YamlContext in which to serialize the object.
     * @return The serialized ConfigurationSection containing ItemStack data.
     */
    @Override
    public ConfigurationSection serialize(ItemStack obj, YamlContext context) {
        context.set("item", BLib.getApi().getItemStackSerialize().serialize(obj));
        return context.getHandle();
    }

    /**
     * Deserialize an ItemStack object from the specified YamlContext.
     *
     * @param context The YamlContext containing the ItemStack data.
     * @return The deserialized ItemStack object.
     */
    @Override
    public ItemStack deserialize(YamlContext context) {
        return BLib.getApi().getItemStackSerialize().deserialize(context.getAsString("item"));
    }
}
