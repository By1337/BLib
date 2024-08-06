package org.by1337.blib.block.replacer.type;

import org.bukkit.Material;

public interface ReplaceBlock {
    Material getType();
    ReplaceBlock withPlaceFlag(int flag);
     int getFlag();
}
