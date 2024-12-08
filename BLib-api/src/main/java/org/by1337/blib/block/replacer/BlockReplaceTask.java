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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockReplaceTask implements ReplaceTask {
    private List<Pair<Vec3i, ReplaceBlock>> blockList = new ArrayList<>();
    private Iterator<Pair<Vec3i, ReplaceBlock>> iterator;
    private CompletableFuture<BlockReplaceTask> future;
    private Predicate<Block> filter;
    private Consumer<Block> blockPreReplaceCallBack;
    private Consumer<Block> blockPostReplaceCallBack;
    private int flag = BlockReplaceFlags.UPDATE_ALL;
    private int maxReplacesInTick = -1;
    private int updateLimit = BlockReplaceFlags.UPDATE_LIMIT;
    private BlockReplacer defaultBlockReplacer;


    public CompletableFuture<BlockReplaceTask> start(World world) {
        return start(world, flag);
    }

    public CompletableFuture<BlockReplaceTask> start(World world, int flags) {
        return start(world, flags, updateLimit);
    }

    public CompletableFuture<BlockReplaceTask> start(World world, int flags, int updateLimit) {
        return start(world, flags, updateLimit, BLib.getApi().getPooledBlockReplacer());
    }

    public CompletableFuture<BlockReplaceTask> start(World world, int flags, int updateLimit, PooledBlockReplacer pooledBlockReplacer) {
        this.flag = flags;
        this.updateLimit = updateLimit;

        iterator = blockList.iterator();
        blockList = Collections.unmodifiableList(blockList);

        future = new CompletableFuture<>();

        pooledBlockReplacer.startTask(this, world);
        return future;
    }

    @Override
    public @Nullable Pair<Vec3i, ReplaceBlock> next() {
        if (iterator == null) throw new IllegalStateException("Isn't started!");
        if (!iterator.hasNext()) return null;
        return iterator.next();
    }

    @Override
    public boolean isEnd() {
        return !iterator.hasNext();
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public int getUpdateLimit() {
        return updateLimit;
    }

    @Override
    public @Nullable Predicate<Block> getFilter() {
        return filter;
    }

    @Override
    public @Nullable Consumer<Block> getBlockPreReplaceCallBack() {
        return blockPreReplaceCallBack;
    }

    @Override
    public int getMaxReplacesInTick() {
        return maxReplacesInTick;
    }

    public void setFilter(Predicate<Block> filter) {
        this.filter = filter;
    }

    public void setBlockBreakCallBack(Consumer<Block> blockBreakCallBack) {
        setBlockPreReplaceCallBack(blockBreakCallBack);
    }

    public void setBlockPreReplaceCallBack(Consumer<Block> blockPreReplaceCallBack) {
        this.blockPreReplaceCallBack = blockPreReplaceCallBack;
    }

    @Override
    public @Nullable Consumer<Block> getBlockPostReplaceCallBack() {
        return blockPostReplaceCallBack;
    }

    @Override
    public void setBlockPostReplaceCallBack(Consumer<Block> blockPreReplaceCallBack) {
        this.blockPostReplaceCallBack = blockPreReplaceCallBack;
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

    public void setDefaultBlockReplacer(BlockReplacer defaultBlockReplacer) {
        this.defaultBlockReplacer = defaultBlockReplacer;
    }

    @Override
    public @Nullable BlockReplacer getCustomBlockReplacer() {
        return defaultBlockReplacer;
    }

    public BlockReplaceTask addToRemove(IntAABB aabb) {
        return addToReplace(aabb, Material.AIR);
    }

    public BlockReplaceTask addToReplace(IntAABB aabb, Material to) {
        return addToReplace(aabb, new MaterialBlock(to));
    }

    public BlockReplaceTask addToReplace(IntAABB aabb, BlockData to) {
        return addToReplace(aabb, new BlockDataBlock(to));
    }

    public BlockReplaceTask addToReplace(IntAABB aabb, com.sk89q.worldedit.world.block.BlockState to) {
        return addToReplace(aabb, new WeBlockStateBlock(to));
    }

    public BlockReplaceTask addToReplace(IntAABB aabb, ReplaceBlock replaceBlock) {
        for (Vec3i vec3i : aabb.getAllPointsInAABB()) {
            blockList.add(Pair.of(vec3i, replaceBlock));
        }
        return this;
    }


    public BlockReplaceTask addToReplace(Vec3i pos, com.sk89q.worldedit.world.block.BlockState to) {
        blockList.add(Pair.of(pos, new WeBlockStateBlock(to)));
        return this;
    }

    public BlockReplaceTask addToReplace(Location pos, com.sk89q.worldedit.world.block.BlockState to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new WeBlockStateBlock(to)));
        return this;
    }

    public BlockReplaceTask addToReplace(Vec3i pos, ReplaceBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceTask addToReplace(Location pos, ReplaceBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }

    public BlockReplaceTask addToReplace(Vec3i pos, WeBlockStateBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceTask addToReplace(Location pos, WeBlockStateBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }

    public BlockReplaceTask addToReplace(Vec3i pos, BlockData to) {
        blockList.add(Pair.of(pos, new BlockDataBlock(to)));
        return this;
    }

    public BlockReplaceTask addToReplace(Location pos, BlockData to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new BlockDataBlock(to)));
        return this;
    }

    public BlockReplaceTask addToReplace(Vec3i pos, BlockDataBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceTask addToReplace(Location pos, BlockDataBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }

    public BlockReplaceTask addToRemove(Vec3i pos) {
        blockList.add(Pair.of(pos, new MaterialBlock(Material.AIR)));
        return this;
    }

    public BlockReplaceTask addToRemove(Location pos) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new MaterialBlock(Material.AIR)));
        return this;
    }

    public BlockReplaceTask addToReplace(Vec3i pos, Material to) {
        blockList.add(Pair.of(pos, new MaterialBlock(to)));
        return this;
    }

    public BlockReplaceTask addToReplace(Location pos, Material to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), new MaterialBlock(to)));
        return this;
    }

    public BlockReplaceTask addToReplace(Vec3i pos, MaterialBlock to) {
        blockList.add(Pair.of(pos, to));
        return this;
    }

    public BlockReplaceTask addToReplace(Location pos, MaterialBlock to) {
        blockList.add(Pair.of(new Vec3i(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()), to));
        return this;
    }

    public CompletableFuture<BlockReplaceTask> getFuture() {
        return future;
    }

    public List<Pair<Vec3i, ReplaceBlock>> getBlockList() {
        return blockList;
    }


}
