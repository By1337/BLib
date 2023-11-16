package org.by1337.lib;

import org.by1337.api.world.BLocation;
import org.by1337.api.util.Version;


import org.by1337.v1_16_5.command.SummonCommandV1_16_5;
import org.by1337.v1_17.command.SummonCommandV1_17;
import org.by1337.v1_17_1.command.SummonCommandV1_17_1;
import org.by1337.v1_18.command.SummonCommandV1_18;
import org.by1337.v1_18_1.command.SummonCommandV1_18_1;
import org.by1337.v1_18_2.command.SummonCommandV1_18_2;
import org.by1337.v1_19.command.SummonCommandV1_19;
import org.by1337.v1_19_1.command.SummonCommandV1_19_1;
import org.by1337.v1_19_2.command.SummonCommandV1_19_2;
import org.by1337.v1_19_3.command.SummonCommandV1_19_3;
import org.by1337.v1_19_4.command.SummonCommandV1_19_4;
import org.by1337.v1_20_1.command.SummonCommandV1_20_1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonCommand {
    public static void execute(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt){
        if (Version.VERSION == Version.V1_16_5) {
            new SummonCommandV1_16_5().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_17) {
            new SummonCommandV1_17().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_17_1) {
            new SummonCommandV1_17_1().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_18) {
            new SummonCommandV1_18().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_18_1) {
            new SummonCommandV1_18_1().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_18_2) {
            new SummonCommandV1_18_2().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_19) {
            new SummonCommandV1_19().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_19_1) {
            new SummonCommandV1_19_1().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_19_2) {
            new SummonCommandV1_19_2().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_19_3) {
            new SummonCommandV1_19_3().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_19_4) {
            new SummonCommandV1_19_4().spawn(entityType, location, nbt);
        } else if (Version.VERSION == Version.V1_20_1) {
            new SummonCommandV1_20_1().spawn(entityType, location, nbt);
        } else{
            throw new IllegalStateException("Unsupported version");
        }
    }
}
