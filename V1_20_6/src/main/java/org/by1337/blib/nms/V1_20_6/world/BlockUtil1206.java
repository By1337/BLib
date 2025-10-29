package org.by1337.blib.nms.V1_20_6.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.V1_20_6.nbt.ParseCompoundTagV1206;
import org.by1337.blib.util.Version;
import org.by1337.blib.world.BlockUtil;
import org.jetbrains.annotations.Nullable;

@NMSAccessor(forClazz = BlockUtil.class, from = Version.V1_20_6, to = Version.V1_21_4)
public class BlockUtil1206 implements BlockUtil {
    private final ParseCompoundTag compoundTag = new ParseCompoundTagV1206();

    public @Nullable CompoundTag getBlockEntity(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        BlockEntity entity = cb.getCraftWorld().getHandle().getBlockEntity(cb.getPosition());
        if (entity == null) return null;
        net.minecraft.nbt.CompoundTag tag = entity.saveWithFullMetadata(MinecraftServer.getServer().registryAccess());
        return (CompoundTag) compoundTag.fromNMS(tag);
    }

    public void setBlockData(BlockData blockData, Location location, boolean applyPhysics) {
        location.getBlock().setBlockData(blockData, applyPhysics);
    }

    public void setBlockEntity(Location location, CompoundTag data) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        BlockEntity entity = cb.getCraftWorld().getHandle().getBlockEntity(cb.getPosition());
        if (entity == null) return;
        entity.loadWithComponents((net.minecraft.nbt.CompoundTag) compoundTag.toNMS(data), MinecraftServer.getServer().registryAccess());
    }

    @Override
    public BlockInfo getBlockInfo(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        int id = Block.BLOCK_STATE_REGISTRY.getId(cb.getNMS());
        CompoundTag data;
        BlockEntity entity = cb.getCraftWorld().getHandle().getBlockEntity(cb.getPosition());
        if (entity != null) {
            data = (CompoundTag) compoundTag.fromNMS(entity.saveWithFullMetadata(MinecraftServer.getServer().registryAccess()));
        } else {
            data = null;
        }
        return new BlockInfo(id, data);
    }

    public void clearClearable(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        BlockEntity entity = cb.getCraftWorld().getHandle().getBlockEntity(cb.getPosition());
        Clearable.tryClear(entity);
    }

    public int getBlockId(Location location) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        return Block.BLOCK_STATE_REGISTRY.getId(cb.getNMS());
    }

    public void setBlock(Location location, int blockId, @Nullable CompoundTag blockEntity, boolean applyPhysics) {
        CraftBlock cb = (CraftBlock) location.getBlock();
        if (setTypeAndData(cb, Block.BLOCK_STATE_REGISTRY.byId(blockId), applyPhysics)) {
            BlockEntity entity = cb.getCraftWorld().getHandle().getBlockEntity(cb.getPosition());
            if (entity != null && blockEntity != null) {
                entity.loadWithComponents((net.minecraft.nbt.CompoundTag) compoundTag.toNMS(blockEntity), MinecraftServer.getServer().registryAccess());
            }
        }
    }

    private boolean setTypeAndData(CraftBlock cb, net.minecraft.world.level.block.state.BlockState bs, boolean applyPhysics) {
        return CraftBlock.setTypeAndData(cb.getHandle(), cb.getPosition(), cb.getNMS(), bs, applyPhysics);
    }

}
