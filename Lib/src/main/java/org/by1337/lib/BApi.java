package org.by1337.lib;

import org.by1337.api.Api;
import org.by1337.api.chat.util.Message;
import org.by1337.api.command.CommandUtil;
import org.by1337.api.factory.PacketEntityFactory;
import org.by1337.api.factory.PacketFactory;
import org.by1337.api.inventory.FakeTitleFactory;
import org.by1337.api.inventory.ItemStackSerialize;
import org.by1337.api.util.AsyncCatcher;
import org.by1337.lib.command.BCommandUtil;
import org.by1337.lib.factory.AbstractPacketFactory;
import org.by1337.lib.factory.BPacketEntityFactory;
import org.by1337.lib.inventory.FakeTitleFactoryImpl;
import org.by1337.lib.inventory.ItemStackSerializeFactory;
import org.by1337.v1_16_5.AsyncCatcherImpl;
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
}
