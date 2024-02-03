package org.by1337.blib.command;

import org.bukkit.command.defaults.BukkitCommand;
import org.by1337.blib.BLib;

public interface BukkitCommandRegister {
    static BukkitCommandRegister getInstance() {
        return BLib.getApi().getBukkitCommandRegister();
    }

    void register(BukkitCommand bukkitCommand);

    void unregister(BukkitCommand bukkitCommand);
}
