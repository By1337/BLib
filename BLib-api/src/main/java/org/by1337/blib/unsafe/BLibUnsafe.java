package org.by1337.blib.unsafe;

import org.by1337.blib.inventory.FastItemMutator;
import org.by1337.blib.inventory.LegacyFastItemMutator;

public interface BLibUnsafe {
    PluginClasspathUtil getPluginClasspathUtil();

    // <=1.20.4
    LegacyFastItemMutator getLegacyFastItemMutator();

    // >=1.20.6
    FastItemMutator getFastItemMutator();
}
