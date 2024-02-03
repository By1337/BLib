package org.by1337.blib.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.by1337.blib.BLib;
import org.by1337.blib.configuration.YamlContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultCommand {
    private String cmd;
    @Nullable
    private String permission;
    private String usage;
    @Nullable
    private String successfulCompletion;
    private List<String> aliases;

    private BukkitCommand bukkitCommand;

    protected Command<CommandSender> command;

    public DefaultCommand(String cmd, @Nullable String permission, @NotNull String usage, @Nullable String successfulCompletion, List<String> aliases) {
        this.cmd = cmd;
        this.permission = permission;
        this.usage = usage;
        this.successfulCompletion = successfulCompletion;
        this.aliases = aliases;
    }

    public DefaultCommand(YamlContext context, String cmd) {
        this.cmd = cmd;
        this.permission = context.getAsString("permission", "none");
        if (permission.equals("none")) permission = null;
        this.usage = Objects.requireNonNull(context.getAsString("usage"));
        this.successfulCompletion = context.getAsString("successful-completion", "none");
        if (successfulCompletion.equals("none")) successfulCompletion = null;
        this.aliases = context.getList("aliases", String.class, Collections.emptyList());
    }


    public void execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        try {
            command.process(sender, args);
            if (successfulCompletion != null) {
                BLib.getApi().getMessage().sendMsg(sender, successfulCompletion);
            }
        } catch (CommandException e) {
            BLib.getApi().getMessage().sendMsg(sender, getUsage());
        } catch (Exception e) {
            BLib.getApi().getMessage().error(e);
        }
    }

    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return command.getTabCompleter(sender, args);
    }

    public void register() {
        bukkitCommand = new BukkitCommand(cmd) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                DefaultCommand.this.execute(sender, commandLabel, args);
                return true;
            }

            @NotNull
            @Override
            public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
                return DefaultCommand.this.tabComplete(sender, alias, args);
            }
        };
        bukkitCommand.setAliases(aliases);
        bukkitCommand.setPermission(permission);
        BLib.getApi().getBukkitCommandRegister().register(bukkitCommand);
    }

    public void unregister() {
       BLib.getApi().getBukkitCommandRegister().unregister(bukkitCommand);
    }

    public void setCommand(Command<CommandSender> command) {
        this.command = command;
    }
    public String getCmd() {
        return cmd;
    }

    public @Nullable String getPermission() {
        return permission;
    }

    public @NotNull String getUsage() {
        return usage;
    }

    public @Nullable String getSuccessfulCompletion() {
        return successfulCompletion;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
