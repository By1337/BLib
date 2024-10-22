package org.by1337.blib.nbt;

import blib.com.mojang.datafixers.util.Pair;
import blib.com.mojang.serialization.DataResult;
import blib.com.mojang.serialization.DynamicOps;
import blib.com.mojang.serialization.MapLike;
import com.google.common.collect.Lists;
import org.by1337.blib.nbt.impl.*;
import org.by1337.blib.text.MessageFormatter;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Stream;

public class NbtOps implements DynamicOps<NBT> {
    public static final NbtOps INSTANCE = new NbtOps();
    public static final NBT EMPTY = new StringNBT(null);

    protected NbtOps() {
    }

    @Override
    public NBT empty() {
        return EMPTY;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, NBT nbt) {
        return switch (nbt.getType()) {
            case BYTE -> outOps.createByte(((ByteNBT) nbt).getValue());
            case DOUBLE -> outOps.createDouble(((DoubleNBT) nbt).getValue());
            case FLOAT -> outOps.createFloat(((FloatNBT) nbt).getValue());
            case INT -> outOps.createInt(((IntNBT) nbt).getValue());
            case LONG -> outOps.createLong(((LongNBT) nbt).getValue());
            case SHORT -> outOps.createShort(((ShortNBT) nbt).getValue());
            case STRING -> outOps.createString(((StringNBT) nbt).getValue());
            case BYTE_ARR -> outOps.createByteList(ByteBuffer.wrap(((ByteArrNBT) nbt).getValue()));
            case INT_ARR -> outOps.createIntList(Arrays.stream(((IntArrNBT) nbt).getValue()));
            case LONG_ARR -> outOps.createLongList(Arrays.stream(((IntArrNBT) nbt).getValue()).asLongStream());
            case LIST -> convertList(outOps, nbt);
            case COMPOUND -> convertMap(outOps, nbt);
        };
    }

    @Override
    public DataResult<Number> getNumberValue(NBT nbt) {
        if (nbt instanceof NumericNBT n) {
            return DataResult.success(n.getAsNumber());
        } else {
            return DataResult.error(() -> "Not a number");
        }
    }

    @Override
    public NBT createNumeric(Number number) {
        return new DoubleNBT(number.doubleValue());
    }

    @Override
    public NBT createByte(byte value) {
        return ByteNBT.valueOf(value);
    }

    @Override
    public NBT createShort(short value) {
        return ShortNBT.valueOf(value);
    }

    @Override
    public NBT createInt(int value) {
        return IntNBT.valueOf(value);
    }

    @Override
    public NBT createLong(long value) {
        return LongNBT.valueOf(value);
    }

    @Override
    public NBT createFloat(float value) {
        return new FloatNBT(value);
    }

    @Override
    public NBT createDouble(double value) {
        return new DoubleNBT(value);
    }

    @Override
    public DataResult<String> getStringValue(NBT nbt) {
        if (nbt instanceof StringNBT n) {
            return DataResult.success(n.getValue());
        } else {
            return DataResult.error(() -> "Not a string");
        }
    }

    @Override
    public NBT createString(String s) {
        return new StringNBT(s);
    }

    @Override
    public DataResult<NBT> mergeToList(NBT list, NBT val) {
        if (list instanceof ListNBT || list == EMPTY) {
            ListNBT listNBT;
            if (list instanceof ListNBT l){
                listNBT = new ListNBT(l.getList(), l.isAllowMultipleType());
            }else {
                listNBT = new ListNBT();
            }
            try {
                listNBT.add(val);
            } catch (ListNBT.NBTCastException e) {
                return DataResult.error(e::getMessage);
            }
            return DataResult.success(listNBT);
        } else if (val instanceof NumericNBT num) {
            if (list instanceof ByteArrNBT arr) {
                byte[] res = new byte[arr.getValue().length + 1];
                System.arraycopy(arr.getValue(), 0, res, 0, arr.getValue().length);
                res[res.length - 1] = num.byteValue();
                return DataResult.success(new ByteArrNBT(res));
            } else if (list instanceof IntArrNBT arr) {
                int[] res = new int[arr.getValue().length + 1];
                System.arraycopy(arr.getValue(), 0, res, 0, arr.getValue().length);
                res[res.length - 1] = num.byteValue();
                return DataResult.success(new IntArrNBT(res));
            } else if (list instanceof LongArrNBT arr) {
                long[] res = new long[arr.getValue().length + 1];
                System.arraycopy(arr.getValue(), 0, res, 0, arr.getValue().length);
                res[res.length - 1] = num.byteValue();
                return DataResult.success(new LongArrNBT(res));
            } else {
                return DataResult.error(() -> MessageFormatter.apply("Unable to add {} to {}", val.getType(), list.getType()));
            }
        } else {
            return DataResult.error(() -> MessageFormatter.apply("Unable to add {} to {}", val.getType(), list.getType()));
        }
    }

