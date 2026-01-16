package org.by1337.blib.core;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.by1337.blib.Api;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.custom.registry.BlockRegistry;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.PooledBlockReplacer;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.CommandWrapper;
import org.by1337.blib.command.argument.ArgumentPlayer;
import org.by1337.blib.command.argument.ArgumentSetList;
import org.by1337.blib.command.requires.RequiresPermission;
import org.by1337.blib.core.block.CustomBlockManager;
import org.by1337.blib.core.test.CommandTests;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.translation.Translation;
import org.by1337.blib.util.SpacedNameKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BApi implements Api {
    private final Message message;
    private final Logger logger;
    private final PooledBlockReplacer pooledBlockReplacer;
    private CustomBlockManager customBlockManager;
    private CommandWrapper commandWrapper;
    private Plugin plugin;
    private final File legacyDataFolder;

    public BApi(Plugin plugin) {
        this.plugin = plugin;
        legacyDataFolder = plugin.getDataFolder();
        logger = plugin.getLogger();
        File file = new File(legacyDataFolder, "translation.json");
        if (!file.exists()) {
            saveResource("translation.json", false, legacyDataFolder);
        }
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            message = new Message(logger, reader);
            Translation translation = message.getTranslation();
            try (var in = new InputStreamReader(plugin.getResource("translation.json"), StandardCharsets.UTF_8)) {
                translation.saveDefaults(in);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pooledBlockReplacer = new PooledBlockReplacer(
                plugin,
                15,
                BlockReplacer.INSTANCE,
                message
        );
        customBlockManager = new CustomBlockManager(legacyDataFolder);
        org.by1337.blib.BLib.setApi(this);
    }

    public void onEnable() {
        Lang.loadTranslations(message.getTranslation());
        pooledBlockReplacer.enable();
        customBlockManager.load();
        commandWrapper = new CommandWrapper(create(), plugin);
        commandWrapper.setPermission("blib.use");
        commandWrapper.register();
    }

    public void onDisable() {
        pooledBlockReplacer.close();
        customBlockManager.save();
    }

    @Override
    public @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    public @NotNull Message getMessage() {
        return message;
    }

    @Override
    public @NotNull PooledBlockReplacer getPooledBlockReplacer() {
        return pooledBlockReplacer;
    }


    private void saveResource(@NotNull String resourcePath, boolean replace, File dataFolder) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = this.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + this.getClass().getClassLoader());
            } else {
                File outFile = new File(dataFolder, resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                        getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException ex) {
                    getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    private @Nullable InputStream getResource(@NotNull String filename) {
        try {
            URL url = BApi.class.getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }

    private Command<CommandSender> create() {
        return new Command<CommandSender>("blib")
                .requires(new RequiresPermission<>("blib.use"))
                .addSubCommand(new Command<CommandSender>("reload")
                        .requires(new RequiresPermission<>("blib.reload"))
                        .executor(((sender, args) -> {
                            File file = new File(legacyDataFolder, "translation.json");
                            if (!file.exists()) {
                                saveResource("translation.json", false, legacyDataFolder);
                            }
                            try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                                var translation = Translation.fromJson(reader, message);
                                message.setTranslation(translation);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Lang.loadTranslations(message.getTranslation());
                            sender.sendMessage(Lang.getMessage("reload"));
                        }))
                ).addSubCommand(new Command<CommandSender>("cb")
                        .requires(new RequiresPermission<>("blib.cb"))
                        .addSubCommand(new Command<CommandSender>("list")
                                .requires(new RequiresPermission<>("blib.cb.list"))
                                .executor(((sender, args) -> {
                                    Collection<CustomBlock> customBlocks = BlockRegistry.get().getAll();
                                    StringBuilder sb = new StringBuilder();
                                    int x = 0;
                                    for (CustomBlock customBlock : customBlocks) {
                                        sb.append("&f").append(++x).append(". id:'&a").append(customBlock.getId()).append("&f' loader:'&a").append(BlockRegistry.getPlugin(customBlock.getClass())).append("&f'\n");
                                    }
                                    message.sendMsg(sender, sb.toString(), sender);
                                }))
                        )
                        .addSubCommand(new Command<CommandSender>("give")
                                .requires(new RequiresPermission<>("blib.cb.give"))
                                .argument(new ArgumentSetList<>("id", () -> BlockRegistry.get().getAll().stream().map(c -> c.getId().toString()).toList()))
                                .argument(new ArgumentPlayer<>("player"))
                                .executor(((sender, args) -> {
                                    String id = (String) args.getOrThrow("id", "/blib cb give <id>");
                                    Player player = (Player) args.getOrDefault("player", sender instanceof Player pl ? pl : null);
                                    if (player == null) {
                                        message.sendTranslatable(sender, "no-selected-player");
                                        return;
                                    }
                                    SpacedNameKey spacedNameKey = new SpacedNameKey(id);
                                    CustomBlock customBlock = BlockRegistry.get().getCustomBlock(spacedNameKey);
                                    player.getInventory().addItem(customBlock.getItem()).forEach((slot, i) -> {
                                        player.getWorld().dropItemNaturally(player.getLocation(), i);
                                    });
                                }))
                        )
                )
                .executor((sender, args) -> {
                    message.sendTranslatable(sender, "hello-message", plugin.getDescription().getVersion());
                })
                .addSubCommand(new Command<CommandSender>("test")
                        .requires(new RequiresPermission<>("blib.tests"))
                        .addSubCommand(CommandTests.sysInfo())
                        .addSubCommand(CommandTests.msgTest())
                        .addSubCommand(CommandTests.logTest())
                        .addSubCommand(CommandTests.sleep())
                        .addSubCommand(CommandTests.itemClone())
                        .addSubCommand(CommandTests.args())
                        .addSubCommand(CommandTests.gc())
                )
                ;


    }
}
