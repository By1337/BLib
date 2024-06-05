package org.by1337.blib.fastutil.block;

import org.bukkit.World;
import org.bukkit.event.world.WorldUnloadEvent;

public interface BlockReplacerManager {
    void startTask(BlockReplaceTask task, World world);
    void onUnload(WorldUnloadEvent event);

    void unload(boolean force);

    default void unload() {
        unload(true);
    }
}
