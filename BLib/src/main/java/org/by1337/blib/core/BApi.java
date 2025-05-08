package org.by1337.blib.core;

import org.by1337.blib.Api;
import org.by1337.blib.block.replacer.PooledBlockReplacer;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.core.command.BCommandUtil;
import org.by1337.blib.core.factory.AbstractPacketFactory;
import org.by1337.blib.core.factory.BBukkitCommandRegisterFactory;
import org.by1337.blib.core.factory.BPacketEntityFactory;
import org.by1337.blib.core.factory.BlockReplacerFactory;
import org.by1337.blib.core.inventory.ItemStackSerializeFactory;
import org.by1337.blib.core.nbt.ParseCompoundTagManager;
import org.by1337.blib.core.text.ComponentToANSIImpl;
import org.by1337.blib.core.text.minimessage.LegacyConvertorImpl;
import org.by1337.blib.core.text.minimessage.NativeLegacyConvertor;
import org.by1337.blib.core.unsafe.BLibUnsafeImpl;
import org.by1337.blib.factory.PacketEntityFactory;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.inventory.FakeTitleFactory;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nms.V1_20_6.inventory.InventoryUtilV1_20_6;
import org.by1337.blib.nms.V1_21.inventory.InventoryUtilV1_21;
import org.by1337.blib.nms.V1_21.inventory.InventoryUtilV1_21_1;
import org.by1337.blib.nms.V1_21_3.inventory.InventoryUtilV1_21_3;
import org.by1337.blib.nms.V1_21_4.inventory.InventoryUtilV1_21_4;
import org.by1337.blib.nms.V1_21_5.inventory.InventoryUtilV1_21_5;
import org.by1337.blib.nms.v1_16_5.inventory.InventoryUtilV1_16_5;
import org.by1337.blib.nms.v1_17_1.inventory.InventoryUtilV1_17_1;
import org.by1337.blib.nms.v1_18_2.inventory.InventoryUtilV1_18_2;
import org.by1337.blib.nms.v1_19_4.inventory.InventoryUtilV1_19_4;
import org.by1337.blib.nms.v1_20_1.inventory.InventoryUtilV1_20_1;
import org.by1337.blib.nms.v1_20_2.inventory.InventoryUtilV1_20_2;
import org.by1337.blib.nms.v1_20_4.inventory.InventoryUtilV1_20_4;
import org.by1337.blib.text.ComponentToANSI;
import org.by1337.blib.text.LegacyConvertor;
import org.by1337.blib.translation.Translation;
import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.util.AsyncCatcher;
import org.by1337.blib.util.Version;
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
    private static final CommandUtil commandUtil = new BCommandUtil();
    private final Message message;
    private final AsyncCatcher asyncCatcher = new AsyncCatcher() {
        @Override
        public void catchOp(@NotNull String identifier) {
            AsyncCatcher.super.catchOp(identifier);
        }
    };
    private final ItemStackSerialize itemStackSerialize = ItemStackSerializeFactory.create();
    private final InventoryUtil inventoryUtil = switch (Version.VERSION) {
        case V1_16_5 -> new InventoryUtilV1_16_5();
        case V1_17_1 -> new InventoryUtilV1_17_1();
        case V1_18_2 -> new InventoryUtilV1_18_2();
        case V1_19_4 -> new InventoryUtilV1_19_4();
        case V1_20_1 -> new InventoryUtilV1_20_1();
        case V1_20_2 -> new InventoryUtilV1_20_2();
        case V1_20_4, V1_20_3 -> new InventoryUtilV1_20_4();
        case V1_20_5, V1_20_6 -> new InventoryUtilV1_20_6();
        case V1_21 -> new InventoryUtilV1_21();
        case V1_21_1 -> new InventoryUtilV1_21_1();
        case V1_21_3 -> new InventoryUtilV1_21_3();
        case V1_21_4 -> new InventoryUtilV1_21_4();
        case V1_21_5 -> new InventoryUtilV1_21_5();
        default -> throw new IllegalStateException("Unsupported version: " + Version.VERSION);
    };
    @Deprecated
    private final FakeTitleFactory fakeTitleFactory = () -> inventoryUtil::sendFakeTitle;

    private final BukkitCommandRegister register = new BBukkitCommandRegisterFactory().create();
    private final LegacyConvertor legacyConvertor;
    private final ComponentToANSIImpl componentToANSI = new ComponentToANSIImpl();
    private final PooledBlockReplacer pooledBlockReplacer;

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
                BlockReplacerFactory.create(),
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
}
