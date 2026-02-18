package org.by1337.blib.unsafe;

import org.by1337.blib.inventory.FastItemMutator;
import org.by1337.blib.inventory.LegacyFastItemMutator;

public interface BLibUnsafe {
    BLibUnsafe INSTANCE = new BLibUnsafe() {
        @Override
        public PluginClasspathUtil getPluginClasspathUtil() {
            return PluginClasspathUtil.INSTANCE;
        }

        @Override
        public LegacyFastItemMutator getLegacyFastItemMutator() {
            throw null;
        }

        @Override
        public FastItemMutator getFastItemMutator() {
            return FastItemMutator.INSTANCE;
        }
    };
    PluginClasspathUtil getPluginClasspathUtil();

    // <=1.20.4
    LegacyFastItemMutator getLegacyFastItemMutator();

    // >=1.20.6
    FastItemMutator getFastItemMutator();
}
