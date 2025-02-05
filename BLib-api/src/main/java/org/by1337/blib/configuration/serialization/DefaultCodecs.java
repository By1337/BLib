package org.by1337.blib.configuration.serialization;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.DataResult;
import blib.com.mojang.serialization.DynamicOps;
import blib.com.mojang.serialization.codecs.PrimitiveCodec;
import org.by1337.blib.util.OldEnumFixer;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class DefaultCodecs {
    public static final Codec<UUID> UUID = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<UUID> read(DynamicOps<T> ops, T t) {
            return ops.getStringValue(t).flatMap(s -> DataResult.success(java.util.UUID.fromString(s)));
        }

        @Override
        public <T> T write(DynamicOps<T> ops, UUID uuid) {
            return ops.createString(uuid.toString());
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> PrimitiveCodec<T> createAnyEnumCodec(final Class<T> type) {
        if (OldEnumFixer.isOldEnum(type)) return createOldEnumCodec(type);
        if (type.isEnum()){
            return (PrimitiveCodec<T>) createEnumCodec(type.asSubclass(Enum.class));
        }
        throw new IllegalArgumentException("Unknown enum type: " + type);
    }

    public static <T extends Enum<T>> PrimitiveCodec<T> createEnumCodec(final Class<T> type) {
        if (OldEnumFixer.isOldEnum(type)) return createOldEnumCodec(type);
        return new PrimitiveCodec<>() {
            @Override
            public <T1> DataResult<T> read(DynamicOps<T1> ops, T1 t1) {
                return ops.getStringValue(t1).flatMap(s -> {
                    try {
                        return DataResult.success(Enum.valueOf(type, s));
                    } catch (IllegalArgumentException e) {
                        try {
                            return DataResult.success(Enum.valueOf(type, s.toUpperCase(Locale.ENGLISH)));
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

    public static <T> PrimitiveCodec<T> createOldEnumCodec(final Class<T> type) {
        return new PrimitiveCodec<>() {
            private final Map<String, T> nameToValue = OldEnumFixer.getNameToValueMap(type);
            private final Map<T, String> valueToName = OldEnumFixer.getValueToNameMap(type);

            @Override
            public <T1> DataResult<T> read(DynamicOps<T1> ops, T1 t1) {
                return ops.getStringValue(t1).flatMap(s -> {
                    if (nameToValue.containsKey(s)) {
                        return DataResult.success(nameToValue.get(s));
                    } else {
                        String s1 = s.toUpperCase(Locale.ENGLISH);
                        if (nameToValue.containsKey(s1)) {
                            return DataResult.success(nameToValue.get(s.toUpperCase(Locale.ENGLISH)));
                        } else {
                            return DataResult.error(() -> "Unknown enum value: " + s);
                        }
                    }
                });
            }

            @Override
            public <T1> T1 write(DynamicOps<T1> ops, T t) {
                return ops.createString(valueToName.getOrDefault(t, t.toString()));
            }
        };
    }
}
