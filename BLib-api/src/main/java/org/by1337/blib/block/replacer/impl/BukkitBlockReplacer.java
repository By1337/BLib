package org.by1337.blib.block.replacer.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.block.replacer.BlockReplaceFlags;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.block.replacer.type.impl.BlockDataBlock;
import org.by1337.blib.block.replacer.type.impl.MaterialBlock;
import org.by1337.blib.block.replacer.type.impl.WeBlockStateBlock;
import org.by1337.blib.geom.Vec3i;


public class BukkitBlockReplacer implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos, ReplaceBlock replaceBlock, ReplaceTask task, World world) {
        BlockData blockData;
        if (replaceBlock instanceof BlockDataBlock dataBlock) {
            blockData = dataBlock.blockData;
        } else if (replaceBlock instanceof MaterialBlock materialBlock) {
            blockData = materialBlock.material.createBlockData();
        } else if (replaceBlock instanceof WeBlockStateBlock weBlockStateBlock) {
            blockData = BukkitAdapter.adapt(weBlockStateBlock.blockState);
        } else {
            throw new UnsupportedOperationException("Unsupported type " + replaceBlock.getClass());
        }
        Block block = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        block.setBlockData(blockData, (task.getFlag() & BlockReplaceFlags.UPDATE_KNOWN_SHAPE) == 0);
        return block;
    }
}