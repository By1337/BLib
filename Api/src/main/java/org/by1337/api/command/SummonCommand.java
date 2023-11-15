package org.by1337.api.command;

import org.by1337.api.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface for spawning entities using a summon command.
 */
public interface SummonCommand {
    /**
     * Spawns an entity of the specified type at the given location with optional NBT data.
     *
     * @param entityType The type of entity to summon, e.g., "minecraft:creeper".
     * @param location   The location where the entity should be spawned.
     * @param nbt        Optional NBT data for the summoned entity, or null if not needed.
     */
    void spawn(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt);
}
