package org.by1337.blib.command;

import dev.by1337.core.BCore;
import org.bukkit.command.defaults.BukkitCommand;
import org.by1337.blib.BLib;

public interface BukkitCommandRegister {
    BukkitCommandRegister INSTANCE = new BukkitCommandRegister() {
        dev.by1337.core.bridge.command.BukkitCommandRegister bridge = BCore.getBukkitCommandRegister();
        @Override
        public void register(BukkitCommand bukkitCommand) {
            bridge.register(bukkitCommand);
        }

        @Override
        public void unregister(BukkitCommand bukkitCommand) {
            bridge.unregister(bukkitCommand);
        }
    };
    static BukkitCommandRegister getInstance() {
        return INSTANCE;
    }

    void register(BukkitCommand bukkitCommand);

    void unregister(BukkitCommand bukkitCommand);
}
