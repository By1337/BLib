package org.by1337.blib.fastutil.block.block.impl;

import com.sk89q.worldedit.world.block.BlockState;
import org.by1337.blib.fastutil.block.block.ReplaceBlock;

public class WeBlockStateBlock implements ReplaceBlock {
    public final BlockState blockState;

    public WeBlockStateBlock(BlockState blockState) {
        this.blockState = blockState;
    }
}
