package org.by1337.blib.nms.v1_16_5.world;

import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.v1_16_5.nbt.ParseCompoundTagV1165;
import org.by1337.blib.util.Version;
import org.by1337.blib.world.BlockUtil;
import org.jetbrains.annotations.Nullable;

@NMSAccessor(forClazz = BlockUtil.class, forVersions = Version.V1_16_5)
public class BlockUtil165 implements BlockUtil {

    public @Nullable CompoundTag getBlockEntity(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        BlockEntity entity = cb.getCraftWorld().getHandle().getTileEntity(cb.getPosition());
        if (entity == null) return null;
        net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
        entity.save(tag);
        return (CompoundTag) ParseCompoundTagV1165.convertFromNms(tag);
    }

    public void setBlockData(BlockData blockData, Location location, boolean applyPhysics) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        cb.setTypeAndData(((CraftBlockData)blockData).getState(), applyPhysics);
    }

    public void setBlockEntity(Location location, CompoundTag data) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        BlockEntity entity = cb.getCraftWorld().getHandle().getTileEntity(cb.getPosition());
        if (entity == null) return;
        entity.load(cb.getNMS(), (net.minecraft.nbt.CompoundTag) ParseCompoundTagV1165.convert(data));
    }

    @Override
    public BlockInfo getBlockInfo(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        int id = Block.REGISTRY_ID.getId(cb.getNMS());
        CompoundTag data;
        BlockEntity entity = cb.getCraftWorld().getHandle().getTileEntity(cb.getPosition());
        if (entity != null) {
            data = (CompoundTag) ParseCompoundTagV1165.convertFromNms(entity.save(new net.minecraft.nbt.CompoundTag()));
        } else {
            data = null;
        }
        return new BlockInfo(id, data);
    }

    public void clearClearable(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        BlockEntity entity = cb.getCraftWorld().getHandle().getTileEntity(cb.getPosition());
        Clearable.tryClear(entity);
    }

    public int getBlockId(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        return Block.REGISTRY_ID.getId(cb.getNMS());
    }

    public void setBlock(Location location, int blockId, @Nullable CompoundTag blockEntity, boolean applyPhysics) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        if (cb.setTypeAndData(Block.REGISTRY_ID.fromId(blockId), applyPhysics)) {
            BlockEntity entity = cb.getCraftWorld().getHandle().getTileEntity(cb.getPosition());
            if (entity != null && blockEntity != null) {
                entity.load(cb.getNMS(), (net.minecraft.nbt.CompoundTag) ParseCompoundTagV1165.convert(blockEntity));
                if (!entity.getPosition().equals(cb.getPosition())) {
                    entity.setPosition(cb.getPosition());
                }
            }
        }
    }
}
