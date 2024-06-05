package org.by1337.blib.fastutil.block.block.impl;

import org.bukkit.block.data.BlockData;
import org.by1337.blib.fastutil.block.block.ReplaceBlock;

public class BlockDataBlock implements ReplaceBlock {
    public final BlockData blockData;

    public BlockDataBlock(BlockData blockData) {
        this.blockData = blockData;
    }
}
