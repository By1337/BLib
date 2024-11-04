package org.by1337.blib.util;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.DataResult;
import org.by1337.blib.chat.util.InvalidCharacters;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The NameKey class represents a unique key based on a name, ensuring its validity.
 * It is used to identify objects and entities with names in the application.
 */
public class NameKey {
    public static Codec<NameKey> CODEC = Codec.STRING.comapFlatMap(NameKey::validate, NameKey::getKey);
    private final String name;

    /**
     * Constructs a NameKey with the given name.
     *
     * @param name The name to be used as the key.
     */
    public NameKey(@NotNull String name) {
        Objects.requireNonNull(name, "name is null");
        InvalidCharacters.validate(name);
        this.name = name;
    }
    public static DataResult<NameKey> validate(String id){
        try {
            return DataResult.success(new NameKey(id));
        } catch (IllegalArgumentException e) {
            return DataResult.error(() -> "Not a valid name: " + id + " " + e.getMessage());
        }
    }

    /**
     * Constructs a NameKey with the given name and a flag indicating if it is a root key.
     *
     * @param name   The name to be used as the key.
     * @param isRoot nothing
     */
    @Deprecated
    public NameKey(@NotNull String name, boolean isRoot) {
        this(name);
    }


    /**
     * Get the name associated with this NameKey.
     *
     * @return The name of the NameKey.
     */
    @NotNull
    public String getName() {
        return name;
    }
    @NotNull
    public String getKey() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameKey nameKey)) return false;
        return Objects.equals(getName(), nameKey.getName());
    }
    public int compareTo(NameKey nameKey) {
        return name.compareTo(nameKey.name);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "NameKey{" +
                "name='" + name + '\'' +
                '}';
    }
}
