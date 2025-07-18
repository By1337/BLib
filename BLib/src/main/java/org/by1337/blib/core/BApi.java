package org.by1337.blib.core;

import org.by1337.blib.Api;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.PooledBlockReplacer;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.core.factory.AbstractPacketFactory;
import org.by1337.blib.core.factory.BPacketEntityFactory;
import org.by1337.blib.core.nms.NmsFactory;
import org.by1337.blib.core.text.ComponentToANSIImpl;
import org.by1337.blib.core.text.minimessage.LegacyConvertorImpl;
import org.by1337.blib.core.text.minimessage.NativeLegacyConvertor;
import org.by1337.blib.core.unsafe.BLibUnsafeImpl;
import org.by1337.blib.factory.PacketEntityFactory;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.registry.RegistryCreator;
import org.by1337.blib.text.ComponentToANSI;
import org.by1337.blib.text.LegacyConvertor;
import org.by1337.blib.translation.Translation;
import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.util.AsyncCatcher;
import org.by1337.blib.world.BlockUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class BApi implements Api {
    private static final BPacketEntityFactory entityFactory = new BPacketEntityFactory();
    private static final PacketFactory packetFactory = AbstractPacketFactory.create();
    private final Message message;
    private final AsyncCatcher asyncCatcher = new AsyncCatcher() {
        @Override
        public void catchOp(@NotNull String identifier) {
            AsyncCatcher.super.catchOp(identifier);
        }
    };
    private final ItemStackSerialize itemStackSerialize = NmsFactory.get().get(ItemStackSerialize.class);
    private final InventoryUtil inventoryUtil = NmsFactory.get().get(InventoryUtil.class);

    private final BukkitCommandRegister register = NmsFactory.get().get(BukkitCommandRegister.class);
    private final LegacyConvertor legacyConvertor;
    private final ComponentToANSIImpl componentToANSI = new ComponentToANSIImpl();
    private final PooledBlockReplacer pooledBlockReplacer;
    private final ParseCompoundTag parseCompoundTag = NmsFactory.get().get(ParseCompoundTag.class);
    private final RegistryCreator registryCreator = NmsFactory.get().get(RegistryCreator.class);
    private final BlockUtil blockUtil = null;//NmsFactory.get().get(BlockUtil.class);

    public BApi() {
        if (NativeLegacyConvertor.isAvailable()) {
            legacyConvertor = new NativeLegacyConvertor();
        } else {
            legacyConvertor = new LegacyConvertorImpl();
        }
        File file = new File(BLib.getInstance().getDataFolder(), "translation.json");
        if (!file.exists()) {
            BLib.getInstance().saveResource("translation.json", false);
        }
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            message = new Message(BLib.getInstance().getLogger(), reader);
            Translation translation = message.getTranslation();
            try (var in = new InputStreamReader(BLib.getInstance().getResource("translation.json"), StandardCharsets.UTF_8)) {
                translation.saveDefaults(in);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pooledBlockReplacer = new PooledBlockReplacer(
                BLib.getInstance(),
                15,
                NmsFactory.get().get(BlockReplacer.class),
                message
        );
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
    @Deprecated
    public @NotNull PacketFactory getPacketFactory() {
        return packetFactory;
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
    public @NotNull BukkitCommandRegister getBukkitCommandRegister() {
        return register;
    }


    @Override
    public @NotNull ParseCompoundTag getParseCompoundTag() {
        return parseCompoundTag;
    }

    @Override
    public @NotNull LegacyConvertor getLegacyConvertor() {
        return legacyConvertor;
    }

    @Override
    public @NotNull ComponentToANSI getComponentToANSI() {
        return componentToANSI;
    }

    @Override
    public @NotNull PooledBlockReplacer getPooledBlockReplacer() {
        return pooledBlockReplacer;
    }

    @Override
    public @NotNull BLibUnsafe getUnsafe() {
        return BLibUnsafeImpl.INSTANCE;
    }

    @Override
    public @NotNull InventoryUtil getInventoryUtil() {
        return inventoryUtil;
    }


    @Override
    public @NotNull RegistryCreator getRegistryCreator() {
        return registryCreator;
    }


    //@Override
    public @NotNull BlockUtil getBlockUtil() {
        return blockUtil;
    }
}
