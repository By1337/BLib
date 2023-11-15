package org.by1337.api.command.requires;

import org.bukkit.command.CommandSender;

/**
 * Functional interface for checking command requirements.
 */
@FunctionalInterface
public interface Requires {

    /**
     * Checks whether a specific requirement is met by the command sender.
     *
     * @param sender The sender of the command.
     * @return `true` if the requirement is met, otherwise `false`.
     */
    boolean check(CommandSender sender);
}
