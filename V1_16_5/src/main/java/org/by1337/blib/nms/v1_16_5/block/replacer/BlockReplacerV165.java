package org.by1337.blib.nms.v1_16_5.block.replacer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MCUtil;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
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
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

import java.util.Objects;

@NMSAccessor(forClazz = BlockReplacer.class, forVersions = Version.V1_16_5)
public class BlockReplacerV165 implements BlockReplacer {

    @Override
    public Block replace(Vec3i pos, BlockData data, ReplaceTask task, World world, int flag) {
        ServerLevel nmsWorld = ((CraftWorld) world).getHandle();
        BlockPos blockPos = toNMS(pos);
        if (Level.isOutsideWorld(blockPos)) return null;

        Block bukkitBlock = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());

        BlockState oldBlock = nmsWorld.getType(blockPos);
        BlockState blockData = ((CraftBlockData) data).getState();
        if (Objects.equals(oldBlock, blockData)) return bukkitBlock;

        if (!blockData.isAir() && blockData.getBlock() instanceof BaseEntityBlock && blockData.getBlock() != oldBlock.getBlock()) {
            nmsWorld.removeTileEntity(blockPos);
        }
        // nmsWorld.setTypeAndData()
        if (nmsWorld.captureTreeGeneration) return bukkitBlock;
        LevelChunk chunk = nmsWorld.getChunkAtWorldCoords(blockPos);

        boolean captured = false;
        if (nmsWorld.captureBlockStates && !nmsWorld.capturedBlockStates.containsKey(blockPos)) {
            CraftBlockState blockstate = (CraftBlockState) nmsWorld.getWorld().getBlockAt(blockPos.getX(), blockPos.getY(), blockPos.getZ()).getState();
            blockstate.setFlag(flag);
            nmsWorld.capturedBlockStates.put(blockPos.immutableCopy(), blockstate);
            captured = true;
        }

        BlockState newBlock = chunk.setType(blockPos, blockData, (flag & BlockReplaceFlags.UPDATE_MOVE_BY_PISTON) != 0, (flag & BlockReplaceFlags.DONT_PLACE) == 0);
        nmsWorld.chunkPacketBlockController.onBlockChange(nmsWorld, blockPos, blockData, newBlock, flag);

        if (newBlock == null) {
            if (nmsWorld.captureBlockStates && captured) {
                nmsWorld.capturedBlockStates.remove(blockPos);
            }
            return null;
        } else {
            BlockState iblockdata2 = nmsWorld.getType(blockPos);
            if ((flag & BlockReplaceFlags.UPDATE_SUPPRESS_LIGHT) == 0 &&
                    iblockdata2 != newBlock &&
                    (iblockdata2.getLightBlock(nmsWorld, blockPos) != newBlock.getLightBlock(nmsWorld, blockPos) ||
                            iblockdata2.getLightEmission() != newBlock.getLightEmission() ||
                            iblockdata2.useShapeForLightOcclusion() ||
                            newBlock.useShapeForLightOcclusion())
            ) {
                //  nmsWorld.getMethodProfiler().enter("queueCheckLight");
                nmsWorld.getChunkProvider().getLightEngine().checkBlock(blockPos);
                //   nmsWorld.getMethodProfiler().exit();
            }

            if (!nmsWorld.captureBlockStates) {
                try {
                    notifyAndUpdatePhysics(blockPos, chunk, newBlock, blockData, iblockdata2, flag, task.getUpdateLimit(), nmsWorld);
                } catch (StackOverflowError var11) {
                    Level.lastPhysicsProblem = new BlockPos(blockPos);
                }
            }

            return bukkitBlock;
        }
    }

    public void notifyAndUpdatePhysics(BlockPos blockposition, LevelChunk chunk, BlockState oldBlock, BlockState newBlock, BlockState actualBlock, int flags, int limit, Level level) {
        if (actualBlock == newBlock) {
            if (oldBlock != actualBlock) {
                level.setBlocksDirty(blockposition, oldBlock, actualBlock);
            }

            if ((flags & BlockReplaceFlags.UPDATE_CLIENTS) != 0 &&
                    (!level.isClientSide || (flags & BlockReplaceFlags.UPDATE_INVISIBLE) == 0) &&
                    (level.isClientSide || chunk == null || chunk.getState() != null && chunk.getState().isAtLeast(ChunkHolder.FullChunkStatus.TICKING))
            ) {
                ((ServerLevel) level).getChunkProvider().flagDirty(blockposition);
                //  level.notify(blockposition, oldBlock, newBlock, flags); // disable paper updatePathfindingOnBlockUpdate
            } else if ((flags & BlockReplaceFlags.UPDATE_CLIENTS) != 0 &&
                    (!level.isClientSide || (flags & BlockReplaceFlags.UPDATE_INVISIBLE) == 0) &&
                    (level.isClientSide || chunk == null ||
                            ((ServerLevel) level).getChunkProvider().playerChunkMap.playerViewDistanceBroadcastMap.getObjectsInRange(MCUtil.getCoordinateKey(blockposition)) != null)
            ) {
                ((ServerLevel) level).getChunkProvider().flagDirty(blockposition);
            }

            if ((flags & BlockReplaceFlags.UPDATE_NEIGHBORS) != 0) {
                level.update(blockposition, oldBlock.getBlock());
                if (!level.isClientSide && newBlock.isComplexRedstone()) {
                    level.updateAdjacentComparators(blockposition, newBlock.getBlock());
                }
            }

            if ((flags & BlockReplaceFlags.UPDATE_KNOWN_SHAPE) == 0 && limit > 0) {
                int k = flags & -BlockReplaceFlags.UPDATE_CLIENTS; // BlockReplaceFlags.UPDATE_SUPPRESS_DROPS // disable unset UPDATE_SUPPRESS_DROPS
                oldBlock.updateIndirectNeighbourShapes(level, blockposition, k, limit - 1);
                CraftWorld world = (level).getWorld();
                if (world != null && ((ServerLevel) level).hasPhysicsEvent) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftBlockData.fromData(newBlock));
                    level.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                }

                newBlock.updateNeighbourShapes(level, blockposition, k, limit - 1);
                newBlock.updateIndirectNeighbourShapes(level, blockposition, k, limit - 1);
            }
            level.onBlockStateChange(blockposition, oldBlock, actualBlock);
        }
    }


    private BlockPos toNMS(Vec3i pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

}

