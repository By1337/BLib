package org.by1337.api;

import org.by1337.api.chat.util.Message;
import org.by1337.api.command.CommandUtil;
import org.by1337.api.factory.PacketEntityFactory;
import org.by1337.api.factory.PacketFactory;
import org.by1337.api.inventory.FakeTitleFactory;
import org.by1337.api.inventory.ItemStackSerialize;
import org.by1337.api.util.AsyncCatcher;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public interface Api {
    @NotNull PacketEntityFactory getPacketEntityFactory();
    @NotNull Logger getLogger();
    @NotNull PacketFactory getPacketFactory();
    @NotNull CommandUtil getCommandUtil();
    @NotNull AsyncCatcher getAsyncCatcher();
    @NotNull Message getMessage();
    @NotNull ItemStackSerialize  getItemStackSerialize();
    @NotNull FakeTitleFactory getFakeTitleFactory();
}
