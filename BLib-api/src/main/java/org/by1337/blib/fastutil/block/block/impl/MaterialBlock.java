package org.by1337.blib.fastutil.block.block.impl;

import org.bukkit.Material;
import org.by1337.blib.fastutil.block.block.ReplaceBlock;

public class MaterialBlock implements ReplaceBlock {
    public final Material material;

    public MaterialBlock(Material material) {
        this.material = material;
    }
}
