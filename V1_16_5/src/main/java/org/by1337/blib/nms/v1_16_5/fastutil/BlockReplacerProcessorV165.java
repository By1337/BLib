package org.by1337.blib.nms.v1_16_5.fastutil;

import com.sk89q.worldedit.bukkit.BukkitAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.fastutil.FastUtilSetting;
import org.by1337.blib.fastutil.block.BlockReplaceTask;
import org.by1337.blib.fastutil.block.BlockReplacerProcessor;
import org.by1337.blib.fastutil.block.block.ReplaceBlock;
import org.by1337.blib.fastutil.block.block.impl.BlockDataBlock;
import org.by1337.blib.fastutil.block.block.impl.MaterialBlock;
import org.by1337.blib.fastutil.block.block.impl.WeBlockStateBlock;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.profiler.EmptyProfiler;
import org.by1337.blib.profiler.IProfiler;
import org.by1337.blib.profiler.Profiler;
import org.by1337.blib.util.Pair;
import org.spigotmc.AsyncCatcher;

public class BlockReplacerProcessorV165 implements BlockReplacerProcessor {
    private final FastUtilSetting setting;
    private final Message message;
    private final Level nmsWorld;
    private final World world;
    private final BukkitTask task;
    private int warns;
    private long timeOverflow;
    private final List<BlockReplacerProcessorV165.Process> taskList = new ArrayList<>();

