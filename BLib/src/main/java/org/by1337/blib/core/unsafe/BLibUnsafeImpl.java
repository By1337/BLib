package org.by1337.blib.core.unsafe;

import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.unsafe.PluginClasspathUtil;

public class BLibUnsafeImpl implements BLibUnsafe {
    public static final BLibUnsafeImpl INSTANCE = new BLibUnsafeImpl();
    private final PluginClasspathUtil pluginClasspathUtil = new PluginClasspathUtilImpl();

    @Override
    public PluginClasspathUtil getPluginClasspathUtil() {
        return pluginClasspathUtil;
    }


}
