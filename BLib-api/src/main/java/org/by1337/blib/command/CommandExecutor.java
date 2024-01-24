package org.by1337.blib.command;

import org.by1337.blib.command.argument.ArgumentMap;

/**
 * Functional interface for executing a command.
 */
@FunctionalInterface
public interface CommandExecutor<T> {

    /**
     * Executes a command with the provided sender and arguments.
     *
     * @param sender The sender of the command.
     * @param args   The map of command arguments.
     * @throws CommandSyntaxError If there's a syntax error in the command execution.
     */
    void executes(T sender, ArgumentMap<String, Object> args) throws CommandException;
}
