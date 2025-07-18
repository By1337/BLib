package org.by1337.blib.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.by1337.blib.text.MessageFormatter;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An interface for spawning entities using a summon command.
 */
@Deprecated(forRemoval = true)
public interface SummonCommand {
    SummonCommand INSTANCE = new SummonCommand() {};
    /**
     * Spawns an entity of the specified type at the given location with optional NBT data.
     *
     * @param entityType The type of entity to summon, e.g., "minecraft:creeper".
     * @param location   The location where the entity should be spawned.
     * @param nbt        Optional NBT data for the summoned entity, or null if not needed.
     */
    default void spawn(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        Location location1 = location.getLocation();
        World world = location1.getWorld();
        String worldName;
        if (world.getName().equals("world")) {
            worldName = "minecraft:overworld";
        } else if (world.getName().contains(":")) {
            worldName = world.getName();
        } else {
            worldName = "minecraft:" + world.getName();
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                MessageFormatter.apply(
                        "execute in {} run summon {} {} {} {} {}",
                        worldName,
                        entityType,
                        location1.getX(),
                        location1.getY(),
                        location1.getZ(),
                        Objects.requireNonNullElse(nbt, "{}")
                )
                );
    }
}
