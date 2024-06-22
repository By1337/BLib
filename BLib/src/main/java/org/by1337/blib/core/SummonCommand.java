package org.by1337.blib.core;

import org.by1337.blib.nms.V1_20_6.command.SummonCommandV1_20_6;
import org.by1337.blib.util.Version;
import org.by1337.blib.world.BLocation;
import org.by1337.blib.nms.v1_16_5.command.SummonCommandV1_16_5;
import org.by1337.blib.nms.v1_17.command.SummonCommandV1_17;
import org.by1337.blib.nms.v1_17_1.command.SummonCommandV1_17_1;
import org.by1337.blib.nms.v1_18_2.command.SummonCommandV1_18_2;
import org.by1337.blib.nms.v1_19_4.command.SummonCommandV1_19_4;
import org.by1337.blib.nms.v1_20_1.command.SummonCommandV1_20_1;
import org.by1337.blib.nms.v1_20_2.command.SummonCommandV1_20_2;
import org.by1337.blib.nms.v1_20_4.command.SummonCommandV1_20_4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonCommand {
    public static void execute(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        switch (Version.VERSION) {
            case V1_16_5 -> new SummonCommandV1_16_5().spawn(entityType, location, nbt);
            case V1_17 -> new SummonCommandV1_17().spawn(entityType, location, nbt);
            case V1_17_1 -> new SummonCommandV1_17_1().spawn(entityType, location, nbt);
            case V1_18_2 -> new SummonCommandV1_18_2().spawn(entityType, location, nbt);
            case V1_19_4 -> new SummonCommandV1_19_4().spawn(entityType, location, nbt);
            case V1_20_1 -> new SummonCommandV1_20_1().spawn(entityType, location, nbt);
            case V1_20_2 -> new SummonCommandV1_20_2().spawn(entityType, location, nbt);
            case V1_20_4 -> new SummonCommandV1_20_4().spawn(entityType, location, nbt);
            case V1_20_5, V1_20_6 -> new SummonCommandV1_20_6().spawn(entityType, location, nbt);
            default -> throw new IllegalStateException("Unsupported version! use 1.16.5, 1.17.1, 1.18.2, 1.19.4, 1.20.(1|2|3|4)");
        }
    }

}
