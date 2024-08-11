package org.by1337.blib.nms.V1_21.command;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.CraftServer;
import org.by1337.blib.command.BukkitCommandRegister;

public class BukkitCommandRegisterV1_21_1 implements BukkitCommandRegister {
    @Override
    public void register(BukkitCommand bukkitCommand) {
        CraftServer server = (CraftServer) Bukkit.getServer();
        server.getCommandMap().register(bukkitCommand.getName(), bukkitCommand);
    }

    @Override
    public void unregister(BukkitCommand bukkitCommand) {
        for (var s : bukkitCommand.getAliases()) {
            ((CraftServer) Bukkit.getServer()).getCommandMap().getKnownCommands().remove(bukkitCommand.getName() + ":" + s);
            ((CraftServer) Bukkit.getServer()).getCommandMap().getKnownCommands().remove(s);
        }
        ((CraftServer) Bukkit.getServer()).getCommandMap().getKnownCommands().remove(bukkitCommand.getName() + ":" + bukkitCommand.getName());
        ((CraftServer) Bukkit.getServer()).getCommandMap().getKnownCommands().remove(bukkitCommand.getName());
        bukkitCommand.unregister(((CraftServer) Bukkit.getServer()).getCommandMap());
    }
}
