package org.by1337.blib.core.unsafe;

import org.by1337.blib.inventory.ItemStackUtil;
import org.by1337.blib.nms.v1_16_5.inventory.ItemStackUtilV165;
import org.by1337.blib.nms.v1_17_1.inventory.ItemStackUtilV171;
import org.by1337.blib.nms.v1_20_4.inventory.ItemStackUtilV204;
import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.unsafe.PluginClasspathUtil;
import org.by1337.blib.util.Version;

public class BLibUnsafeImpl implements BLibUnsafe {
    public static final BLibUnsafeImpl INSTANCE = new BLibUnsafeImpl();
    private final PluginClasspathUtil pluginClasspathUtil = new PluginClasspathUtilImpl();
    private final ItemStackUtil itemStackUtil = switch (Version.VERSION) {
        case V1_16_5 -> new ItemStackUtilV165();
        case V1_17_1 -> new ItemStackUtilV171();
        case V1_20_4 -> new ItemStackUtilV204();
        default -> null;
    };

    @Override
    public PluginClasspathUtil getPluginClasspathUtil() {
        return pluginClasspathUtil;
    }

    @Override
    public ItemStackUtil getItemStackUtil() {
        return itemStackUtil;
    }
}
