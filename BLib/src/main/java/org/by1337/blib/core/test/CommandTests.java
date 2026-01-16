package org.by1337.blib.core.test;

import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.BLib;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.argument.*;
import org.by1337.blib.configuration.YamlOps;
import org.by1337.blib.configuration.YamlValue;
import org.by1337.blib.configuration.serialization.BukkitCodecs;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.util.Version;

import java.util.List;

public class CommandTests {

    public static Command<CommandSender> args() {
        return new Command<CommandSender>("args")
                .addSubCommand(new Command<CommandSender>("ArgumentString")
                        .argument(new ArgumentString<>("msg", List.of("test", "test test")))
                        .argument(new ArgumentString<>("msg2", List.of("test", "test test")))
                        .executor(((sender, args) -> {
                            BLib.getApi().getMessage().sendMsg(sender, (String) args.getOrDefault("msg", "null"));
                            BLib.getApi().getMessage().sendMsg(sender, (String) args.getOrDefault("msg2", "null"));
                        }))
                )
                .addSubCommand(new Command<CommandSender>("ArgumentLookingAtBlock")
                        .argument(new ArgumentLookingAtBlock<>("pos"))
                        .argument(new ArgumentLookingAtBlock<>("pos1"))
                        .executor(((sender, args) -> {
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("pos", "null").toString());
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("pos1", "null").toString());
                        }))
                )
                .addSubCommand(new Command<CommandSender>("ArgumentFormattedDouble")
                        .argument(new ArgumentFormattedDouble<>("d", List.of("10.5K")))
                        .argument(new ArgumentFormattedDouble<>("d1", List.of("10.5K")))
                        .executor(((sender, args) -> {
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("d", "null").toString());
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("d1", "null").toString());
                        }))
                )
                .addSubCommand(new Command<CommandSender>("ArgumentSound")
                        .argument(new ArgumentSound<>("s"))
                        .argument(new ArgumentSound<>("s1"))
                        .executor(((sender, args) -> {
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("s", "null").toString());
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("s1", "null").toString());
                        }))
                )
                .addSubCommand(new Command<CommandSender>("legacy_sound")
                        .argument(new ArgumentEnumValue<>("s", Sound.class))
                        .argument(new ArgumentEnumValue<>("s1", Sound.class))
                        .executor(((sender, args) -> {
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("s", "null").toString());
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("s1", "null").toString());
                        }))
                )
                .addSubCommand(new Command<CommandSender>("Registry#MATERIAL")
                        .argument(new ArgumentRegistry<>("v", Registry.MATERIAL))
                        .argument(new ArgumentRegistry<>("v1", Registry.MATERIAL))
                        .executor(((sender, args) -> {
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("v", "null").toString());
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("v1", "null").toString());
                        }))
                )
                .addSubCommand(new Command<CommandSender>("OldEnum")
                        .argument(new ArgumentEnumValue<>("s", Sound.class))
                        .argument(new ArgumentEnumValue<>("s1", Sound.class))
                        .executor(((sender, args) -> {
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("s", "null").toString());
                            BLib.getApi().getMessage().sendMsg(sender, args.getOrDefault("s1", "null").toString());
                        }))
                )
                .addSubCommand(new Command<CommandSender>("codec")
                        .executor(((sender, args) -> {
                            System.out.println(BukkitCodecs.BIOME.read(YamlOps.INSTANCE, YamlValue.wrap("NETHER_WASTES")));
                        }))
                )
                ;
    }

    public static Command<CommandSender> msgTest() {
        return new Command<CommandSender>("msg")
                .argument(new ArgumentStrings<>("msg"))
                .executor(((sender, args) -> {
                    BLib.getApi().getMessage().sendMsg(sender, (String) args.getOrDefault("msg", "123"));
                }));
    }

    public static Command<CommandSender> gc() {
        return new Command<CommandSender>("gc")
                .executor(((sender, args) -> {
                    System.gc();
                }));
    }

    public static Command<CommandSender> logTest() {
        return new Command<CommandSender>("log")
                .argument(new ArgumentStrings<>("msg"))
                .executor(((sender, args) -> {
                    BLib.getApi().getMessage().log(
                            BLib.getApi().getMessage().componentBuilder((String) args.getOrDefault("msg", "123"))
                    );
                }));
    }

    public static Command<CommandSender> sysInfo() {
        return new Command<CommandSender>("sys_info")
                .executor(((sender, args) -> {
                    sender.sendMessage(
                            "§fVersion info: Bukkit.getVersion()='§7" + Bukkit.getVersion() +
                                    "§f', Bukkit.getBukkitVersion()='§7" + Bukkit.getBukkitVersion() +
                                    "§f', Bukkit.getServer().getClass().getPackage().getName()='§7" + Bukkit.getServer().getClass().getPackage().getName() + "§f'"
                    );
                    sender.sendMessage("§fDetected version='§f" + Version.VERSION + "§f'");

                    sender.sendMessage("§fos.name='§7" + System.getProperty("os.name") + "§f'");
                    sender.sendMessage("§fjava.version='§7" + System.getProperty("java.version") + "§f'");
                    sender.sendMessage("gameVersion='§7" + Version.getGameVersion() + "'");
                }));
    }

    public static Command<CommandSender> sleep() {
        return new Command<CommandSender>("sleep")
                .argument(new ArgumentInteger<>("time", 0))
                .executor(((sender, args) -> {
                    int time = (int) args.get("time");
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    public static Command<CommandSender> itemClone() {
        return new Command<CommandSender>("cloneItem")
                .requires((sender -> sender instanceof Player))
                .requires(sender -> !((Player) sender).getInventory().getItemInMainHand().getType().isEmpty())
                .executor(((sender, args) -> {
                    ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();
                    sender.sendMessage("save to nbt...");
                    CompoundTag compoundTag = BLib.getApi().getParseCompoundTag().copy(itemStack);
                    System.out.println(compoundTag);
                    sender.sendMessage("save to nbt done!");
                    sender.sendMessage("deserialize from nbt...");
                    ((Player) sender).getWorld().dropItemNaturally(((Player) sender).getLocation(),
                            BLib.getApi().getParseCompoundTag().create(compoundTag)
                    );
                    sender.sendMessage("deserialize from nbt done!");

                    sender.sendMessage("save to string...");
                    String s = BLib.getApi().getItemStackSerialize().serialize(itemStack);
                    System.out.println(s);
                    sender.sendMessage("save to string done!");
                    sender.sendMessage("deserialize from string...");
                    ((Player) sender).getWorld().dropItemNaturally(((Player) sender).getLocation(),
                            BLib.getApi().getItemStackSerialize().deserialize(s)
                    );
                    sender.sendMessage("deserialize from string done!");

                }));

    }

}