    public BlockReplacerProcessorV165(FastUtilSetting setting, Message message, World world, Plugin plugin) {
        this.setting = setting;
        this.message = message;
        this.world = world;
        this.nmsWorld = ((CraftWorld) world).getHandle();
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0L, 1L);
    }

    private void tick() {
        if (!this.taskList.isEmpty()) {
            int limit = this.setting.getMaxReplacesPerTick();
            long time = System.currentTimeMillis() + this.setting.getMaxTimeOut();
            if (this.warns >= 5) {
                this.message.warning("Skipped %s+ ticks! Server is overload? Time %s", this.warns, this.timeOverflow);
                limit >>= 4;
            }

            int limitForTask = limit / this.taskList.size();
            limitForTask = Math.max(limitForTask, 1);
            BlockReplacerProcessorV165.CyclicIterator iter = new BlockReplacerProcessorV165.CyclicIterator();

            while (iter.hasNext() && limit > 0) {
                BlockReplacerProcessorV165.Process process = iter.next();
                if (process.isEnd()) {
                    iter.remove();
                    process.task.getFuture().completeAsync(() -> process.statusv165);
                } else {
                    int cashBack = process.tick(limitForTask);
                    if (process.isEnd()) {
                        iter.remove();
                        process.task.getFuture().completeAsync(() -> process.statusv165);
                    }

                    long time1 = System.currentTimeMillis() - time;
                    if (time1 > 0L) {
                        this.timeOverflow += time1;
                        ++this.warns;
                        break;
                    }

                    this.warns = 0;
                    this.timeOverflow = 0L;
                    limit -= limitForTask - cashBack;
                }
            }
        }
    }

    public void addTask(BlockReplaceTask task) {
        AsyncCatcher.catchOp("Async task add!");
        this.taskList.add(new BlockReplacerProcessorV165.Process(task));
    }

    public void unload(boolean force) {
        this.task.cancel();
        if (!force) {
            for (BlockReplacerProcessorV165.Process process : this.taskList) {
                for (int x = 0; !process.isEnd(); ++x) {
                    if (x > 10000) {
                        this.message.error("Failed to end task: " + process.task, new Throwable());
                        break;
                    }

                    process.tick(Integer.MAX_VALUE);
                    if (process.isEnd()) {
                        process.task.getFuture().completeAsync(() -> process.statusv165);
                    }
                }
            }
        }

        this.taskList.clear();
    }

    private BlockPos toNms(Vec3i vec3i) {
        return new BlockPos(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    private class CyclicIterator implements Iterator<BlockReplacerProcessorV165.Process> {
        int current = 0;

        public boolean hasNext() {
            return !BlockReplacerProcessorV165.this.taskList.isEmpty();
        }

        public BlockReplacerProcessorV165.Process next() {
            if (BlockReplacerProcessorV165.this.taskList.isEmpty()) {
                throw new IllegalStateException("List is empty");
            } else {
                this.current = (this.current + 1) % BlockReplacerProcessorV165.this.taskList.size();
                return BlockReplacerProcessorV165.this.taskList.get(this.current);
            }
        }

        public void remove() {
            BlockReplacerProcessorV165.this.taskList.remove(this.current % BlockReplacerProcessorV165.this.taskList.size());
            if (BlockReplacerProcessorV165.this.taskList.size() >= this.current) {
                this.current = 0;
            }
        }
    }

    private class Process {
        private final BlockReplaceTask task;
        private final BlockReplaceStatusv165 statusv165;
        private final Iterator<Pair<Vec3i, ReplaceBlock>> iterator;
        private final IProfiler profiler;

        public Process(BlockReplaceTask param2) {
            this.task = param2;
            this.statusv165 = new BlockReplaceStatusv165((task.isDebug() ? new Profiler() : new EmptyProfiler()), task);
            this.profiler = this.statusv165.getProfiler();
            this.iterator = task.getBlockList().iterator();
        }

        public boolean isEnd() {
            return !this.iterator.hasNext();
        }

        private void applyPhysics() {
            if (this.task.isApplyPhysics()) {
                if (!BlockReplacerProcessorV165.this.nmsWorld.captureBlockStates) {
                    this.profiler.start("full applyPhysics");

                    for (Pair<Vec3i, ReplaceBlock> pair : this.task.getBlockList()) {
                        this.applyPhysics(BlockReplacerProcessorV165.this.toNms(pair.getLeft()));
                    }

                    this.profiler.end("full applyPhysics");
                }
            }
        }

        private void applyPhysics(BlockPos blockPos) {
            this.profiler.start("applyPhysics");

            for (Direction value : Direction.values()) {
                BlockPos pos = blockPos.add(value.getNormal_());
                BlockState type = BlockReplacerProcessorV165.this.nmsWorld.getType(pos);
                if (!type.getBukkitMaterial().isAir()) {
                    type.getBlock()
                            .updateState(
                                    type, value, BlockReplacerProcessorV165.this.nmsWorld.getType(blockPos), BlockReplacerProcessorV165.this.nmsWorld, pos, blockPos
                            );
                }
            }

            this.profiler.end("applyPhysics");
        }

        private int tick(int limit) {
            int var2;
            try {
                this.profiler.start("tick");
                var2 = this.tick0(limit);
            } finally {
                this.profiler.end("tick");
            }

            return var2;
        }

        private int tick0(int limit) {
            if (this.isEnd()) {
                throw new IllegalStateException("Task is end!");
            } else {
                while (this.iterator.hasNext()) {
                    if (limit <= 0) {
                        return limit;
                    }

                    --limit;
                    Pair<Vec3i, ReplaceBlock> pair = this.iterator.next();
                    BlockPos blockPos = BlockReplacerProcessorV165.this.toNms(pair.getLeft());
                    if (blockPos.getY() <= 255 && blockPos.getY() >= 0) {
                        ReplaceBlock blockReplace = pair.getRight();
                        BlockState replace;
                        if (blockReplace instanceof BlockDataBlock blockDataBlock) {
                            replace = ((CraftBlockData) blockDataBlock.blockData).getState();
                        } else if (blockReplace instanceof MaterialBlock materialBlock) {
                            replace = ((CraftBlockData) materialBlock.material.createBlockData()).getState();
                        } else if (blockReplace instanceof WeBlockStateBlock weBlockStateBlock) {
                            replace = ((CraftBlockData) BukkitAdapter.adapt(weBlockStateBlock.blockState)).getState();
                        } else {
                            throw new IllegalArgumentException(blockReplace.getClass().toString());
                        }
                        LevelChunk chunk = BlockReplacerProcessorV165.this.nmsWorld.getChunkAtWorldCoords(blockPos);
                        BlockState oldBlock = chunk.getBlockData(blockPos);
                        if (Objects.equals(oldBlock, replace)) {
                            ++limit;
                        } else {
                            Block bukkitBlock = CraftBlock.at(BlockReplacerProcessorV165.this.nmsWorld, blockPos);
                            if (this.task.getFilter() != null && this.task.getFilter().test(bukkitBlock)) {
                                ++limit;
                            } else {
                                this.statusv165.oldBlocksMap.put(pair.getLeft(), bukkitBlock.getBlockData());
                                if (this.task.getBlockBreakCallBack() != null) {
                                    this.task.getBlockBreakCallBack().accept(bukkitBlock);
                                }

                                boolean captured = this.captured(blockPos);
                                oldBlock.remove(BlockReplacerProcessorV165.this.nmsWorld, blockPos, replace, true);
                                if (oldBlock.getBlock() instanceof BaseEntityBlock) {
                                    BlockReplacerProcessorV165.this.nmsWorld.removeTileEntity(blockPos);
                                }

                                BlockState iblockdata1 = chunk.setType(blockPos, replace, true, this.task.isDoPlace());
                                if (this.task.isRecalculateLight()) {
                                    BlockReplacerProcessorV165.this.nmsWorld.getChunkProvider().getLightEngine().checkBlock_(blockPos);
                                }

                                if (this.task.isDeobfuscatePaperAntiXRay()) {
                                    BlockReplacerProcessorV165.this.nmsWorld
                                            .chunkPacketBlockController
                                            .onBlockChange(BlockReplacerProcessorV165.this.nmsWorld, blockPos, replace, iblockdata1, 1042);
                                }

                                if (iblockdata1 == null) {
                                    if (BlockReplacerProcessorV165.this.nmsWorld.captureBlockStates && captured) {
                                        BlockReplacerProcessorV165.this.nmsWorld.capturedBlockStates.remove(blockPos);
                                    }
                                } else {
                                    if (!BlockReplacerProcessorV165.this.nmsWorld.captureBlockStates && this.task.isSendPacketsOnBlockChange()) {
                                        ((ServerLevel) BlockReplacerProcessorV165.this.nmsWorld).getChunkProvider().flagDirty(blockPos);
                                    }

                                    if (this.task.isPerfectPhysics()) {
                                        this.applyPhysics(blockPos);
                                    }
                                }
                            }
                        }
                    }
                }

                this.applyPhysics();
                return limit;
            }
        }

        private boolean captured(BlockPos position) {
            if (BlockReplacerProcessorV165.this.nmsWorld.captureBlockStates
                && !BlockReplacerProcessorV165.this.nmsWorld.capturedBlockStates.containsKey(position)) {
                CraftBlockState blockstate = (CraftBlockState) BlockReplacerProcessorV165.this.world
                        .getBlockAt(position.getX(), position.getY(), position.getZ())
                        .getState();
                blockstate.setFlag(1);
                BlockReplacerProcessorV165.this.nmsWorld.capturedBlockStates.put(position.immutableCopy(), blockstate);
                return true;
            } else {
                return false;
            }
        }
    }
}
