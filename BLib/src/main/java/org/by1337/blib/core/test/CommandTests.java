package org.by1337.blib.core.test;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.BLib;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.CommandException;
import org.by1337.blib.command.argument.ArgumentInteger;
import org.by1337.blib.command.argument.ArgumentString;
import org.by1337.blib.command.argument.ArgumentStrings;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.network.clientbound.entity.PacketAddEntity;
import org.by1337.blib.network.clientbound.entity.PacketSetEntityData;
import org.by1337.blib.util.Version;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.world.entity.PacketArmorStand;

import java.util.List;

public class CommandTests {

    public static Command<CommandSender> args() {
        return new Command<CommandSender>("ArgumentString")
                .argument(new ArgumentString<>("msg", List.of("test", "test test")))
                .argument(new ArgumentString<>("msg2", List.of("test", "test test")))
                .executor(((sender, args) -> {
                    BLib.getApi().getMessage().sendMsg(sender, (String) args.getOrDefault("msg", "123"));
                    BLib.getApi().getMessage().sendMsg(sender, (String) args.getOrDefault("msg2", "123"));
                }));
    }

    public static Command<CommandSender> msgTest() {
        return new Command<CommandSender>("msg")
                .argument(new ArgumentStrings<>("msg"))
                .executor(((sender, args) -> {
                    BLib.getApi().getMessage().sendMsg(sender, (String) args.getOrDefault("msg", "123"));
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

    @SuppressWarnings("removal")
    public static Command<CommandSender> packetArmorStandTest() {
        return new Command<CommandSender>("armor_stand_spawn_test")
                .argument(new ArgumentStrings<>("name"))
                .executor(((sender, args) -> {
                    if (!(sender instanceof Player player)) throw new CommandException("only for players!");
                    PacketArmorStand armorStand = BLib.createPacketEntity(new BLocation(player.getLocation()), PacketArmorStand.class);
                    armorStand.setCustomName(BLib.getApi().getMessage().messageBuilder((String) args.getOrDefault("name", "&cCustomArmorStand")));
                    armorStand.setCustomNameVisible(true);
                    armorStand.setSmall(true);
                    armorStand.setInvisible(true);
                    armorStand.setNoBasePlate(true);
                    armorStand.setSilent(true);
                    armorStand.setNoGravity(true);
                    armorStand.setMarker(true);
                    armorStand.setGlowing(true);
                    player.sendMessage("§fCreate PacketAddEntity");
                    PacketAddEntity packet = PacketAddEntity.newInstance(armorStand);
                    player.sendMessage("§fCreate PacketSetEntityData");
                    PacketSetEntityData packet1 = PacketSetEntityData.newInstance(armorStand);
                    player.sendMessage("§fSend PacketAddEntity");
                    packet.send(player);
                    player.sendMessage("§fSend PacketSetEntityData");
                    packet1.send(player);
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
