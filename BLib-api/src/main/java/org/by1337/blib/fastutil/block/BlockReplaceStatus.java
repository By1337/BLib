package org.by1337.blib.fastutil.block;

import org.bukkit.block.data.BlockData;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.profiler.IProfiler;

import java.util.Map;

public abstract class BlockReplaceStatus {
    private final IProfiler profiler;
    private final BlockReplaceTask task;

    public BlockReplaceStatus(IProfiler profiler, BlockReplaceTask task) {
        this.profiler = profiler;
        this.task = task;
    }

    public IProfiler getProfiler() {
        return profiler;
    }

    public BlockReplaceTask getTask() {
        return task;
    }
    public abstract void undo();
    public abstract Map<Vec3i, BlockData> getOldBlocks();
}
