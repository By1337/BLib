package org.by1337.blib.nms.v1_18_2.block.replacer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.CapturedBlockState;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.by1337.blib.block.replacer.BlockReplaceFlags;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

import java.util.Objects;

@NMSAccessor(forClazz = BlockReplacer.class, forVersions = Version.V1_18_2)
public class BlockReplacerV1_18_2 implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos0, BlockData data, ReplaceTask task, World world, int flag) {

        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos blockposition = toNMS(pos0);
        if (nmsWorld.isOutsideBuildHeight(blockposition)) return null;
        if (nmsWorld.captureTreeGeneration) return null;


        Block bukkitBlock = world.getBlockAt(pos0.getX(), pos0.getY(), pos0.getZ());

        BlockState oldBlock = nmsWorld.getBlockState(blockposition);
        BlockState iblockdata = ((CraftBlockData) data).getState();
        if (Objects.equals(oldBlock, iblockdata)) return bukkitBlock;

        if (oldBlock.hasBlockEntity() && oldBlock.getBlock() != iblockdata.getBlock()) {
            nmsWorld.removeBlockEntity(blockposition);
        }

        LevelChunk chunk = nmsWorld.getChunkAt(blockposition);


        net.minecraft.world.level.block.Block block = iblockdata.getBlock();
        boolean captured = false;
        if (nmsWorld.captureBlockStates && !nmsWorld.capturedBlockStates.containsKey(blockposition)) {
            CapturedBlockState blockstate = CapturedBlockState.getBlockState(nmsWorld, blockposition, flag);
            nmsWorld.capturedBlockStates.put(blockposition.immutable(), blockstate);
            captured = true;
        }

        BlockState iblockdata1 = chunk.setBlockState(blockposition, iblockdata, (flag & BlockReplaceFlags.UPDATE_MOVE_BY_PISTON) != 0, (flag & BlockReplaceFlags.DONT_PLACE) == 0);
        if (iblockdata1 == null) {
            if (nmsWorld.captureBlockStates && captured) {
                nmsWorld.capturedBlockStates.remove(blockposition);
            }

            return null;
        } else {
            BlockState iblockdata2 = nmsWorld.getBlockState(blockposition);
            if ((flag & BlockReplaceFlags.UPDATE_SUPPRESS_LIGHT) == 0 && iblockdata2 != iblockdata1 && (iblockdata2.getLightBlock(nmsWorld, blockposition) != iblockdata1.getLightBlock(nmsWorld, blockposition) || iblockdata2.getLightEmission() != iblockdata1.getLightEmission() || iblockdata2.useShapeForLightOcclusion() || iblockdata1.useShapeForLightOcclusion())) {
                nmsWorld.getProfiler().push("queueCheckLight");
                nmsWorld.getChunkSource().getLightEngine().checkBlock(blockposition);
                nmsWorld.getProfiler().pop();
            }

            if (!nmsWorld.captureBlockStates) {
                try {
                    this.notifyAndUpdatePhysics(blockposition, chunk, iblockdata1, iblockdata, iblockdata2, flag, task.getUpdateLimit(), nmsWorld);
                } catch (StackOverflowError var11) {
                    Level.lastPhysicsProblem = new BlockPos(blockposition);
                }
            }

            return bukkitBlock;
        }
    }

    public void notifyAndUpdatePhysics(BlockPos blockposition, LevelChunk chunk, BlockState oldBlock, BlockState newBlock, BlockState actualBlock, int i, int j, Level level) {
        if (actualBlock == newBlock) {
            if (oldBlock != actualBlock) {
                level.setBlocksDirty(blockposition, oldBlock, actualBlock);
            }

            if ((i & 2) != 0 && (!level.isClientSide || (i & 4) == 0) && (level.isClientSide || chunk == null || chunk.getFullStatus() != null && chunk.getFullStatus().isOrAfter(ChunkHolder.FullChunkStatus.TICKING))) {
                //  level.sendBlockUpdated(blockposition, oldBlock, newBlock, i); // disable update navigatingMobs
                ((ServerLevel) level).getChunkSource().blockChanged(blockposition);
            }

            if ((i & 1) != 0) {
                level.blockUpdated(blockposition, oldBlock.getBlock());
                if (!level.isClientSide && newBlock.hasAnalogOutputSignal()) {
                    level.updateNeighbourForOutputSignal(blockposition, newBlock.getBlock());
                }
            }

            if ((i & 16) == 0 && j > 0) {
                int k = i & -(BlockReplaceFlags.UPDATE_CLIENTS /*+ BlockReplaceFlags.UPDATE_SUPPRESS_DROPS*/); // disable unset UPDATE_SUPPRESS_DROPS;
                oldBlock.updateIndirectNeighbourShapes(level, blockposition, k, j - 1);
                CraftWorld world = (level).getWorld();
                if (world != null) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftBlockData.fromData(newBlock));
                    level.getCraftServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                }

                newBlock.updateNeighbourShapes(level, blockposition, k, j - 1);
                newBlock.updateIndirectNeighbourShapes(level, blockposition, k, j - 1);
            }

            if (!level.preventPoiUpdated) {
                level.onBlockStateChange(blockposition, oldBlock, actualBlock);
            }
        }
    }


    private BlockPos toNMS(Vec3i pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }
}
