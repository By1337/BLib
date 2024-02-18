package org.by1337.blib.inventory;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * An interface for serializing and deserializing Bukkit ItemStacks to/from string representations.
 */
public interface ItemStackSerialize {
    /**
     * Serializes the given ItemStack into a string representation.
     *
     * @param itemStack The ItemStack to be serialized.
     * @return A string representation of the serialized ItemStack.
     * @throws IllegalArgumentException If the serialization fails or the ItemStack is invalid.
     */
    @NotNull
    String serialize(@NotNull ItemStack itemStack) throws IllegalArgumentException;

    @NotNull
    String serializeAndCompress(@NotNull ItemStack itemStack) throws IllegalArgumentException;

    /**
     * Deserializes an ItemStack from the provided string representation.
     *
     * @param data The string representation of the ItemStack.
     * @return The deserialized ItemStack.
     * @throws IllegalArgumentException If the deserialization fails or the string data is invalid.
     */
    @NotNull
    ItemStack deserialize(@NotNull String data) throws IllegalArgumentException;

    @NotNull
    ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException;

    String compress(String raw) throws IOException;
    String decompress(String raw) throws IOException;
}
