package org.by1337.blib.unsafe;

import org.by1337.blib.inventory.ItemStackUtil;
import org.jetbrains.annotations.ApiStatus;

public interface BLibUnsafe {
    PluginClasspathUtil getPluginClasspathUtil();
    @ApiStatus.Experimental
    ItemStackUtil getItemStackUtil();
}
