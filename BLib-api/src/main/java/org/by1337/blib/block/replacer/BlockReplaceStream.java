package org.by1337.blib.block.replacer;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.BLib;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.block.replacer.type.impl.BlockDataBlock;
import org.by1337.blib.block.replacer.type.impl.MaterialBlock;
import org.by1337.blib.block.replacer.type.impl.WeBlockStateBlock;
import org.by1337.blib.geom.IntAABB;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.util.Pair;
import org.by1337.blib.util.collection.LockableList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockReplaceStream implements ReplaceTask {
    private LockableList<Pair<Vec3i, ReplaceBlock>> blockList = LockableList.createThreadSaveList();
    private boolean cancel;
    private int flag = BlockReplaceFlags.UPDATE_ALL;
    private int maxReplacesInTick = -1;
    private int updateLimit = BlockReplaceFlags.UPDATE_LIMIT;
    private Predicate<Block> filter;
    private Consumer<Block> blockBreakCallBack;

    public void start(World world) {
        start(world, flag);
    }

    public void start(World world, int flags) {
        start(world, flags, updateLimit);
    }

    public void start(World world, int flags, int updateLimit) {
        start(world, flags, updateLimit, BLib.getApi().getPooledBlockReplacer());
    }

    public void start(World world, int flags, int updateLimit, PooledBlockReplacer pooledBlockReplacer) {
        this.flag = flags;
        this.updateLimit = updateLimit;
        pooledBlockReplacer.startTask(this, world);
    }

    @Nullable
    public Pair<Vec3i, ReplaceBlock> next() {
        if (blockList.isEmpty()) return null;
        blockList.lock();
        try {
            var pair = blockList.get(0);
            blockList.remove(pair);
            return pair;
        } finally {
            blockList.unlock();
        }
    }

    @Override
    public @Nullable Predicate<Block> getFilter() {
        return filter;
    }

    @Override
    public @Nullable Consumer<Block> getBlockBreakCallBack() {
        return blockBreakCallBack;
    }

    @Override
    public int getMaxReplacesInTick() {
        return maxReplacesInTick;
    }

    @Override
    public boolean isEnd() {
        return cancel && blockList.isEmpty();
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public int getUpdateLimit() {
        return updateLimit;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setMaxReplacesInTick(int maxReplacesInTick) {
        this.maxReplacesInTick = maxReplacesInTick;
    }

    public void setUpdateLimit(int updateLimit) {
        this.updateLimit = updateLimit;
    }

    public void setFilter(Predicate<Block> filter) {
        this.filter = filter;
    }

    public void setBlockBreakCallBack(Consumer<Block> blockBreakCallBack) {
        this.blockBreakCallBack = blockBreakCallBack;
    }

    public BlockReplaceStream addToRemove(IntAABB aabb) {
        return addToReplace(aabb, Material.AIR);
    }

    public BlockReplaceStream addToReplace(IntAABB aabb, Material to) {
        return addToReplace(aabb, new MaterialBlock(to));
    }

    public BlockReplaceStream addToReplace(IntAABB aabb, BlockData to) {
        return addToReplace(aabb, new BlockDataBlock(to));
    }

    public BlockReplaceStream addToReplace(IntAABB aabb, com.sk89q.worldedit.world.block.BlockState to) {
        return addToReplace(aabb, new WeBlockStateBlock(to));
    }

    public BlockReplaceStream addToReplace(IntAABB aabb, ReplaceBlock replaceBlock) {
        for (Vec3i vec3i : aabb.getAllPointsInAABB()) {
            blockList.add(Pair.of(vec3i, replaceBlock));
        }
        return this;
    }


    public BlockReplaceStream addToReplace(Vec3i pos, com.sk89q.worldedit.world.block.BlockState to) {
        blockList.add(Pair.of(pos, new WeBlockStateBlock(to)));
        return this;
    }

    public BlockReplaceStream addToReplace(Location pos, com.sk89q.worldedit.world.block.BlockState to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new WeBlockStateBlock(to)));
        return this;
    }

    public BlockReplaceStream addToReplace(Vec3i pos, ReplaceBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceStream addToReplace(Location pos, ReplaceBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }

    public BlockReplaceStream addToReplace(Vec3i pos, WeBlockStateBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceStream addToReplace(Location pos, WeBlockStateBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }

    public BlockReplaceStream addToReplace(Vec3i pos, BlockData to) {
        blockList.add(Pair.of(pos, new BlockDataBlock(to)));
        return this;
    }

    public BlockReplaceStream addToReplace(Location pos, BlockData to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new BlockDataBlock(to)));
        return this;
    }

    public BlockReplaceStream addToReplace(Vec3i pos, BlockDataBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceStream addToReplace(Location pos, BlockDataBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }

    public BlockReplaceStream addToRemove(Vec3i pos) {
        blockList.add(Pair.of(pos, new MaterialBlock(Material.AIR)));
        return this;
    }

    public BlockReplaceStream addToRemove(Location pos) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new MaterialBlock(Material.AIR)));
        return this;
    }

    public BlockReplaceStream addToReplace(Vec3i pos, Material to) {
        blockList.add(Pair.of(pos, new MaterialBlock(to)));
        return this;
    }

    public BlockReplaceStream addToReplace(Location pos, Material to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new MaterialBlock(to)));
        return this;
    }

    public BlockReplaceStream addToReplace(Vec3i pos, MaterialBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceStream addToReplace(Location pos, MaterialBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }
}
