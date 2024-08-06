package org.by1337.blib.block.replacer.type.impl;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class BlockDataBlock implements ReplaceBlock {
    public final BlockData blockData;
    public final int flag;

    public BlockDataBlock(BlockData blockData) {
        this.blockData = blockData;
        flag = -1;
    }

    public BlockDataBlock(BlockData blockData, int flag) {
        this.blockData = blockData;
        this.flag = flag;
    }

    @Override
    public Material getType() {
        return blockData.getMaterial();
    }

    @Override
    public ReplaceBlock withPlaceFlag(int flag0) {
        return new BlockDataBlock(blockData, flag0);
    }
    @Override
    public int getFlag() {
        return flag;
    }
}