    @Override
    public DataResult<NBT> mergeToMap(NBT map, NBT key, NBT value) {
        if (!(map instanceof CompoundTag) && map != EMPTY) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        }
        if (!(key instanceof StringNBT)) {
            return DataResult.error(() -> "key is not a string: " + key, map);
        }
        CompoundTag newTag;
        if (map instanceof CompoundTag) {
            newTag =  (CompoundTag) map.copy();
        }else {
            newTag = new CompoundTag();
        }
        newTag.putTag(((StringNBT) key).getValue(), value);
        return DataResult.success(newTag);
    }

    @Override
    public DataResult<NBT> mergeToMap(NBT map, MapLike<NBT> values) {
        if (!(map instanceof CompoundTag) && map != EMPTY) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        }
        CompoundTag newTag;
        if (map instanceof CompoundTag) {
            newTag =  (CompoundTag) map.copy();
        }else {
            newTag = new CompoundTag();
        }
        final List<NBT> missed = Lists.newArrayList();
        values.entries().forEach(entry -> {
            final NBT key = entry.getFirst();
            if (!(key instanceof StringNBT)) {
                missed.add(key);
                return;
            }
            newTag.putTag(((StringNBT) key).getValue(), entry.getSecond());
        });
        if (!missed.isEmpty()) {
            return DataResult.error(() -> "some keys are not string: " + missed, newTag);
        }
        return DataResult.success(newTag);
    }

    @Override
    public DataResult<Stream<Pair<NBT, NBT>>> getMapValues(NBT map) {
        if (!(map instanceof CompoundTag) && map != EMPTY) {
            return DataResult.error(() -> "Not a map: " + map);
        }
        if (map instanceof CompoundTag tag){
            return DataResult.success(tag.getTags().entrySet().stream().map(e -> Pair.of(new StringNBT(e.getKey()), e.getValue() == EMPTY ? null : e.getValue())));
        }
        return DataResult.success(Stream.empty());
    }

    @Override
    public NBT createMap(Stream<Pair<NBT, NBT>> stream) {
        CompoundTag newTag = new CompoundTag();
        stream.forEach(p -> newTag.putTag(String.valueOf(p.getFirst().getAsObject()), p.getSecond()));
        return newTag;
    }


    @Override
    public DataResult<MapLike<NBT>> getMap(NBT map) {
        if (!(map instanceof CompoundTag )&& map != EMPTY) {
            return DataResult.error(() -> "Not a map: " + map);
        }
        Map<String, NBT> nbtMap;
        if (map instanceof CompoundTag tag){
            nbtMap = new HashMap<>(tag.getTags());
        }else {
            nbtMap = Collections.emptyMap();
        }
        return DataResult.success(new MapLike<>() {
            @Override
            public @Nullable NBT get(NBT nbt) {
                if (nbt instanceof StringNBT s) {
                    return nbtMap.get(s.getValue());
                }
                return null;
            }

            @Override
            public @Nullable NBT get(String s) {
                return nbtMap.get(s);
            }

            @Override
            public Stream<Pair<NBT, NBT>> entries() {
                return nbtMap.entrySet().stream().map(e -> Pair.of(new StringNBT(e.getKey()), e.getValue()));
            }
        });
    }

    @Override
    public DataResult<Stream<NBT>> getStream(NBT nbt) {
        if (nbt == EMPTY) return DataResult.success(Stream.empty());
        if (nbt instanceof ListNBT listNBT) {
            return DataResult.success(listNBT.stream());
        } else if (nbt instanceof ByteArrNBT arr) {
            return DataResult.success(arr.stream().map(ByteNBT::valueOf));
        } else if (nbt instanceof IntArrNBT arr) {
            return DataResult.success(arr.stream().map(IntNBT::valueOf));
        } else if (nbt instanceof LongArrNBT arr) {
            return DataResult.success(arr.stream().map(LongNBT::valueOf));
        }
        return DataResult.error(() -> "Not a list: " + nbt.getType());
    }


    @Override
    public NBT createList(Stream<NBT> stream) {
        List<NBT> list = new ArrayList<>();
        stream.forEach(list::add);
        if (list.isEmpty()) return new ListNBT();
        if (list.stream().allMatch(n -> n.getType() == NbtType.BYTE)) {
            byte[] arr = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = ((ByteNBT) list.get(i)).byteValue();
            }
            return new ByteArrNBT(arr);
        }
        if (list.stream().allMatch(n -> n.getType() == NbtType.INT)) {
            int[] arr = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = ((IntNBT) list.get(i)).byteValue();
            }
            return new IntArrNBT(arr);
        }
        if (list.stream().allMatch(n -> n.getType() == NbtType.LONG)) {
            long[] arr = new long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = ((LongNBT) list.get(i)).byteValue();
            }
            return new LongArrNBT(arr);
        }
        return new ListNBT(list);
    }


    @Override
    public NBT remove(NBT nbt, String s) {
        if (nbt instanceof CompoundTag tag) {
            CompoundTag t = tag.copy();
            t.remove(s);
            return t;
        }
        return nbt;
    }

    @Override
    public NBT emptyMap() {
        return new CompoundTag();
    }

    @Override
    public NBT emptyList() {
        return new ListNBT();
    }


    @Override
    public DataResult<ByteBuffer> getByteBuffer(NBT input) {
        if (input instanceof ByteArrNBT arr) {
            return DataResult.success(ByteBuffer.wrap(arr.getValue()));
        }
        return DynamicOps.super.getByteBuffer(input);
    }

    @Override
    public NBT createByteList(ByteBuffer input) {
        return new ByteArrNBT(input.array());
    }
}
