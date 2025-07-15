package org.by1337.blib.nms.v1_17_1.command;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.command.CraftCommandMap;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

@NMSAccessor(forClazz = BukkitCommandRegister.class, forVersions = Version.V1_17_1)
public class BukkitCommandRegisterV1_17_1 implements BukkitCommandRegister {
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
