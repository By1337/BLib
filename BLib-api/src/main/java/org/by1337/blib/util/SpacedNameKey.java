package org.by1337.blib.util;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.DataResult;
import blib.com.mojang.serialization.DynamicOps;
import blib.com.mojang.serialization.codecs.PrimitiveCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SpacedNameKey implements SpacedNamed {
    public static Codec<SpacedNameKey> CODEC = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<SpacedNameKey> read(DynamicOps<T> ops, T t) {
            return ops.getStringValue(t).flatMap(s -> {
                try {
                    return DataResult.success(new SpacedNameKey(s));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Not a valid spaced name: " + s + " " + e.getMessage());
                }
            });
        }

        @Override
        public <T> T write(DynamicOps<T> ops, SpacedNameKey spacedNameKey) {
            return ops.createString(spacedNameKey.toString());
        }
    };

    private final NameKey space;
    private final NameKey name;

    public SpacedNameKey(@NotNull NameKey space, @NotNull NameKey name) {
        Objects.requireNonNull(space, "space is null");
        Objects.requireNonNull(name, "name is null");
        this.space = space;
        this.name = name;
    }

    public SpacedNameKey(@NotNull String space, @NotNull String name) {
        Objects.requireNonNull(space, "space is null");
        Objects.requireNonNull(name, "name is null");
        this.space = new NameKey(space);
        this.name = new NameKey(name);
    }

    public SpacedNameKey(@NotNull String spacedNameKey) {
        Objects.requireNonNull(spacedNameKey, "spacedNameKey is null");
        if (!spacedNameKey.contains(":")) throw new IllegalArgumentException("Expected <space>:<name>");
        String[] arr = spacedNameKey.split(":");
        if (arr.length != 2) throw new IllegalArgumentException("Expected <space>:<name>");
        this.space = new NameKey(arr[0]);
        this.name = new NameKey(arr[1]);
    }

    public static SpacedNameKey fromString(@NotNull String spacedName, @NotNull String defaultSpace) {
        Objects.requireNonNull(spacedName, "spacedName is null");
        Objects.requireNonNull(defaultSpace, "defaultSpace is null");
        if (spacedName.contains(":")) return new SpacedNameKey(spacedName);
        return new SpacedNameKey(defaultSpace, spacedName);
    }

    public @NotNull NameKey getSpace() {
        return space;
    }

    public @NotNull NameKey getName() {
        return name;
    }

    @Override
    public @NotNull String toString() {
        return space.getName() + ":" + name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpacedNameKey that = (SpacedNameKey) o;
        return Objects.equals(space, that.space) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(space, name);
    }

    @Override
    public @NotNull SpacedNameKey getSpacedName() {
        return this;
    }
}
