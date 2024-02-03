package org.by1337.blib;

import org.by1337.blib.chat.util.Message;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.factory.BukkitCommandRegisterFactory;
import org.by1337.blib.factory.PacketEntityFactory;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.inventory.FakeTitleFactory;
import org.by1337.blib.inventory.ItemStackSerialize;

import org.by1337.blib.util.AsyncCatcher;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public interface Api {
    @NotNull PacketEntityFactory getPacketEntityFactory();
    @NotNull Logger getLogger();
    @NotNull PacketFactory getPacketFactory();
    @NotNull CommandUtil getCommandUtil();
    @NotNull AsyncCatcher getAsyncCatcher();
    @NotNull Message getMessage();
    @NotNull ItemStackSerialize getItemStackSerialize();
    @NotNull FakeTitleFactory getFakeTitleFactory();
    @NotNull BukkitCommandRegister getBukkitCommandRegister();
}
