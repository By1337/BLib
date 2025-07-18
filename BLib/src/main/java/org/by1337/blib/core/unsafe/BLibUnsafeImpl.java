package org.by1337.blib.core.unsafe;

import org.by1337.blib.core.nms.NmsFactory;
import org.by1337.blib.inventory.FastItemMutator;
import org.by1337.blib.inventory.LegacyFastItemMutator;
import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.unsafe.PluginClasspathUtil;
import org.by1337.blib.util.Version;

public class BLibUnsafeImpl implements BLibUnsafe {
    public static final BLibUnsafeImpl INSTANCE = new BLibUnsafeImpl();
    private final PluginClasspathUtil pluginClasspathUtil = new PluginClasspathUtilImpl();

    private final LegacyFastItemMutator legacyFastItemMutator = Version.is1_20_4orOlder() ? NmsFactory.get().get(LegacyFastItemMutator.class) : null;
    private final FastItemMutator fastItemMutator = Version.is1_20_5orNewer() ? NmsFactory.get().get(FastItemMutator.class) : null;

    @Override
    public PluginClasspathUtil getPluginClasspathUtil() {
        return pluginClasspathUtil;
    }

    @Override
    public LegacyFastItemMutator getLegacyFastItemMutator() {
        return legacyFastItemMutator;
    }

    @Override
    public FastItemMutator getFastItemMutator() {
        return fastItemMutator;
    }
}
