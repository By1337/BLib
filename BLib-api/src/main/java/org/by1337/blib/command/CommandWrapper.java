package org.by1337.blib.command;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendSuggestionsEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.by1337.blib.BLib;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandWrapper extends BukkitCommand implements Listener {
    private final Command<CommandSender> command;
    private final Set<String> allPossibleNames;
    private final Plugin plugin;
    private final boolean async;

    public CommandWrapper(Command<CommandSender> command, Plugin plugin) {
        super(command.getCommand());
        setAliases(command.getAliases().stream().toList());
        this.command = command;
        this.plugin = plugin;
        allPossibleNames = new HashSet<>();
        allPossibleNames.addAll(command.getAliases());
        allPossibleNames.add(command.getCommand());
        async = command.allowAsync();

    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        BLib.getApi().getBukkitCommandRegister().register(this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        try {
            command.process(sender, args);
            return true;
        } catch (CommandException e) {
            BLib.getApi().getMessage().sendMsg(sender, e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return command.tabComplete(sender, args);
    }

    @EventHandler
    @SuppressWarnings("all")
    public void on(AsyncPlayerSendSuggestionsEvent event) {
        if (event.isAsynchronous() && !async) return;
        String input = event.getBuffer();
        StringReader reader = new StringReader(input);
        if (reader.hasNext() && reader.next() == '/') {
            String cmd = reader.readToSpace();
            reader.pop();
            if (allPossibleNames.contains(cmd)) {
                event.setSuggestions(command.tabComplete(event.getPlayer(), reader));
            }
        }
    }

    public void close() {
        BLib.getApi().getBukkitCommandRegister().unregister(this);
        AsyncPlayerSendSuggestionsEvent.getHandlerList().unregister(this);
    }

}
