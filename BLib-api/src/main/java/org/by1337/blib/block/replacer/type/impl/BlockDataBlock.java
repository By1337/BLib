package org.by1337.blib.block.replacer.type.impl;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class BlockDataBlock implements ReplaceBlock {
    public final BlockData blockData;

    public BlockDataBlock(BlockData blockData) {
        this.blockData = blockData;
    }

    @Override
    public Material getType() {
        return blockData.getMaterial();
    }
}
