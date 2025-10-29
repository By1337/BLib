package org.by1337.blib.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.custom.impl.CustomBlockExample;
import org.by1337.blib.block.custom.listener.CustomBlockListener;
import org.by1337.blib.block.custom.registry.BlockRegistry;
import org.by1337.blib.block.custom.registry.WorldRegistry;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.CommandWrapper;
import org.by1337.blib.command.argument.*;
import org.by1337.blib.command.requires.RequiresPermission;
import org.by1337.blib.core.block.CustomBlockManager;
import org.by1337.blib.core.fastutil.FastUtilCommands;
import org.by1337.blib.core.nms.NMSBootstrap;
import org.by1337.blib.core.nms.NMSTests;
import org.by1337.blib.core.nms.NmsFactory;
import org.by1337.blib.core.nms.verify.NMSVerify;
import org.by1337.blib.core.test.CommandTests;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.translation.Translation;
import org.by1337.blib.util.SpacedNameKey;
import org.by1337.blib.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BLib extends JavaPlugin {
    private static final Logger log = LoggerFactory.getLogger(BLib.class);
    private static Plugin instance;
    private BApi api;
    public static boolean DEBUG = false;
    private CustomBlockManager customBlockManager;
    private CommandWrapper commandWrapper;

    public static Plugin getInstance() {
        return BLib.instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        File libraries = new File(getDataFolder(), "libraries");
        if (!libraries.exists()) {
            getSLF4JLogger().info("Downloading libraries...");
            libraries.mkdirs();
        }
        LibLoader.load(libraries.toPath(), this);

        api = new BApi();
        org.by1337.blib.BLib.setApi(api);
        customBlockManager = new CustomBlockManager();
    }

    @Override
    public void onEnable() {
        init();
        setCommand();

        commandWrapper = new CommandWrapper(command, this);
        commandWrapper.setPermission("blib.use");
        commandWrapper.register();

        command.addSubCommand(FastUtilCommands.SET);
        command.addSubCommand(FastUtilCommands.SHEM_PASTE);

        api.getPooledBlockReplacer().enable();

        CustomBlockExample example = new CustomBlockExample();
        BlockRegistry.get().register(example.getId(), example);
        Bukkit.getPluginManager().registerEvents(new CustomBlockListener(WorldRegistry.CUSTOM_BLOCK_REGISTRY), this);
        customBlockManager.load();
    }

    @Override
    public void onDisable() {
        api.getPooledBlockReplacer().close();
        customBlockManager.save();
        commandWrapper.close();
    }

    private void init() {
        Lang.loadTranslations(api.getMessage().getTranslation());
        api.getMessage().log(Lang.getMessage("detect-version"), Version.VERSION.getVer());
    }

    private static Command<CommandSender> command;

    private void setCommand() {
        command = new Command<CommandSender>("blib")
                .requires(new RequiresPermission<>("blib.use"))
                .addSubCommand(new Command<CommandSender>("reload")
                        .requires(new RequiresPermission<>("blib.reload"))
                        .executor(((sender, args) -> {
                            File file = new File(BLib.getInstance().getDataFolder(), "translation.json");
                            if (!file.exists()) {
                                BLib.getInstance().saveResource("translation.json", false);
                            }
                            try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                                var translation = Translation.fromJson(reader, api.getMessage());
                                api.getMessage().setTranslation(translation);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            init();
                            sender.sendMessage(Lang.getMessage("reload"));
                        }))
                ).addSubCommand(new Command<CommandSender>("enableDebug")
                        .requires(new RequiresPermission<>("blib.debug"))
                        .argument(new ArgumentBoolean<>("enable"))
                        .executor(((sender, args) -> {
                            DEBUG = (boolean) args.getOrDefault("enable", !DEBUG);
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
                                    api.getMessage().sendMsg(sender, sb.toString(), sender);
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
                                        api.getMessage().sendTranslatable(sender, "no-selected-player");
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
                    api.getMessage().sendTranslatable(sender, "hello-message", getDescription().getVersion());
                })
                .addSubCommand(new Command<CommandSender>("nms")
                        .requires(new RequiresPermission<>("blib.nms"))
                        .addSubCommand(new Command<CommandSender>("nms_test")
                                .requires(new RequiresPermission<>("blib.nms_test"))
                                .argument(new ArgumentEnumValue<CommandSender>("ver", Version.class))
                                .executor(((sender, args) -> {
                                    Player player = (Player) sender;
                                    Version version = (Version) args.get("ver");
                                    System.out.println(
                                            NMSTests.run(player, version == null ? Version.VERSION : version)
                                    );
                                }))
                        ).addSubCommand(new Command<CommandSender>("nms_verify")
                                .requires(new RequiresPermission<>("blib.nms_verify"))
                                .argument(new MultiArgument<>("clazz",
                                        new ArgumentChoice<>("clazz", NmsFactory.get().nmsSuppliers().keySet().stream().toList()),
                                        new ArgumentString<>("clazz")
                                        ))
                                .executor(((sender, args) -> {
                                    String clazz = (String) args.getOrThrow("clazz", "Use: /blib nms_verify <class>");
                                    for (String key : NmsFactory.get().nmsSuppliers().keySet()) {
                                        if (wildcardMatch(key, clazz)){
                                            try {
                                                Object creator = NmsFactory.get().nmsSuppliers().get(key).get();
                                                new NMSVerify("org/by1337/").verify(creator.getClass());
                                            } catch (Throwable throwable) {
                                                log.error("Failed to verify nms class {}, on version {}", key, Version.VERSION.getVer(), throwable);
                                                sender.sendMessage(Component.text("Not valid: " + key).color(NamedTextColor.RED));
                                                continue;
                                            }
                                            sender.sendMessage(Component.text("Valid: " + key).color(NamedTextColor.GREEN));
                                        }
                                    }
                                }))
                        )
                        .addSubCommand(new Command<CommandSender>("nms_verify_current")
                                .requires(new RequiresPermission<>("blib.nms_verify"))
                                .executor(((sender, args) -> {
                                    for (NmsFactory.ByVersionHolder holder : NmsFactory.get().creators().values()) {
                                        Class<?> cl = holder.nms();
                                        try {
                                            Object creator = holder.findFor(Version.VERSION).get();
                                            cl = creator.getClass();
                                            new NMSVerify("org/by1337/").verify(creator.getClass());
                                        } catch (Throwable throwable) {
                                            log.error("Failed to verify nms class {}, on version {}", cl,   Version.VERSION.getVer(), throwable);
                                            sender.sendMessage(Component.text("Not valid: " + cl).color(NamedTextColor.RED));
                                            continue;
                                        }
                                        sender.sendMessage(Component.text("Valid: " + cl).color(NamedTextColor.GREEN));

                                    }
                                }))
                        )
                )
        ;

        command.addSubCommand(new Command<CommandSender>("test")
                .requires((pl) -> DEBUG)
                .requires(new RequiresPermission<>("blib.tests"))
                .addSubCommand(CommandTests.packetArmorStandTest())
                .addSubCommand(CommandTests.sysInfo())
                .addSubCommand(CommandTests.msgTest())
                .addSubCommand(CommandTests.logTest())
                .addSubCommand(CommandTests.sleep())
                .addSubCommand(CommandTests.itemClone())
                .addSubCommand(CommandTests.args())
                .addSubCommand(CommandTests.gc())
        );

    }
    public static boolean wildcardMatch(String text, String pattern) {
        StringBuilder sb = new StringBuilder();
        for (char c : pattern.toCharArray()) {
            switch (c) {
                case '*': sb.append(".*"); break;
                case '?': sb.append('.'); break;
                case '.': sb.append("\\."); break;
                default:
                    if ("\\+()^${}|[]".indexOf(c) >= 0) sb.append('\\');
                    sb.append(c);
            }
        }
        return text.matches(sb.toString());
    }
}
