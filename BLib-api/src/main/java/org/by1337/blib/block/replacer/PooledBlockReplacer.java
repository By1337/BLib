package org.by1337.blib.block.replacer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.util.Pair;
import org.by1337.blib.util.collection.CyclicIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PooledBlockReplacer {
    private final Plugin plugin;
    private final long maxTimeOut;
    private final BlockReplacer blockReplacer;
    private final Message message;
    private final Map<World, WorldReplacer> worldReplacerMap = new HashMap<>();
    private BukkitTask task;

    public PooledBlockReplacer(Plugin plugin, long maxTimeOut, BlockReplacer blockReplacer, Message message) {
        this.plugin = plugin;
        this.maxTimeOut = maxTimeOut;
        this.blockReplacer = blockReplacer;
        this.message = message;
    }

    private void tick() {
        worldReplacerMap.values().forEach(WorldReplacer::tick);
    }

    public void startTask(ReplaceTask task, World world) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> startTask(task, world));
            return;
        }
        worldReplacerMap.computeIfAbsent(world, k -> new WorldReplacer(world)).addTask(task);
    }

    public void enable() {
        if (task != null) {
            throw new UnsupportedOperationException("Already started!");
        }
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, 0L, 1L);
    }

    public void close() {
        if (task == null) {
            throw new UnsupportedOperationException("Isn't started!");
        }
        task.cancel();
        task = null;
        worldReplacerMap.values().forEach(WorldReplacer::close);
        worldReplacerMap.clear();
    }

    private class WorldReplacer {
        private final List<ReplaceTaskInfo> taskList = new ArrayList<>();
        private final World world;
        private int warns = 0;
        private long timeOverflow = 0;

        private WorldReplacer(World world) {
            this.world = world;
        }

        private void close() {
            for (ReplaceTaskInfo info : taskList) {
                Pair<Vec3i, ReplaceBlock> pair;
                while ((pair = info.task.next()) != null) {
                    Block bukkitBlock = world.getBlockAt(pair.getLeft().getX(), pair.getLeft().getY(), pair.getLeft().getZ());
                    var filter = info.task.getFilter();
                    if (filter != null) {
                        if (filter.test(bukkitBlock)) continue;
                    }
                    var callBack = info.task.getBlockBreakCallBack();
                    if (callBack != null) {
                        callBack.accept(bukkitBlock);
                    }
                    blockReplacer.replace(pair.getLeft(), pair.getRight(), info.task, world);
                }
            }
        }

        private void tick() {

            if (!this.taskList.isEmpty()) {
                long time = System.currentTimeMillis() + maxTimeOut;
                if (warns >= 5) {
                    message.warning("Skipped %s+ ticks! Server is overload? Time %s. World %s", warns, timeOverflow, world.getName());
                }
                int limitForTask = 6_000 / this.taskList.size();
                limitForTask = Math.max(limitForTask, 1);

                CyclicIterator<ReplaceTaskInfo> iterator = new CyclicIterator<>(taskList);
                main:
                while (iterator.hasNext()) {
                    ReplaceTaskInfo info = iterator.next();
                    if (info.task.isEnd()) {
                        iterator.remove();
                        if (info.task instanceof BlockReplaceTask task0) {
                            task0.getFuture().completeAsync(() -> task0);
                        }
                        continue;
                    }
                    if (!info.doSkip) {
                        int currentLimit = limitForTask;
                        if (info.task.getMaxReplacesInTick() != -1) {
                            currentLimit = info.task.getMaxReplacesInTick();
                        }
                        while (currentLimit > 0) {
                            var pair = info.task.next();
                            if (pair == null) break;
                            Block bukkitBlock = world.getBlockAt(pair.getLeft().getX(), pair.getLeft().getY(), pair.getLeft().getZ());
                            var filter = info.task.getFilter();
                            if (filter != null) {
                                if (filter.test(bukkitBlock)) continue;
                            }
                            var callBack = info.task.getBlockBreakCallBack();
                            if (callBack != null) {
                                callBack.accept(bukkitBlock);
                            }
                            BlockReplacer replacer;
                            if (info.task.getCustomBlockReplacer() != null) {
                                replacer = info.task.getCustomBlockReplacer();
                            } else {
                                replacer = blockReplacer;
                            }
                            replacer.replace(pair.getLeft(), pair.getRight(), info.task, world);
                            currentLimit--;

                            long time1 = System.currentTimeMillis() - time;
                            if (time1 > 0L) {
                                timeOverflow += time1;
                                ++warns;
                                break main;
                            } else {
                                warns = 0;
                                timeOverflow = 0L;
                            }
                        }
                        if (currentLimit <= 0 && info.task.getMaxReplacesInTick() != -1) {
                            info.doSkip = true;
                        }
                        if (info.task instanceof BlockReplaceStream){
                            info.doSkip = true;
                        }
                    }
                    boolean allDoSkip = true;
                    for (ReplaceTaskInfo taskInfo : taskList) {
                        if (!taskInfo.doSkip) {
                            allDoSkip = false;
                            break;
                        }
                    }
                    if (allDoSkip) {
                        break;
                    }
                }
                for (ReplaceTaskInfo taskInfo : taskList) {
                    taskInfo.doSkip = false;
                }
            }
        }

        public void addTask(ReplaceTask task) {
            taskList.add(new ReplaceTaskInfo(task));
        }

        private static class ReplaceTaskInfo {
            final ReplaceTask task;
            boolean doSkip;

            private ReplaceTaskInfo(ReplaceTask task) {
                this.task = task;
            }
        }
    }
}
