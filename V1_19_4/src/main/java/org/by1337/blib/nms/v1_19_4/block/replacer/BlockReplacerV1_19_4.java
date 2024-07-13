package org.by1337.blib.nms.v1_19_4.block.replacer;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import io.papermc.paper.util.MCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.by1337.blib.block.replacer.BlockReplaceFlags;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.block.replacer.type.impl.BlockDataBlock;
import org.by1337.blib.block.replacer.type.impl.MaterialBlock;
import org.by1337.blib.block.replacer.type.impl.WeBlockStateBlock;
import org.by1337.blib.geom.Vec3i;

import java.util.Objects;

public class BlockReplacerV1_19_4 implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos0, BlockData data, ReplaceTask task, World world) {


        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos pos = toNMS(pos0);
        if (nmsWorld.isOutsideBuildHeight(pos)) return null;
        if (nmsWorld.captureTreeGeneration) return null;


        Block bukkitBlock = world.getBlockAt(pos0.getX(), pos0.getY(), pos0.getZ());

        BlockState oldBlock = nmsWorld.getBlockState(pos);
        BlockState state = ((CraftBlockData) data).getState();
        if (Objects.equals(oldBlock, state)) return bukkitBlock;

        if (oldBlock.hasBlockEntity() && oldBlock.getBlock() != state.getBlock()) {
            nmsWorld.removeBlockEntity(pos);
        }

        LevelChunk chunk = nmsWorld.getChunkAt(pos);

     //   net.minecraft.world.level.block.Block block = state.getBlock();
        boolean captured = false;
        if (nmsWorld.captureBlockStates && !nmsWorld.capturedBlockStates.containsKey(pos)) {
            CraftBlockState blockstate = (CraftBlockState)world.getBlockAt(pos.getX(), pos.getY(), pos.getZ()).getState();
            blockstate.setFlag(task.getFlag());
            nmsWorld.capturedBlockStates.put(pos.immutable(), blockstate);
            captured = true;
        }
        BlockState iblockdata1 = chunk.setBlockState(pos, state, (task.getFlag() & BlockReplaceFlags.UPDATE_MOVE_BY_PISTON) != 0, (task.getFlag() & BlockReplaceFlags.DONT_PLACE) == 0);
        nmsWorld.chunkPacketBlockController.onBlockChange(nmsWorld, pos, state, iblockdata1, task.getFlag(), task.getUpdateLimit());

        if (iblockdata1 == null) {
            if (nmsWorld.captureBlockStates && captured) {
                nmsWorld.capturedBlockStates.remove(pos);
            }
            return null;
        } else {
            BlockState iblockdata2 = nmsWorld.getBlockState(pos);
            if ((task.getFlag() & 128) == 0 && iblockdata2 != iblockdata1 && (iblockdata2.getLightBlock(nmsWorld, pos) != iblockdata1.getLightBlock(nmsWorld, pos) || iblockdata2.getLightEmission() != iblockdata1.getLightEmission() || iblockdata2.useShapeForLightOcclusion() || iblockdata1.useShapeForLightOcclusion())) {
                nmsWorld.getProfiler().push("queueCheckLight");
                nmsWorld.getChunkSource().getLightEngine().checkBlock(pos);
                nmsWorld.getProfiler().pop();
            }

            if (!nmsWorld.captureBlockStates) {
                try {
                    notifyAndUpdatePhysics(pos, chunk, iblockdata1, state, iblockdata2, task.getFlag(), task.getUpdateLimit(), nmsWorld);
                } catch (StackOverflowError var11) {
                    Level.lastPhysicsProblem = new BlockPos(pos);
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

            if ((i & BlockReplaceFlags.UPDATE_CLIENTS) != 0 && (!level.isClientSide || (i & BlockReplaceFlags.UPDATE_INVISIBLE) == 0) && (level.isClientSide || chunk == null || chunk.getFullStatus().isOrAfter(ChunkHolder.FullChunkStatus.TICKING))) {
             //   level.sendBlockUpdated(blockposition, oldBlock, newBlock, i); // disable update pathfinding
                ((ServerLevel)level).getChunkSource().blockChanged(blockposition);
            } else if ((i & BlockReplaceFlags.UPDATE_CLIENTS) != 0 && (!level.isClientSide || (i & BlockReplaceFlags.UPDATE_INVISIBLE) == 0) && (level.isClientSide || chunk == null || ((ServerLevel)level).getChunkSource().chunkMap.playerChunkManager.broadcastMap.getObjectsInRange(MCUtil.getCoordinateKey(blockposition)) != null)) {
                ((ServerLevel)level).getChunkSource().blockChanged(blockposition);
            }

            if ((i & BlockReplaceFlags.UPDATE_NEIGHBORS) != 0) {
                level.blockUpdated(blockposition, oldBlock.getBlock());
                if (!level.isClientSide && newBlock.hasAnalogOutputSignal()) {
                    level.updateNeighbourForOutputSignal(blockposition, newBlock.getBlock());
                }
            }

            if ((i & BlockReplaceFlags.UPDATE_KNOWN_SHAPE) == 0 && j > 0) {
                int k = i & -(BlockReplaceFlags.UPDATE_CLIENTS /*+ BlockReplaceFlags.UPDATE_SUPPRESS_DROPS*/); // disable unset UPDATE_SUPPRESS_DROPS;
                oldBlock.updateIndirectNeighbourShapes(level, blockposition, k, j - 1);
                CraftWorld world = ((ServerLevel)level).getWorld();
                if (((ServerLevel)level).hasPhysicsEvent) {
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
