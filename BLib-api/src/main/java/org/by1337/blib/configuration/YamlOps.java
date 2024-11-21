package org.by1337.blib.configuration;

import blib.com.mojang.datafixers.util.Pair;
import blib.com.mojang.serialization.DataResult;
import blib.com.mojang.serialization.DynamicOps;
import blib.com.mojang.serialization.MapLike;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class YamlOps implements DynamicOps<YamlValue> {
    public static final YamlOps INSTANCE = new YamlOps();

    protected YamlOps() {
    }

    @Override
    public YamlValue empty() {
        return YamlValue.EMPTY;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, YamlValue input) {
        if (input.isMap()) {
            return convertMap(outOps, input);
        }
        if (input.isList()) {
            return convertList(outOps, input);
        }
        if (input.isEmpty()) {
            return outOps.empty();
        }
        if (input.getValue() instanceof Boolean) {
            return outOps.createBoolean(input.getAsBoolean());
        }
        if (input.getValue() instanceof Number) {
            BigDecimal value = new BigDecimal(input.getAsString());
            try {
                long l = value.longValueExact();
                if ((byte) l == l) {
                    return outOps.createByte((byte) l);
                }
                if ((short) l == l) {
                    return outOps.createShort((short) l);
                }
                if ((int) l == l) {
                    return outOps.createInt((int) l);
                }
                return outOps.createLong(l);
            } catch (ArithmeticException e) {
                double d = value.doubleValue();
                if ((float) d == d) {
                    return outOps.createFloat((float) d);
                }
                return outOps.createDouble(d);
            }
        }
        return outOps.createString(input.getAsString());
    }

    @Override
    public DataResult<Number> getNumberValue(YamlValue input) {
        if (input.getValue() instanceof Number n) {
            return DataResult.success(n);
        }
        try {
            return DataResult.success(input.getAsInteger());
        } catch (NumberFormatException e) {
            return DataResult.error(() -> "Not a number: " + e + " " + input);
        }
    }

    @Override
    public YamlValue createNumeric(Number i) {
        return YamlValue.wrap(i);
    }

    @Override
    public DataResult<String> getStringValue(YamlValue input) {
        if (input.isPrimitive()) {
            return DataResult.success(input.getAsString());
        }
        return DataResult.error(() -> "Not a string: " + input);
    }

    @Override
    public YamlValue createString(String value) {
        return YamlValue.wrap(value);
    }

    @Override
    public DataResult<YamlValue> mergeToList(YamlValue list, YamlValue value) {
        if (!list.isList() && !list.isEmpty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
        }

        List<YamlValue> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.getAsList(Function.identity()));
        }
        result.add(value);
        return DataResult.success(YamlValue.wrap(value));
    }

    @Override
    public DataResult<YamlValue> mergeToList(YamlValue list, List<YamlValue> values) {
        if (!list.isList() && !list.isEmpty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
        }
        List<YamlValue> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.getAsList(Function.identity()));
        }
        result.addAll(values);
        return DataResult.success(YamlValue.wrap(result));
    }

    @Override
    public DataResult<YamlValue> mergeToMap(YamlValue map, YamlValue key, YamlValue value) {
        if (!map.isMap() && !map.isEmpty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        }
        if (!key.isPrimitive()) {
            return DataResult.error(() -> "key is not a primitive: " + key, map);
        }
        Map<YamlValue, YamlValue> result = new HashMap<>();
        if (!map.isEmpty()) {
            result.putAll(map.getAsMap(Function.identity(), Function.identity()));
        }
        result.put(key, value);
        return DataResult.success(YamlValue.wrap(result));
    }

    @Override
    public DataResult<YamlValue> mergeToMap(final YamlValue map, final MapLike<YamlValue> values) {
        if (!map.isMap() && !map.isEmpty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        }
        Map<YamlValue, YamlValue> result = new HashMap<>();
        if (!map.isEmpty()) {
            result.putAll(map.getAsMap(Function.identity(), Function.identity()));
        }
        final List<YamlValue> missed = Lists.newArrayList();
        values.entries().forEach(entry -> {
            final YamlValue key = entry.getFirst();
            if (!key.isPrimitive()) {
                missed.add(key);
                return;
            }
            result.put(key, entry.getSecond());
        });
        if (!missed.isEmpty()) {
            return DataResult.error(() -> "some keys are not primitives: " + missed, YamlValue.wrap(result));
        }
        return DataResult.success(YamlValue.wrap(result));
    }

    @Override
    public DataResult<Stream<Pair<YamlValue, YamlValue>>> getMapValues(YamlValue input) {
        if (!input.isMap()) {
            return DataResult.error(() -> "Not a map: " + input);
        }
        return DataResult.success(input.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue().isEmpty() ? null : e.getValue())));
    }

    @Override
    public DataResult<Consumer<BiConsumer<YamlValue, YamlValue>>> getMapEntries(final YamlValue input) {
        if (!input.isMap()) {
            return DataResult.error(() -> "Not a map: " + input);
        }
        return DataResult.success(c -> {
            for (final Map.Entry<YamlValue, YamlValue> entry : input.entrySet()) {
                c.accept(entry.getKey(), entry.getValue().isEmpty() ? null : entry.getValue());
            }
        });
    }

    @Override
    public DataResult<MapLike<YamlValue>> getMap(final YamlValue input) {
        if (!input.isMap()) {
            return DataResult.error(() -> "Not a map: " + input);
        }
        final Map<YamlValue, YamlValue> map = input.getAsMap(Function.identity(), Function.identity());
        return DataResult.success(new MapLike<>() {
            @Override
            public @Nullable YamlValue get(YamlValue key) {
                var v = map.get(key);
                return v.isEmpty() ? null : v;
            }

            @Override
            public @Nullable YamlValue get(String key) {
                return get(YamlValue.wrap(key));
            }

            @Override
            public Stream<Pair<YamlValue, YamlValue>> entries() {
                return map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue()));
            }
        });
    }

    @Override
    public YamlValue createMap(Stream<Pair<YamlValue, YamlValue>> map) {
        Map<YamlValue, YamlValue> result = new HashMap<>();
        map.forEach(e -> result.put(e.getFirst(), e.getSecond()));
        return YamlValue.wrap(result);
    }

    @Override
    public DataResult<Stream<YamlValue>> getStream(YamlValue input) {
        if (!input.isList()) {
            return DataResult.error(() -> "Not a list: " + input);
        }
        return DataResult.success(input.stream());
    }

    @Override
    public YamlValue createList(Stream<YamlValue> input) {
        List<YamlValue> result = new ArrayList<>();
        input.forEach(result::add);
        return YamlValue.wrap(result);
    }

    @Override
    public YamlValue remove(YamlValue input, String key) {
        if (input.isMap()) {
            Map<YamlValue, YamlValue> map = input.getAsMap(Function.identity(), Function.identity());
            map.remove(YamlValue.wrap(key));
            return YamlValue.wrap(map);
        }
        return input;
    }
}
