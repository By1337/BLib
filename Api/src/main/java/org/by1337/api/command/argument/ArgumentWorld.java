package org.by1337.api.command.argument;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.by1337.api.command.CommandSyntaxError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an argument that accepts a valid world name as input.
 */
public class ArgumentWorld<T> extends ArgumentSetList<T> {
    /**
     * Constructs an ArgumentWorld with the specified name, using available world names as options.
     *
     * @param name The name of the argument.
     */
    public ArgumentWorld(String name) {
        super(name, Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
    }

    /**
     * Constructs an ArgumentWorld with the specified name, custom examples, and available world names as options.
     *
     * @param name The name of the argument.
     * @param exx  A list of example values for the argument.
     */
    public ArgumentWorld(String name, List<String> exx) {
        super(name, exx, Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
    }

    /**
     * Processes the input string and returns the corresponding world object if the world name is valid.
     *
     * @param sender The sender of the command.
     * @param str    The input string to process.
     * @return The world object corresponding to the valid world name provided in the input.
     * @throws CommandSyntaxError If the input string is not a valid world name.
     */
    @Override
    public Object process(T sender, String str) throws CommandSyntaxError {
        if (str.isEmpty()) return null;
        return Bukkit.getWorld(str);
    }
}
