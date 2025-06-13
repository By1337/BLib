package org.by1337.blib.core.unsafe;

import org.by1337.blib.inventory.FastItemMutator;
import org.by1337.blib.inventory.LegacyFastItemMutator;
import org.by1337.blib.nms.V1_20_6.inventory.FastItemMutatorV1_20_6;
import org.by1337.blib.nms.V1_21.inventory.FastItemMutatorV1_21;
import org.by1337.blib.nms.V1_21.inventory.FastItemMutatorV1_21_1;
import org.by1337.blib.nms.V1_21_3.inventory.FastItemMutatorV1_21_3;
import org.by1337.blib.nms.V1_21_4.inventory.FastItemMutatorV1_21_4;
import org.by1337.blib.nms.V1_21_5.inventory.FastItemMutatorV1_21_5;
import org.by1337.blib.nms.v1_16_5.inventory.LegacyFastItemMutatorV1_16_5;
import org.by1337.blib.nms.v1_17_1.inventory.LegacyFastItemMutatorV1_17_1;
import org.by1337.blib.nms.v1_18_2.inventory.LegacyFastItemMutatorV1_18_2;
import org.by1337.blib.nms.v1_19_4.inventory.LegacyFastItemMutatorV1_19_4;
import org.by1337.blib.nms.v1_20_1.inventory.LegacyFastItemMutatorV1_20_1;
import org.by1337.blib.nms.v1_20_2.inventory.LegacyFastItemMutatorV1_20_2;
import org.by1337.blib.nms.v1_20_4.inventory.LegacyFastItemMutatorV1_20_4;
import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.unsafe.PluginClasspathUtil;
import org.by1337.blib.util.Version;

public class BLibUnsafeImpl implements BLibUnsafe {
    public static final BLibUnsafeImpl INSTANCE = new BLibUnsafeImpl();
    private final PluginClasspathUtil pluginClasspathUtil = new PluginClasspathUtilImpl();

    private final LegacyFastItemMutator legacyFastItemMutator = switch (Version.VERSION) {
        case V1_16_5 -> new LegacyFastItemMutatorV1_16_5();
        case V1_17_1 -> new LegacyFastItemMutatorV1_17_1();
        case V1_18_2 -> new LegacyFastItemMutatorV1_18_2();
        case V1_19_4 -> new LegacyFastItemMutatorV1_19_4();
        case V1_20_1 -> new LegacyFastItemMutatorV1_20_1();
        case V1_20_2 -> new LegacyFastItemMutatorV1_20_2();
        case V1_20_4, V1_20_3 -> new LegacyFastItemMutatorV1_20_4();
        default -> null;
    };
    private final FastItemMutator fastItemMutator = switch (Version.VERSION) {
        case V1_20_6, V1_20_5 -> new FastItemMutatorV1_20_6();
        case V1_21 -> new FastItemMutatorV1_21();
        case V1_21_1 -> new FastItemMutatorV1_21_1();
        case V1_21_3 -> new FastItemMutatorV1_21_3();
        case V1_21_4 -> new FastItemMutatorV1_21_4();
        case V1_21_5 -> new FastItemMutatorV1_21_5();
        default -> null;
    };

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
