package org.by1337.api.configuration.adapter;

import org.bukkit.configuration.ConfigurationSection;

/**
 * An interface for defining class-specific adapters that can serialize and deserialize objects of a specific type
 * to and from a ConfigurationSection.
 *
 * @param <T> The type of object that this adapter can handle.
 */
public interface ClassAdapter<T> extends Adapter<T, ConfigurationSection> {
}
