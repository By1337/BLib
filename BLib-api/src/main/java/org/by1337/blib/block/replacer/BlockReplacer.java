package org.by1337.blib.block.replacer;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.block.custom.data.CustomBlockData;
import org.by1337.blib.block.custom.registry.WorldRegistry;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.block.replacer.type.impl.BlockDataBlock;
import org.by1337.blib.block.replacer.type.impl.CustomBlockReplace;
import org.by1337.blib.block.replacer.type.impl.MaterialBlock;
import org.by1337.blib.block.replacer.type.impl.WeBlockStateBlock;
import org.by1337.blib.geom.Vec3i;

public interface BlockReplacer {
    default Block replace(Vec3i pos, ReplaceBlock replaceBlock, ReplaceTask task, World world) {
        BlockData data;
        int flag = replaceBlock.getFlag() == -1 ? task.getFlag() : replaceBlock.getFlag();
        if (replaceBlock instanceof BlockDataBlock dataBlock) {
            data = dataBlock.blockData;
        } else if (replaceBlock instanceof MaterialBlock materialBlock) {
            data = materialBlock.material.createBlockData();
        } else if (replaceBlock instanceof WeBlockStateBlock weBlockStateBlock) {
            data = BukkitAdapter.adapt(weBlockStateBlock.blockState);
        } else if (replaceBlock instanceof CustomBlockReplace customBlockReplace) {
            Block block = replace(pos, customBlockReplace.customBlock.createBlockData(), task, world, flag);
            if (block != null) {
                CustomBlockData customBlockData = customBlockReplace.customBlock.doPlace(world, pos.x, pos.y, pos.z);
                WorldRegistry.CUSTOM_BLOCK_REGISTRY.add(world.getName(), pos.x, pos.y, pos.z, customBlockData);
            }
            return block;
        } else {
            throw new UnsupportedOperationException("Unsupported type " + replaceBlock.getClass());
        }
        return replace(pos, data, task, world, flag);
    }

    Block replace(Vec3i pos, BlockData replaceBlock, ReplaceTask task, World world, int replaceFlag);
}
