package org.by1337.blib.core;

import org.by1337.blib.Api;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.core.factory.BBukkitCommandRegisterFactory;
import org.by1337.blib.core.nbt.ParseCompoundTagManager;
import org.by1337.blib.core.text.LegacyConvertorImpl;
import org.by1337.blib.factory.PacketEntityFactory;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.inventory.FakeTitleFactory;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.text.LegacyConvertor;
import org.by1337.blib.util.AsyncCatcher;
import org.by1337.blib.core.command.BCommandUtil;
import org.by1337.blib.core.factory.AbstractPacketFactory;
import org.by1337.blib.core.factory.BPacketEntityFactory;
import org.by1337.blib.core.inventory.FakeTitleFactoryImpl;
import org.by1337.blib.core.inventory.ItemStackSerializeFactory;
import org.by1337.blib.nms.v1_16_5.AsyncCatcherImpl;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class BApi implements Api {
    private static final BPacketEntityFactory entityFactory = new BPacketEntityFactory();
    private static final PacketFactory packetFactory = AbstractPacketFactory.create();
    private static final CommandUtil commandUtil = new BCommandUtil();
    private final Message message;
    private final AsyncCatcher asyncCatcher;
    private final ItemStackSerialize itemStackSerialize = ItemStackSerializeFactory.create();
    private final FakeTitleFactory fakeTitleFactory = new FakeTitleFactoryImpl();
    private final BukkitCommandRegister register = new BBukkitCommandRegisterFactory().create();
    private final LegacyConvertor legacyConvertor = new LegacyConvertorImpl();

    public BApi() {
        message = new Message(BLib.getInstance().getLogger());
        asyncCatcher = new AsyncCatcherImpl();
    }

    @Override
    public @NotNull PacketEntityFactory getPacketEntityFactory() {
        return entityFactory;
    }

    @Override
    public @NotNull Logger getLogger() {
        return BLib.getInstance().getLogger();
    }

    @Override
    public @NotNull PacketFactory getPacketFactory() {
        return packetFactory;
    }

    @Override
    public @NotNull CommandUtil getCommandUtil() {
        return commandUtil;
    }

    @Override
    public @NotNull AsyncCatcher getAsyncCatcher() {
        return asyncCatcher;
    }

    @Override
    public @NotNull Message getMessage() {
        return message;
    }

    @Override
    public @NotNull ItemStackSerialize getItemStackSerialize() {
        return itemStackSerialize;
    }

    @Override
    public @NotNull FakeTitleFactory getFakeTitleFactory() {
        return fakeTitleFactory;
    }

    @Override
    public @NotNull BukkitCommandRegister getBukkitCommandRegister() {
        return register;
    }

    @Override
    public @NotNull ParseCompoundTag getParseCompoundTag() {
        return ParseCompoundTagManager.get();
    }

    @Override
    public @NotNull LegacyConvertor getLegacyConvertor() {
        return legacyConvertor;
    }
}
