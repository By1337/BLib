package org.by1337.blib.block.replacer.impl;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.block.replacer.BlockReplaceFlags;
import org.by1337.blib.block.replacer.BlockReplacer;
import org.by1337.blib.block.replacer.ReplaceTask;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

@NMSAccessor(forClazz = BlockReplacer.class, forVersions = Version.UNKNOWN)
public class BukkitBlockReplacer implements BlockReplacer {
    @Override
    public Block replace(Vec3i pos, BlockData blockData, ReplaceTask task, World world, int flag) {
        Block block = world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        block.setBlockData(blockData, (flag & BlockReplaceFlags.UPDATE_KNOWN_SHAPE) == 0);
        return block;
    }
}
