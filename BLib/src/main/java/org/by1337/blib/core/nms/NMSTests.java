package org.by1337.blib.core.nms;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.command.SummonCommand;
import org.by1337.blib.core.nms.verify.NMSVerify;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.inventory.LegacyFastItemMutator;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.registry.RegistryCreator;
import org.by1337.blib.util.Version;
import org.by1337.blib.world.BlockUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;

public class NMSTests {
    private static final Logger LOGGER = LoggerFactory.getLogger("BLib#NMSTests");

    public static String run(Player who, Version version) {
        NmsFactory factory = NmsFactory.get();

        StringBuilder report = new StringBuilder();

        report.append("NMS version: ").append(version).append("\n");
        report.append("Server version: ").append(Version.VERSION).append("\n");


        run(() -> {
            report.append("Test SummonCommand\n");
            SummonCommand summonCommand = factory.get(SummonCommand.class, version);
            if (summonCommand == null) {
                report.append("SummonCommand impl not found\n");
                return;
            }
            report.append("SummonCommand impl: ").append(summonCommand.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(summonCommand.getClass());
            report.append("init only\n");
        }, report);
        run(() -> {
            report.append("Test BukkitCommandRegister\n");
            BukkitCommandRegister register = factory.get(BukkitCommandRegister.class, version);
            if (register == null) {
                report.append("BukkitCommandRegister impl not found\n");
                return;
            }
            report.append("BukkitCommandRegister impl: ").append(register.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(register.getClass());
            AtomicBoolean done = new AtomicBoolean(false);
            BukkitCommand cmd = new BukkitCommand("blib_test_command") {
                @Override
                public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                    done.set(true);
                    return true;
                }
            };
            register.register(cmd);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "blib_test_command");
            if (!done.get()) {
                report.append("Command not registered\n");
            } else {
                report.append("Command registered\n");
            }
            register.unregister(cmd);
        }, report);

        run(() -> {
            report.append("Test ParseCompoundTag\n");
            ParseCompoundTag pdc = factory.get(ParseCompoundTag.class, version);
            if (pdc == null) {
                report.append("ParseCompoundTag impl not found\n");
                return;
            }
            report.append("ParseCompoundTag impl: ").append(pdc.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(pdc.getClass());
            ItemStack itemStack = new ItemStack(Material.ACACIA_BOAT);
            if (!itemStack.equals(pdc.create(pdc.copy(itemStack)))) {
                throw new IllegalStateException("Failed to test ParseCompoundTag#create & ParseCompoundTag#copy");
            }
            var c = pdc.copy(itemStack);
            if (!pdc.fromNMS(pdc.toNMS(c)).equals(c)) {
                throw new IllegalStateException("Failed to test ParseCompoundTag#fromNMS & ParseCompoundTag#toNMS");
            }
        }, report);


        run(() -> {
            report.append("Test ItemStackSerialize\n");
            ItemStackSerialize serialize = factory.get(ItemStackSerialize.class, version);
            if (serialize == null) {
                report.append("ItemStackSerialize impl not found\n");
                return;
            }
            report.append("ItemStackSerialize impl: ").append(serialize.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(serialize.getClass());
            ItemStack itemStack = new ItemStack(Material.ACACIA_BOAT);
            if (!serialize.deserialize(serialize.serialize(itemStack)).equals(itemStack)) {
                throw new IllegalStateException("Failed to test ItemStackSerialize#serialize & ItemStackSerialize#deserialize");
            }
        }, report);

        run(() -> {
            report.append("Test InventoryUtil\n");
            InventoryUtil invUtil = factory.get(InventoryUtil.class, version);
            if (invUtil == null) {
                report.append("InventoryUtil impl not found\n");
                return;
            }
            report.append("InventoryUtil impl: ").append(invUtil.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(invUtil.getClass());
            report.append("init only\n");
        }, report);


        run(() -> {
            report.append("Test BlockReplacer\n");
            BlockReplacer replacer = factory.get(BlockReplacer.class, version);
            if (replacer == null) {
                report.append("BlockReplacer impl not found\n");
                return;
            }
            report.append("BlockReplacer impl: ").append(replacer.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(replacer.getClass());
            report.append("init only\n");
        }, report);


        run(() -> {
            report.append("Test BlockUtil\n");
            BlockUtil blockUtil = factory.get(BlockUtil.class, version);
            if (blockUtil == null) {
                report.append("BlockUtil impl not found\n");
                return;
            }
            report.append("BlockUtil impl: ").append(blockUtil.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(blockUtil.getClass());
            blockUtil.setBlockData(Material.STONE.createBlockData(), who.getLocation(), false);
            blockUtil.setBlockData(Material.AIR.createBlockData(), who.getLocation(), false);
        }, report);

        run(() -> {
            report.append("Test LegacyFastItemMutator\n");
            LegacyFastItemMutator legacyFastItemMutator = factory.get(LegacyFastItemMutator.class, version);
            if (legacyFastItemMutator == null) {
                report.append("LegacyFastItemMutator impl not found\n");
                return;
            }
            report.append("LegacyFastItemMutator impl: ").append(legacyFastItemMutator.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(legacyFastItemMutator.getClass());
            report.append("init only\n");
        }, report);


        run(() -> {
            report.append("Test RegistryCreator\n");
            RegistryCreator registryCreator = factory.get(RegistryCreator.class, version);
            if (registryCreator == null) {
                report.append("RegistryCreator impl not found\n");
                return;
            }
            report.append("RegistryCreator impl: ").append(registryCreator.getClass().getCanonicalName()).append("\n");
            new NMSVerify().verify(registryCreator.getClass());
            if (registryCreator.createPotionType() == null) {
                report.append("Failed RegistryCreator#createPotionType\n");
            }
            if (registryCreator.createParticleType() == null) {
                report.append("Failed RegistryCreator#createParticleType\n");
            }
        }, report);

        return report.toString();
    }

    private static void run(ThrowingRunnable runnable, StringBuilder report) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            report.append("Exception: \n");
            StringWriter writer = new StringWriter();
            throwable.printStackTrace(new PrintWriter(writer));
            report.append(writer).append("\n");
            return;
        }
    }

    private interface ThrowingRunnable {
        void run() throws Throwable;
    }
}