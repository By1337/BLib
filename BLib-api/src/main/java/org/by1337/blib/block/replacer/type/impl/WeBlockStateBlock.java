package org.by1337.blib.block.replacer.type.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Material;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class WeBlockStateBlock implements ReplaceBlock {
    public final BlockState blockState;
    public final int flag;

    public WeBlockStateBlock(BlockState blockState) {
        this.blockState = blockState;
        flag = -1;
    }

    public WeBlockStateBlock(BlockState blockState, int flag) {
        this.blockState = blockState;
        this.flag = flag;
    }

    @Override
    public Material getType() {
        return BukkitAdapter.adapt(blockState.getBlockType());
    }

    @Override
    public ReplaceBlock withPlaceFlag(int flag0) {
        return new WeBlockStateBlock(blockState, flag0);
    }
    @Override
    public int getFlag() {
        return flag;
    }
}
