package org.by1337.lib.test;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.by1337.api.BLib;
import org.by1337.api.command.argument.ArgumentStrings;
import org.by1337.api.network.clientbound.entity.PacketAddEntity;
import org.by1337.api.network.clientbound.entity.PacketSetEntityData;
import org.by1337.api.world.BLocation;
import org.by1337.api.world.entity.PacketArmorStand;
import org.by1337.api.util.Version;
import org.by1337.api.command.Command;
import org.by1337.api.command.CommandException;
import org.by1337.api.command.argument.ArgumentInteger;
import org.by1337.api.hologaram.Hologram;
import org.by1337.api.util.CyclicList;

import java.util.List;

public class CommandTests {

    public static Command<CommandSender> msgTest() {
        return new Command<CommandSender>("msg")
                .argument(new ArgumentStrings<>("name"))
                .executor(((sender, args) -> {
                   BLib.getApi().getMessage().sendMsg(sender, (String) args.getOrDefault("name", "123"));
                }));
    }
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

}
