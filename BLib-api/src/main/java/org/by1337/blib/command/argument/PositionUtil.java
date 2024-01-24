package org.by1337.blib.command.argument;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for retrieving position-related information from a CommandSender.
 */
public class PositionUtil {
    /**
     * Get the X coordinate of a player's location if the CommandSender is a player.
     *
     * @param sender The CommandSender whose X coordinate is to be retrieved.
     * @return The X coordinate of the player's location, or 0 if the sender is not a player.
     */
    public static Number getXIfPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player player ? player.getLocation().getX() : 0;
    }

    /**
     * Get the Y coordinate of a player's location if the CommandSender is a player.
     *
     * @param sender The CommandSender whose Y coordinate is to be retrieved.
     * @return The Y coordinate of the player's location, or 0 if the sender is not a player.
     */
    public static Number getYIfPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player player ? player.getLocation().getY() : 0;
    }

    /**
     * Get the Z coordinate of a player's location if the CommandSender is a player.
     *
     * @param sender The CommandSender whose Z coordinate is to be retrieved.
     * @return The Z coordinate of the player's location, or 0 if the sender is not a player.
     */
    public static Number getZIfPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player player ? player.getLocation().getZ() : 0;
    }

    /**
     * Get the world of a player's location if the CommandSender is a player, or the default world otherwise.
     *
     * @param sender The CommandSender whose world is to be retrieved.
     * @return The world of the player's location if the sender is a player, or the default world.
     */
    public static World getWorldIfPlayer(@NotNull CommandSender sender) {
        if (sender instanceof Player player) {
            return player.getLocation().getWorld();
        } else {
            World defaultWorld = Bukkit.getWorld("world");
            return defaultWorld != null ? defaultWorld : Bukkit.getWorlds().get(0);
        }
    }

    /**
     * Get the location (coordinates and world) of a player's position if the CommandSender is a player,
     * or the default world and (0, 0, 0) coordinates otherwise.
     *
     * @param sender The CommandSender whose location is to be retrieved.
     * @return The location of the player's position if the sender is a player,
     *         or a default location in the default world if the sender is not a player.
     */
    public static Location getLocationIfPlayer(CommandSender sender) {
        return new Location(getWorldIfPlayer(sender), getXIfPlayer(sender).doubleValue(), getYIfPlayer(sender).doubleValue(), getZIfPlayer(sender).doubleValue());
    }
}
