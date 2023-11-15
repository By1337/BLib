package org.by1337.api.command;

import org.bukkit.command.CommandSender;
import org.by1337.api.command.argument.ArgumentMap;

/**
 * Functional interface for executing a command.
 */
@FunctionalInterface
public interface CommandExecutor {

    /**
     * Executes a command with the provided sender and arguments.
     *
     * @param sender The sender of the command.
     * @param args   The map of command arguments.
     * @throws CommandSyntaxError If there's a syntax error in the command execution.
     */
    void executes(CommandSender sender, ArgumentMap<String, Object> args) throws CommandException;
}
