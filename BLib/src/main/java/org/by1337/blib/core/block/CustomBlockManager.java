package org.by1337.blib.core.block;

import org.by1337.blib.block.custom.data.CustomBlockData;
import org.by1337.blib.block.custom.registry.WorldRegistry;
import org.by1337.blib.core.BLib;
import org.by1337.blib.nbt.DefaultNbtByteBuffer;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.nbt.impl.ListNBT;
import org.by1337.blib.util.Pair;
import org.by1337.blib.world.BlockPosition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CustomBlockManager {
    public void save() {
        ListNBT listNBT = new ListNBT();
        for (List<Pair<BlockPosition, CustomBlockData>> value : WorldRegistry.CUSTOM_BLOCK_REGISTRY.getAll().values()) {
            for (Pair<BlockPosition, CustomBlockData> pair : value) {
                listNBT.add(pair.getRight().getData());
            }
        }
        try {
            File file = new File(BLib.getInstance().getDataFolder(), "customBlocks.bnbt");
            DefaultNbtByteBuffer buffer = new DefaultNbtByteBuffer();
            listNBT.write(buffer);

            Files.write(file.toPath(), buffer.toByteArray());
        } catch (IOException e) {
            org.by1337.blib.BLib.getApi().getMessage().error("Failed to save custom blocks!", e);
        }
    }
    public void load() {
        try {
            File file = new File(BLib.getInstance().getDataFolder(), "customBlocks.bnbt");
            if (!file.exists()) return;
            DefaultNbtByteBuffer buffer = new DefaultNbtByteBuffer(Files.readAllBytes(file.toPath()));

            ListNBT listNBT = (ListNBT) NbtType.LIST.read(buffer);

            for (NBT nbt : listNBT) {
                CompoundTag compoundTag = (CompoundTag) nbt;
                CustomBlockData data = new CustomBlockData(compoundTag);
                WorldRegistry.CUSTOM_BLOCK_REGISTRY.add(data.getWorld(), data.getBlockX(), data.getBlockY(), data.getBlockZ(), data);
            }

        } catch (IOException e) {
            org.by1337.blib.BLib.getApi().getMessage().error("Failed to load custom blocks!", e);
        }
    }
}
