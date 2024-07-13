package org.by1337.blib.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.custom.impl.CustomBlockExample;
import org.by1337.blib.block.custom.listener.CustomBlockListener;
import org.by1337.blib.block.custom.registry.BlockRegistry;
import org.by1337.blib.block.custom.registry.WorldRegistry;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.CommandException;
import org.by1337.blib.command.argument.ArgumentBoolean;
import org.by1337.blib.command.argument.ArgumentSetList;
import org.by1337.blib.command.requires.RequiresPermission;
import org.by1337.blib.core.block.CustomBlockManager;
import org.by1337.blib.core.fastutil.FastUtilCommands;
import org.by1337.blib.core.test.CommandTests;
import org.by1337.blib.geom.IntAABB;
import org.by1337.blib.geom.Sphere;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.translation.Translation;
import org.by1337.blib.util.SpacedNameKey;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class BLib extends JavaPlugin {
    @Getter
    private static Plugin instance;
    private BApi api;
    public static boolean DEBUG = false;
    private CustomBlockManager customBlockManager;

    @Override
    public void onLoad() {
        instance = this;
        api = new BApi();
        org.by1337.blib.BLib.setApi(api);
        setCommand();
        customBlockManager = new CustomBlockManager();
    }

    @Override
    public void onEnable() {
        init();
        getCommand("blib").setExecutor(this);
        getCommand("blib").setTabCompleter(this);
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
    }

    private void init() {
        Lang.loadTranslations(api.getMessage().getTranslation());
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
                ).addSubCommand(new Command<CommandSender>("прикол")

                        .executor(((sender, args) -> {
                            Player player = (Player) sender;
                            Sphere sphere = new Sphere(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10);
                            IntAABB intAABB = IntAABB.fromAABB(sphere.toAABB());
                            List<Vec3i> blocks = new ArrayList<>();
                            Vector playerDirection = player.getLocation().getDirection();
                            Random random = new Random();
                            for (Vec3i vec3i : intAABB.getAllPointsInAABB()) {
                                double distanceSquared = sphere.getCenter().distanceSquared(vec3i.toVec3d());
                                if (distanceSquared <= 10 * 10 && distanceSquared >= 9 * 9) {
                                    Vector blockVector = new Vector(vec3i.getX() - player.getLocation().getX(),
                                            vec3i.getY() - player.getLocation().getY(),
                                            vec3i.getZ() - player.getLocation().getZ());
                                    blockVector.normalize();
                                    double angle = playerDirection.angle(blockVector);
                                    double fov = Math.toRadians(70);

                                    if (angle <= fov) {
                                        blocks.add(vec3i);
                                    }
                                }
                            }
                            for (Vec3i block : blocks) {
                                player.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).setType(
                                        random.nextBoolean() ? Material.OBSIDIAN :  Material.CRYING_OBSIDIAN
                                );
                            }
                        }))
                )
                .addSubCommand(new Command<CommandSender>("cb")
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
                                .requires(sender -> sender instanceof Player)
                                .argument(new ArgumentSetList<>("id", () -> BlockRegistry.get().getAll().stream().map(c -> c.getId().toString()).toList()))
                                .executor(((sender, args) -> {
                                    String id = (String) args.getOrThrow("id", "/blib cb give <id>");
                                    SpacedNameKey spacedNameKey = new SpacedNameKey(id);
                                    CustomBlock customBlock = BlockRegistry.get().getCustomBlock(spacedNameKey);
                                    ((Player) sender).getInventory().addItem(customBlock.getItem()).forEach((slot, i) -> {
                                        ((Player) sender).getWorld().dropItemNaturally(((Player) sender).getLocation(), i);
                                    });
                                }))
                        )
                )
                .executor((sender, args) -> {
                    api.getMessage().sendTranslatable(sender, "hello-message", getDescription().getVersion());
                })
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

        );

    }
}
