package org.by1337.blib.block.replacer;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.geom.Vec3i;

public interface BlockReplacer {
    Block replace(Vec3i pos, ReplaceBlock replaceBlock, ReplaceTask task, World world);
}
