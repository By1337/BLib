package org.by1337.blib.fastutil;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.fastutil.block.BlockReplaceTask;
import org.by1337.blib.fastutil.block.BlockReplacerManager;

public class FastUtilApi {
    private static FastUtilApi instance;
    private final Message message;
    private final Plugin plugin;
    private final FastUtilSetting setting;
    private BlockReplacerManager blockReplacerManager;

    public FastUtilApi(Message message, Plugin plugin, FastUtilSetting setting) {
        if (instance != null) {
            throw new UnsupportedOperationException();
        }
        this.setting = setting;
        instance = this;
        this.message = message;
        this.plugin = plugin;
    }

    public static void startTask(BlockReplaceTask task, World world){
        instance.blockReplacerManager.startTask(task, world);
    }

    public void setBlockReplacerManager(BlockReplacerManager blockReplacerManager) {
        if (this.blockReplacerManager != null){
            throw new UnsupportedOperationException();
        }
        this.blockReplacerManager = blockReplacerManager;
    }

    public static Message getMessage() {
        return instance.message;
    }

    public static Plugin getPlugin() {
        return instance.plugin;
    }

    public static FastUtilSetting getSetting() {
        return instance.setting;
    }

    public static BlockReplacerManager getBlockReplacerManager() {
        return instance.blockReplacerManager;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
