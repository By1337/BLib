package org.by1337.blib.world;

import dev.by1337.core.BCore;
import dev.by1337.core.bridge.world.BlockEntityUtil;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.BLib;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.jetbrains.annotations.Nullable;

public interface BlockUtil {
     @Nullable CompoundTag getBlockEntity(Location location);

    void setBlockEntity(Location location, CompoundTag data);

    void clearClearable(Location location);

    int getBlockId(Location location);

    default void setBlock(Location location, BlockInfo blockInfo, boolean applyPhysics) {
        setBlock(location, blockInfo.id, blockInfo.blockEntity, applyPhysics);
    }

    void setBlock(Location location, int blockId, @Nullable CompoundTag blockEntity, boolean applyPhysics);

    BlockInfo getBlockInfo(Location location);

    void setBlockData(BlockData blockData, Location location, boolean applyPhysics);

    record BlockInfo(int id, @Nullable CompoundTag blockEntity) {
        public void place(Location location, boolean applyPhysics) {
           // BLib.getApi().getBlockUtil().setBlock(location, this, applyPhysics);
        }
    }
}
