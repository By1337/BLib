package org.by1337.blib.configuration.adapter.codec;

import blib.com.mojang.serialization.Codec;
import org.by1337.blib.configuration.YamlOps;
import org.by1337.blib.configuration.YamlValue;

public interface YamlCodec<T> {
    T decode(YamlValue value);

    YamlValue encode(T value);

    static <E> YamlCodec<E> codecOf(Codec<E> codec) {
        return new YamlCodec<E>() {
            @Override
            public E decode(YamlValue value) {
                return codec.decode(YamlOps.INSTANCE, value).getOrThrow().getFirst();
            }

            @Override
            public YamlValue encode(E value) {
                return codec.encodeStart(YamlOps.INSTANCE, value).getOrThrow();
            }
        };
    }
}
