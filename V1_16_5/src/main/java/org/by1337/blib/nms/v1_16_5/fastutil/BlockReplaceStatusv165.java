
package org.by1337.blib.nms.v1_16_5.fastutil;

import org.bukkit.block.data.BlockData;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.fastutil.block.BlockReplaceStatus;
import org.by1337.blib.fastutil.block.BlockReplaceTask;
import org.by1337.blib.profiler.IProfiler;

import java.util.HashMap;
import java.util.Map;

public class BlockReplaceStatusv165 extends BlockReplaceStatus {
    final Map<Vec3i, BlockData> oldBlocksMap = new HashMap<>();

    public BlockReplaceStatusv165(IProfiler profiler, BlockReplaceTask task) {
        super(profiler, task);
    }

    @Override
    public Map<Vec3i, BlockData> getOldBlocks() {
        return oldBlocksMap;
    }
}

