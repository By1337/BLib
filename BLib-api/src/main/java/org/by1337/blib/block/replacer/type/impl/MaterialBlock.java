package org.by1337.blib.block.replacer.type.impl;

import org.bukkit.Material;
import org.by1337.blib.block.replacer.type.ReplaceBlock;

public class MaterialBlock implements ReplaceBlock {
    public static final MaterialBlock AIR = new MaterialBlock(Material.AIR);
    public final Material material;

    public MaterialBlock(Material material) {
        this.material = material;
    }

    @Override
    public Material getType() {
        return material;
    }
}
