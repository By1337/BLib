package org.by1337.blib.block.replacer.type.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Material;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class WeBlockStateBlock implements ReplaceBlock {
    public final BlockState blockState;

    public WeBlockStateBlock(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public Material getType() {
        return BukkitAdapter.adapt(blockState.getBlockType());
    }
}
