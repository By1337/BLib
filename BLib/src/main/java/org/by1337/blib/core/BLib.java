package org.by1337.blib.core;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.by1337.blib.command.argument.ArgumentBoolean;
import org.by1337.blib.util.Version;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.CommandException;
import org.by1337.blib.command.argument.ArgumentSetList;
import org.by1337.blib.command.requires.RequiresPermission;
import org.by1337.blib.core.config.Config;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.core.test.CommandTests;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

public class BLib extends JavaPlugin {
    @Getter
    private static Plugin instance;
    private Config config;

    public static boolean DEBUG = false;

    @Override
    public void onLoad() {
        instance = this;
        org.by1337.blib.BLib.setApi(new BApi());
        setCommand();
    }

    @Override
    public void onEnable() {
        init();
        getCommand("blib").setExecutor(this);
        getCommand("blib").setTabCompleter(this);
    }

    private void init() {
        config = new Config();
        config.load();
        Lang.loadTranslations(config.lang);
        getLogger().log(Level.INFO, String.format(Lang.getMessage("detect-version"), Version.VERSION.getVer()));
    }

    private static Command<CommandSender> command;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String label, @NotNull String[] args) {
        try {
            command.process(sender, args);
            return true;
        } catch (CommandException e) {
            sender.sendMessage(e.getLocalizedMessage());
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String alias, @NotNull String[] args) {
        return command.getTabCompleter(sender, args);
    }

    private void setCommand() {
        command = new Command<CommandSender>("blib")
                .requires(new RequiresPermission<>("blib.use"))
                .addSubCommand(new Command<CommandSender>("reload")
                        .requires(new RequiresPermission<>("blib.reload"))
                        .executor(((sender, args) -> {
                            init();
                            sender.sendMessage(Lang.getMessage("reload"));
                        }))
                )
                .addSubCommand(new Command<CommandSender>("language")
                        .aliases("lang")
                        .requires(new RequiresPermission<>("blib.lang"))
                        .argument(new ArgumentSetList<>("language", Lang.LANGUAGES))
                        .executor(((sender, args) -> {
                            String lang = (String) args.getOrThrow("language", Lang.getMessage("missing-argument"), "language");
                            Lang.loadTranslations(lang);
                            config.lang = lang;
                            config.save();
                            sender.sendMessage(Lang.getMessage("language-changed"));
                        }))
                )
                .addSubCommand(new Command<CommandSender>("enableDebug")
                        .requires(new RequiresPermission<>("blib.debug"))
                        .argument(new ArgumentBoolean<>("enable"))
                        .executor(((sender, args) -> {
                            DEBUG = (boolean) args.getOrDefault("enable", !DEBUG);
                        }))
                )
        ;

        command.addSubCommand(new Command<CommandSender>("test")
                .requires((pl) -> DEBUG)
                .requires(new RequiresPermission<>("blib.tests"))
                .addSubCommand(CommandTests.packetArmorStandTest())
                .addSubCommand(CommandTests.sysInfo())
                .addSubCommand(CommandTests.msgTest())
                .addSubCommand(CommandTests.miniMsgTest())
                .addSubCommand(CommandTests.sleep())
        );

    }
}
