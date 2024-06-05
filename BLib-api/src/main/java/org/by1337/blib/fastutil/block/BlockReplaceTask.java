package org.by1337.blib.fastutil.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.fastutil.FastUtilApi;
import org.by1337.blib.geom.IntAABB;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.util.Pair;
import org.by1337.blib.fastutil.block.block.ReplaceBlock;
import org.by1337.blib.fastutil.block.block.impl.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockReplaceTask extends BlockReplaceSetting {
    private List<Pair<Vec3i, ReplaceBlock>> blockList = new ArrayList<>();
    private CompletableFuture<BlockReplaceStatus> future;

    public CompletableFuture<BlockReplaceStatus> start(World inWorld){
        blockList = Collections.unmodifiableList(blockList);
        future = new CompletableFuture<>();
        FastUtilApi.startTask(this, inWorld);
        return future;
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

    public CompletableFuture<BlockReplaceStatus> getFuture() {
        return future;
    }

    public List<Pair<Vec3i, ReplaceBlock>> getBlockList() {
        return blockList;
    }

    @Override
    public BlockReplaceTask setCustomTimeOut(long customTimeOut) {
        return (BlockReplaceTask) super.setCustomTimeOut(customTimeOut);
    }

    @Override
    public BlockReplaceTask setApplyPhysics(boolean applyPhysics) {
        return (BlockReplaceTask) super.setApplyPhysics(applyPhysics);
    }

    @Override
    public BlockReplaceTask setDeobfuscatePaperAntiXRay(boolean deobfuscatePaperAntiXRay) {
        return (BlockReplaceTask) super.setDeobfuscatePaperAntiXRay(deobfuscatePaperAntiXRay);
    }

    @Override
    public BlockReplaceTask setRecalculateLight(boolean recalculateLight) {
        return (BlockReplaceTask) super.setRecalculateLight(recalculateLight);
    }

    @Override
    public BlockReplaceTask setSendPacketsOnBlockChange(boolean sendPacketsOnBlockChange) {
        return (BlockReplaceTask) super.setSendPacketsOnBlockChange(sendPacketsOnBlockChange);
    }

    @Override
    public BlockReplaceTask setDoDropFromChests(boolean doDropFromChests) {
        return (BlockReplaceTask) super.setDoDropFromChests(doDropFromChests);
    }

    @Override
    public BlockReplaceTask setCustomLimit(int customLimit) {
        return (BlockReplaceTask) super.setCustomLimit(customLimit);
    }

    @Override
    public BlockReplaceTask setBlockBreakCallBack(Consumer<Block> blockBreakCallBack) {
        return (BlockReplaceTask) super.setBlockBreakCallBack(blockBreakCallBack);
    }

    @Override
    public BlockReplaceTask setPerfectPhysics(boolean perfectPhysics) {
        return (BlockReplaceTask) super.setPerfectPhysics(perfectPhysics);
    }

    @Override
    public BlockReplaceTask setFilter(Predicate<Block> filter) {
        return (BlockReplaceTask) super.setFilter(filter);
    }

    @Override
    public BlockReplaceTask setDebug(boolean debug) {
        return (BlockReplaceTask) super.setDebug(debug);
    }
}
