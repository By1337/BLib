package org.by1337.blib.nms.v1_18_2.command;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

@NMSAccessor(forClazz = BukkitCommandRegister.class, from = Version.V1_18_2)
public class BukkitCommandRegisterV1_18_2 implements BukkitCommandRegister {

    @Override
    public void register(BukkitCommand bukkitCommand) {
        Bukkit.getServer().getCommandMap().register(bukkitCommand.getName(), bukkitCommand);
    }

    @Override
    public void unregister(BukkitCommand bukkitCommand) {
        for (var s : bukkitCommand.getAliases()) {
            Bukkit.getServer().getCommandMap().getKnownCommands().remove(bukkitCommand.getName() + ":" + s);
            Bukkit.getServer().getCommandMap().getKnownCommands().remove(s);
        }
        Bukkit.getServer().getCommandMap().getKnownCommands().remove(bukkitCommand.getName() + ":" + bukkitCommand.getName());
        Bukkit.getServer().getCommandMap().getKnownCommands().remove(bukkitCommand.getName());
        bukkitCommand.unregister(Bukkit.getServer().getCommandMap());
    }
}
