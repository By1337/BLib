
package org.by1337.blib.nms.v1_16_5.fastutil;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.fastutil.FastUtilSetting;
import org.by1337.blib.fastutil.block.BlockReplaceTask;
import org.by1337.blib.fastutil.block.BlockReplacerManager;

import java.util.HashMap;
import java.util.Map;

public class BlockReplacerManagerv165 implements BlockReplacerManager {
    private final Map<World, BlockReplacerProcessorV165> blockReplacerMap = new HashMap<>();
    private final FastUtilSetting setting;
    private final Message message;
    private final Plugin plugin;

    public BlockReplacerManagerv165(FastUtilSetting setting, Message message, Plugin plugin) {
        this.setting = setting;
        this.message = message;
        this.plugin = plugin;
    }

    @Override
    public void startTask(BlockReplaceTask task, World world) {
        if (!Bukkit.isPrimaryThread()){
            Bukkit.getScheduler().runTask(plugin, () -> startTask(task, world));
            return;
        }
        blockReplacerMap.computeIfAbsent(world, k -> new BlockReplacerProcessorV165(setting, message, k, plugin)).addTask(task);
    }

    @Override
    public void onUnload(WorldUnloadEvent event) {
        var replacer = blockReplacerMap.remove(event.getWorld());
        if (replacer != null) {
            replacer.unload();
        }
    }

    @Override
    public void unload(boolean force) {
        for (BlockReplacerProcessorV165 value : blockReplacerMap.values()) {
            value.unload(force);
        }
    }
}
