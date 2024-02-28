package org.by1337.blib.nms.v1_18_2.command;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.command.CraftCommandMap;
import org.by1337.blib.command.BukkitCommandRegister;

public class BukkitCommandRegisterV1_18_2 implements BukkitCommandRegister {
    @Override
    public void register(BukkitCommand bukkitCommand) {
        CraftServer server = (CraftServer) Bukkit.getServer();
        server.getCommandMap().register(bukkitCommand.getName(), bukkitCommand);
    }

    @Override
    public void unregister(BukkitCommand bukkitCommand) {
        for (var s : bukkitCommand.getAliases()) {
            ((CraftCommandMap) ((CraftServer) Bukkit.getServer()).getCommandMap()).getKnownCommands().remove(bukkitCommand.getName() + ":" + s);
            ((CraftCommandMap) ((CraftServer) Bukkit.getServer()).getCommandMap()).getKnownCommands().remove(s);
        }
        ((CraftCommandMap) ((CraftServer) Bukkit.getServer()).getCommandMap()).getKnownCommands().remove(bukkitCommand.getName() + ":" + bukkitCommand.getName());
        ((CraftCommandMap) ((CraftServer) Bukkit.getServer()).getCommandMap()).getKnownCommands().remove(bukkitCommand.getName());
        bukkitCommand.unregister(((CraftServer) Bukkit.getServer()).getCommandMap());
    }
}