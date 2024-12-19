package org.by1337.blib.nms.V1_21_4.block.replacer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.by1337.blib.block.replacer.BlockReplaceFlags;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.geom.Vec3i;

import java.util.Objects;

public class BlockReplacerV1_21_4 implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos0, BlockData data, ReplaceTask task, World world, int flag) {
        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos pos = toNMS(pos0);
        if (nmsWorld.isOutsideBuildHeight(pos)) return null;
        if (nmsWorld.captureTreeGeneration) return null;

        Block bukkitBlock = world.getBlockAt(pos0.getX(), pos0.getY(), pos0.getZ());

        BlockState oldBlock = nmsWorld.getBlockState(pos);
        BlockState state = ((CraftBlockData) data).getState();
        if (Objects.equals(oldBlock, state)) return bukkitBlock;
//        if (oldBlock.hasBlockEntity() && state.getBlock() != oldBlock.getBlock()) {
//            nmsWorld.removeBlockEntity(pos);
//        }

        LevelChunk chunk = nmsWorld.getChunkAt(pos);


        boolean captured = false;
        if (nmsWorld.captureBlockStates && !nmsWorld.capturedBlockStates.containsKey(pos)) {
            CraftBlockState blockstate = (CraftBlockState) nmsWorld.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()).getState();
            blockstate.setFlag(flag);
            nmsWorld.capturedBlockStates.put(pos.immutable(), blockstate);
            captured = true;
        }

        BlockState iblockdata1 = chunk.setBlockState(pos, state, (flag & BlockReplaceFlags.UPDATE_MOVE_BY_PISTON) != 0, (flag & BlockReplaceFlags.DONT_PLACE) == 0);
        nmsWorld.chunkPacketBlockController.onBlockChange(nmsWorld, pos, state, iblockdata1, flag, task.getUpdateLimit());
        if (iblockdata1 == null) {
            if (nmsWorld.captureBlockStates && captured) {
                nmsWorld.capturedBlockStates.remove(pos);
            }

            return null;
        } else {
            BlockState iblockdata2 = nmsWorld.getBlockState(pos);
            if (!nmsWorld.captureBlockStates) {
                try {
                    notifyAndUpdatePhysics(pos, chunk, iblockdata1, state, iblockdata2, flag, task.getUpdateLimit(), nmsWorld);
                } catch (StackOverflowError var11) {
                    Level.lastPhysicsProblem = new BlockPos(pos);
                }
            }

            return bukkitBlock;
        }

    }

    public void notifyAndUpdatePhysics(BlockPos blockposition, LevelChunk chunk, BlockState oldBlock, BlockState newBlock, BlockState actualBlock, int flags, int updateLimit, Level level) {
        if (actualBlock == newBlock) {
            if (oldBlock != actualBlock) {
                level.setBlocksDirty(blockposition, oldBlock, actualBlock);
            }

            if ((flags & BlockReplaceFlags.UPDATE_CLIENTS) != 0 && (!level.isClientSide || (flags & BlockReplaceFlags.UPDATE_INVISIBLE) == 0) && (level.isClientSide || chunk == null || chunk.getFullStatus().isOrAfter(FullChunkStatus.FULL))) {
                //level.sendBlockUpdated(blockposition, oldBlock, newBlock, flags); // disable mob path update
                ((ServerLevel) level).getChunkSource().blockChanged(blockposition);
            }

            if ((flags & BlockReplaceFlags.UPDATE_NEIGHBORS) != 0) {
                level.blockUpdated(blockposition, oldBlock.getBlock());
                if (!level.isClientSide && newBlock.hasAnalogOutputSignal()) {
                    level.updateNeighbourForOutputSignal(blockposition, newBlock.getBlock());
                }
            }

            if ((flags & BlockReplaceFlags.UPDATE_KNOWN_SHAPE) == 0 && updateLimit > 0) {
                int k = flags & -(BlockReplaceFlags.UPDATE_CLIENTS /*+ BlockReplaceFlags.UPDATE_SUPPRESS_DROPS*/); // disable unset UPDATE_SUPPRESS_DROPS
                oldBlock.updateIndirectNeighbourShapes(level, blockposition, k, updateLimit - 1);
                CraftWorld world = level.getWorld();
                boolean cancelledUpdates = false;
                if (((ServerLevel) level).hasPhysicsEvent) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftBlockData.fromData(newBlock));
                    level.getCraftServer().getPluginManager().callEvent(event);
                    cancelledUpdates = event.isCancelled();
                }

                if (!cancelledUpdates) {
                    newBlock.updateNeighbourShapes(level, blockposition, k, updateLimit - 1);
                    newBlock.updateIndirectNeighbourShapes(level, blockposition, k, updateLimit - 1);
                }
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
