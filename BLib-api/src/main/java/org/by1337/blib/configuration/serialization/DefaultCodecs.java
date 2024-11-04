package org.by1337.blib.configuration.serialization;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.DataResult;
import blib.com.mojang.serialization.DynamicOps;
import blib.com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.UUID;

public class DefaultCodecs {
    private static final Codec<UUID> UUID = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<UUID> read(DynamicOps<T> ops, T t) {
            return ops.getStringValue(t).flatMap(s -> DataResult.success(java.util.UUID.fromString(s)));
        }

        @Override
        public <T> T write(DynamicOps<T> ops, UUID uuid) {
            return ops.createString(uuid.toString());
        }
    };
    public static <T extends Enum<T>> PrimitiveCodec<T> createEnumCodec(final Class<T> type) {
        return new PrimitiveCodec<>() {
            @Override
            public <T1> DataResult<T> read(DynamicOps<T1> ops, T1 t1) {
                return ops.getStringValue(t1).flatMap(s -> {
                    try {
                        return DataResult.success(Enum.valueOf(type, s));
                    } catch (IllegalArgumentException e) {
                        try {
                            return DataResult.success(Enum.valueOf(type, s.toUpperCase()));
                        } catch (IllegalArgumentException e1) {
                            return DataResult.error(() -> "Unknown enum value: " + s);
                        }
                    }
                });
            }

            @Override
            public <T1> T1 write(DynamicOps<T1> ops, T t) {
                return ops.createString(t.name());
            }
        };
    }
}
