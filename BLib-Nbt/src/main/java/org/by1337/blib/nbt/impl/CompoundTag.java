package org.by1337.blib.nbt.impl;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.by1337.blib.nbt.CompressedNBT;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompoundTag extends NBT {
    private final Map<String, NBT> tags;

    public CompoundTag() {
        tags = new HashMap<>();
    }

    private CompoundTag(Map<String, NBT> tags) {
        this.tags = tags;
    }

    public void putTag(String id, NBT nbt) {
        tags.put(id, nbt);
    }

    public void putString(String id, String s) {
        tags.put(id, new StringNBT(s));
    }

    public void putBoolean(String id, boolean v) {
        tags.put(id, ByteNBT.valueOf((byte) (v ? 1 : 0)));
    }

    public void putByteArray(String id, byte[] byteArray) {
        tags.put(id, new ByteArrNBT(byteArray));
    }

    public void putByte(String id, byte v) {
        tags.put(id, ByteNBT.valueOf(v));
    }

    public void putDouble(String id, double v) {
        tags.put(id, new DoubleNBT(v));
    }

    public void putFloat(String id, float v) {
        tags.put(id, new FloatNBT(v));
    }

    public void putIntArray(String id, int[] intArray) {
        tags.put(id, new IntArrNBT(intArray));
    }

    public void putInt(String id, int v) {
        tags.put(id, IntNBT.valueOf(v));
    }

    public void putList(String id, List<? extends NBT> list) {
        tags.put(id, new ListNBT((List<NBT>) list));
    }

    public <T> void putList(String id, List<T> list, Function<T, NBT> function) {
        List<NBT> list1 = new ArrayList<>();
        for (T raw : list) {
            list1.add(function.apply(raw));
        }
        tags.put(id, new ListNBT(list1));
    }

    public void putLongArray(String id, long[] longArray) {
        tags.put(id, new LongArrNBT(longArray));
    }

    public void putLong(String id, long v) {
        tags.put(id, LongNBT.valueOf(v));
    }

    public void putShort(String id, short v) {
        tags.put(id, ShortNBT.valueOf(v));
    }

    public void putUUID(String key, UUID uuid) {
        tags.put(key, new LongArrNBT(new long[]{uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()}));
    }

    public void putMojangUUID(String key, UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        tags.put(key, new IntArrNBT(leastMostToIntArray(most, least)));
    }

    private static int[] leastMostToIntArray(long most, long least) {
        return new int[]{(int) (most >> 32), (int) most, (int) (least >> 32), (int) least};
    }

    @Nullable
    @CanIgnoreReturnValue
    public NBT remove(String key) {
        return tags.remove(key);
    }

    public int getSize() {
        int size = 0;
        for (Map.Entry<String, NBT> entry : tags.entrySet()) {
            size += entry.getKey().getBytes(StandardCharsets.UTF_8).length;
            size += getSize(entry.getValue());
        }
        return size;
    }

    public static int getSize(NBT nbt) {
        if (nbt instanceof ByteArrNBT arr) {
            return arr.getValue().length;
        } else if (nbt instanceof ByteNBT) {
            return 1;
        } else if (nbt instanceof CompoundTag compoundTag) {
            return compoundTag.getSize();
        } else if (nbt instanceof DoubleNBT) {
            return 8;
        } else if (nbt instanceof FloatNBT) {
            return 4;
        } else if (nbt instanceof IntArrNBT arr) {
            return (arr.getValue().length * 4);
        } else if (nbt instanceof IntNBT) {
            return 4;
        } else if (nbt instanceof LongArrNBT arr) {
            return (arr.getValue().length * 8);
        } else if (nbt instanceof LongNBT) {
            return 8;
        } else if (nbt instanceof ShortNBT) {
            return 2;
        } else if (nbt instanceof StringNBT stringNBT) {
            return stringNBT.getValue().getBytes(StandardCharsets.UTF_8).length;
        } else if (nbt instanceof ListNBT list) {
            int size = 0;
            for (NBT nbt1 : list) {
                size += getSize(nbt1);
            }
            return size;
        }
        return 0;
    }

    public boolean has(String key) {
        return get(key) != null;
    }

    public boolean has(String key, Class<? extends NBT> type) {
        NBT nbt = get(key);
        return nbt != null && nbt.getClass() == type;
    }

    public boolean has(String key, NbtType type) {
        NBT nbt = get(key);
        return nbt != null && nbt.getType() == type;
    }

    @Nullable
    public NBT get(String key) {
        return tags.get(key);
    }

    public NBT getOrThrow(String key) {
        return Objects.requireNonNull(tags.get(key), "unknown tag " + key);
    }

    public NBT getOrDefault(String key, NBT def) {
        return tags.getOrDefault(key, def);
    }

    public NBT getAndDecompress(String key, NBT def) {
        return has(key) ? getAndDecompress(key) : def;
    }

    public NBT getAndDecompress(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        if (v instanceof CompressedNBT compressedNBT) {
            return compressedNBT.decompress();
        } else if (v instanceof ByteArrNBT byteArrNBT) {
            return new CompressedNBT(byteArrNBT.getValue()).decompress();
        } else {
            throw new IllegalStateException("it is impossible to apply decompress to this type! " + v.getClass().getSimpleName());
        }
    }

    public byte getAsByte(String key, byte def) {
        var v = get(key);
        if (v == null) return def;
        return ((Number) v.getAsObject()).byteValue();
    }

    public byte getAsByte(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        if (v instanceof StringNBT stringNBT) return (byte) (stringNBT.getValue().equals("true") ? 1 : 0);
        return ((Number) v.getAsObject()).byteValue();
    }

    public boolean getAsBoolean(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        if (v instanceof StringNBT stringNBT) return stringNBT.getValue().equals("true");
        return ((Number) v.getAsObject()).byteValue() == 1;
    }

    public boolean getAsBoolean(String key, boolean def) {
        var v = get(key);
        if (v == null) return def;
        if (v instanceof StringNBT stringNBT) return stringNBT.getValue().equals("true");
        return ((Number) v.getAsObject()).byteValue() == 1;
    }

    public double getAsDouble(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return ((Number) v.getAsObject()).doubleValue();
    }

    public double getAsDouble(String key, double def) {
        var v = get(key);
        if (v == null) return def;
        return ((Number) v.getAsObject()).doubleValue();
    }

    public float getAsFloat(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return ((Number) v.getAsObject()).floatValue();
    }

    public float getAsFloat(String key, float def) {
        var v = get(key);
        if (v == null) return def;
        return ((Number) v.getAsObject()).floatValue();
    }

    public int getAsInt(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return ((Number) v.getAsObject()).intValue();
    }

    public int getAsInt(String key, int def) {
        var v = get(key);
        if (v == null) return def;
        return ((Number) v.getAsObject()).intValue();
    }

    public long getAsLong(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return ((Number) v.getAsObject()).longValue();
    }

    public long getAsLong(String key, long def) {
        var v = get(key);
        if (v == null) return def;
        return ((Number) v.getAsObject()).longValue();
    }

    public short getAsShort(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return ((Number) v.getAsObject()).shortValue();
    }

    public short getAsShort(String key, short def) {
        var v = get(key);
        if (v == null) return def;
        return ((Number) v.getAsObject()).shortValue();
    }

    public String getAsString(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return String.valueOf(v.getAsObject());
    }

    public String getAsString(String key, String def) {
        var v = get(key);
        if (v == null) return def;
        return String.valueOf(v.getAsObject());
    }

    public CompoundTag getAsCompoundTag(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return (CompoundTag) v;
    }

    public CompoundTag getAsCompoundTag(String key, CompoundTag def) {
        var v = get(key);
        if (v == null) return def;
        return (CompoundTag) v;
    }

    public UUID getAsUUID(String key, UUID def) {
        if (!tags.containsKey(key)) return def;
        return getAsUUID(key);
    }

    public UUID getAsUUID(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        LongArrNBT arr = (LongArrNBT) v;
        return new UUID(arr.getValue()[0], arr.getValue()[1]);
    }

    public UUID getAsUnknownUUID(String key) {
        if (has(key, NbtType.LONG_ARR)) {
            return getAsUUID(key);
        } else if (has(key, NbtType.INT_ARR)) {
            return getAsMojangUUID(key);
        } else {
            NBT nbt = get(key);
            throw new IllegalStateException(
                    String.format(
                            "Expected UUID-Tag to be of type %s or %s, but found %s",
                            NbtType.LONG_ARR,
                            NbtType.INT_ARR,
                            nbt == null ? "null" : nbt.getType()
                    )
            );
        }
    }

    public UUID getAsMojangUUID(String key) {
        return uuidFromIntArray(getAsIntArray(key));
    }

    private static UUID uuidFromIntArray(int[] arr) {
        if (arr.length != 4) {
            throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + arr.length + ".");
        }
        return new UUID((long) arr[0] << 32 | (long) arr[1] & 0xFFFFFFFFL, (long) arr[2] << 32 | (long) arr[3] & 0xFFFFFFFFL);
    }

    public <R> List<R> getAsList(String key, Function<NBT, R> function) {
        ListNBT v = (ListNBT) get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        List<R> list = new ArrayList<>();
        for (NBT nbt : v) {
            list.add(function.apply(nbt));
        }
        return list;
    }

    public <R> List<R> getAsList(String key, Function<NBT, R> function, List<R> def) {
        ListNBT v = (ListNBT) get(key);
        if (v == null) return def;
        List<R> list = new ArrayList<>();
        for (NBT nbt : v) {
            list.add(function.apply(nbt));
        }
        return list;
    }

    public <T extends NBT> T computeIfAbsent(String key, Supplier<T> mappingFunction) {
        return computeIfAbsent(key, k -> mappingFunction.get());
    }

    public <T extends NBT> T computeIfAbsent(String key, Function<String, T> mappingFunction) {
        var v = tags.get(key);
        if (v == null) {
            v = mappingFunction.apply(key);
            tags.put(key, v);
        }
        return (T) v;
    }

    public ListNBT getAsListNBT(String key) {
        var v = get(key);
        if (v == null) throw new NullPointerException("unknown tag " + key);
        return (ListNBT) v;
    }

    public ListNBT getAsListNBT(String key, ListNBT def) {
        var v = get(key);
        if (v == null) return def;
        return (ListNBT) v;
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> List<T> getAsList(String key, Class<T> tClass) {
        ListNBT current = getAsListNBT(key, new ListNBT());
        List<T> result = new ArrayList<>();
        for (NBT nbt : current) {
            if (nbt.getClass() == tClass) {
                result.add((T) nbt);
            }
        }
        return result;
    }

    public ListNBT getListNBT(String key, NbtType type) {
        ListNBT current = getAsListNBT(key, new ListNBT());
        ListNBT newList = new ListNBT();
        for (NBT nbt : current) {
            if (nbt.getType() == type) {
                newList.add(nbt);
            }
        }
        return newList;
    }

    public int[] getAsIntArray(String key, int[] def) {
        if (!tags.containsKey(key)) return def;
        return getAsIntArray(key);
    }

    public int[] getAsIntArray(String key) {
        return Objects.requireNonNull(((IntArrNBT) tags.get(key)), "unknown tag " + key).getValue();
    }

    public long[] getAsLongArray(String key, long[] def) {
        if (!tags.containsKey(key)) return def;
        return getAsLongArray(key);
    }

    public long[] getAsLongArray(String key) {
        return Objects.requireNonNull(((LongArrNBT) tags.get(key)), "unknown tag " + key).getValue();
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    @Override
    public Object getAsObject() {
        return tags;
    }


    @Override
    public NbtType getType() {
        return NbtType.COMPOUND;
    }


    public Map<String, NBT> getTags() {
        return tags;
    }

    public Set<Map.Entry<String, NBT>> entrySet() {
        return tags.entrySet();
    }

    @Override
    public CompoundTag copy() {
        Map<String, NBT> tags2 = new HashMap<>(tags.size());
        for (String s : tags.keySet()) {
            tags2.put(s, tags.get(s).copy());
        }
        return new CompoundTag(tags2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompoundTag that = (CompoundTag) o;
        return Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags);
    }


}
