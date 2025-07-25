package org.by1337.blib;

import org.by1337.blib.block.replacer.PooledBlockReplacer;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.factory.PacketEntityFactory;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.inventory.FakeTitleFactory;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.registry.RegistryCreator;
import org.by1337.blib.text.ComponentToANSI;
import org.by1337.blib.text.LegacyConvertor;
import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.util.AsyncCatcher;
import org.by1337.blib.world.BlockUtil;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@SuppressWarnings({"removal"})
public interface Api {
    @NotNull PacketEntityFactory getPacketEntityFactory();

    @NotNull Logger getLogger();

    @NotNull PacketFactory getPacketFactory();

    @NotNull
    default CommandUtil getCommandUtil() {
        return CommandUtil.INSTANCE;
    }

    @NotNull AsyncCatcher getAsyncCatcher();

    @NotNull Message getMessage();

    @NotNull ItemStackSerialize getItemStackSerialize();

    @Deprecated
    default @NotNull FakeTitleFactory getFakeTitleFactory() {
        return () -> getInventoryUtil()::sendFakeTitle;
    }

    @NotNull BukkitCommandRegister getBukkitCommandRegister();

    @NotNull ParseCompoundTag getParseCompoundTag();

    @NotNull LegacyConvertor getLegacyConvertor();

    @NotNull ComponentToANSI getComponentToANSI();

    @NotNull PooledBlockReplacer getPooledBlockReplacer();

    @NotNull BLibUnsafe getUnsafe();

    @NotNull InventoryUtil getInventoryUtil();

    @NotNull RegistryCreator getRegistryCreator();

    //@NotNull BlockUtil getBlockUtil();
}
