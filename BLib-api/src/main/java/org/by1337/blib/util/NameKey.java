package org.by1337.blib.util;

import org.by1337.blib.chat.util.InvalidCharacters;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The NameKey class represents a unique key based on a name, ensuring its validity.
 * It is used to identify objects and entities with names in the application.
 */
public class NameKey {
    private final String name;

    /**
     * Constructs a NameKey with the given name.
     *
     * @param name The name to be used as the key.
     * @throws IllegalArgumentException If the name ends with "_root" or contains invalid characters.
     */
    public NameKey(String name) {
        InvalidCharacters.validate(name);
        if (name.endsWith("_root")) {
            throw new IllegalArgumentException(String.format("The name cannot end with '_root': '%s'", name));
        }
        this.name = name;
    }

    /**
     * Constructs a NameKey with the given name and a flag indicating if it is a root key.
     *
     * @param name   The name to be used as the key.
     * @param isRoot Indicates whether this key is a root key.
     * @throws IllegalArgumentException If the name ends with "_root" when it is not a root key
     *                                or if it contains invalid characters.
     */
    public NameKey(String name, boolean isRoot) {
        InvalidCharacters.validate(name);
        if (!isRoot && name.endsWith("_root")) {
            throw new IllegalArgumentException(String.format("The name cannot end with '_root': '%s'", name));
        }
        this.name = name;
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
