package org.by1337.api.command.requires;

import lombok.ToString;
import org.bukkit.command.CommandSender;

/**
 * A command requirement that checks if a sender has a specific permission.
 */
@ToString
public class RequiresPermission<T extends CommandSender> implements Requires<T> {
    private String permission;

    /**
     * Constructs a RequiresPermission instance with the specified permission.
     *
     * @param permission The permission to check for.
     */
    public RequiresPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Checks if the command sender has the required permission.
     *
     * @param sender The sender of the command.
     * @return `true` if the sender has the permission, otherwise `false`.
     */
    @Override
    public boolean check(T sender) {
        return sender.hasPermission(permission);
    }
}
