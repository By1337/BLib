package org.by1337.blib.nbt.impl;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.by1337.blib.nbt.CompressedNBT;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class CompoundTag extends NBT {
    private final Map<String, NBT> tags = new HashMap<>();

    public CompoundTag() {

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

    @Nullable
    @CanIgnoreReturnValue
    public NBT remove(String name) {
        return tags.remove(name);
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

    public boolean has(String name) {
        return get(name) != null;
    }

    @Nullable
    public NBT get(String name) {
        return tags.get(name);
    }

    public NBT getOrThrow(String name) {
        return Objects.requireNonNull(tags.get(name), "unknown tag " + name);
    }

    public NBT getOrDefault(String name, NBT def) {
        return tags.getOrDefault(name, def);
    }

    public NBT getAndDecompress(String name, NBT def) {
        return has(name) ? getAndDecompress(name) : def;
    }

    public NBT getAndDecompress(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        if (v instanceof CompressedNBT compressedNBT) {
            return compressedNBT.decompress();
        } else if (v instanceof ByteArrNBT byteArrNBT) {
            return new CompressedNBT(byteArrNBT.getValue()).decompress();
        } else {
            throw new IllegalStateException("it is impossible to apply decompress to this type! " + v.getClass().getSimpleName());
        }
    }

    public byte getAsByte(String name, byte def) {
        var v = get(name);
        if (v == null) return def;
        return ((Number) v.getAsObject()).byteValue();
    }

    public byte getAsByte(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        if (v instanceof StringNBT stringNBT) return (byte) (stringNBT.getValue().equals("true") ? 1 : 0);
        return ((Number) v.getAsObject()).byteValue();
    }

    public boolean getAsBoolean(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        if (v instanceof StringNBT stringNBT) return stringNBT.getValue().equals("true");
        return ((Number) v.getAsObject()).byteValue() == 1;
    }

    public boolean getAsBoolean(String name, boolean def) {
        var v = get(name);
        if (v == null) return def;
        if (v instanceof StringNBT stringNBT) return stringNBT.getValue().equals("true");
        return ((Number) v.getAsObject()).byteValue() == 1;
    }

    public double getAsDouble(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        return ((Number) v.getAsObject()).doubleValue();
    }

    public double getAsDouble(String name, double def) {
        var v = get(name);
        if (v == null) return def;
        return ((Number) v.getAsObject()).doubleValue();
    }

    public float getAsFloat(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        return ((Number) v.getAsObject()).floatValue();
    }

    public float getAsFloat(String name, float def) {
        var v = get(name);
        if (v == null) return def;
        return ((Number) v.getAsObject()).floatValue();
    }

    public int getAsInt(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        return ((Number) v.getAsObject()).intValue();
    }

    public int getAsInt(String name, int def) {
        var v = get(name);
        if (v == null) return def;
        return ((Number) v.getAsObject()).intValue();
    }

    public long getAsLong(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        return ((Number) v.getAsObject()).longValue();
    }

    public long getAsLong(String name, long def) {
        var v = get(name);
        if (v == null) return def;
        return ((Number) v.getAsObject()).longValue();
    }

    public short getAsShort(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        return ((Number) v.getAsObject()).shortValue();
    }

    public short getAsShort(String name, short def) {
        var v = get(name);
        if (v == null) return def;
        return ((Number) v.getAsObject()).shortValue();
    }

    public String getAsString(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        return String.valueOf(v.getAsObject());
    }

    public String getAsString(String name, String def) {
        var v = get(name);
        if (v == null) return def;
        return String.valueOf(v.getAsObject());
    }

    public CompoundTag getAsCompoundTag(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        return (CompoundTag) v;
    }

    public CompoundTag getAsCompoundTag(String name, CompoundTag def) {
        var v = get(name);
        if (v == null) return def;
        return (CompoundTag) v;
    }

    public UUID getAsUUID(String name, UUID def) {
        if (!tags.containsKey(name)) return def;
        return getAsUUID(name);
    }

    public UUID getAsUUID(String name) {
        var v = get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        LongArrNBT arr = (LongArrNBT) v;
        return new UUID(arr.getValue()[0], arr.getValue()[1]);
    }

    public <R> List<R> getAsList(String name, Function<NBT, R> function) {
        ListNBT v = (ListNBT) get(name);
        if (v == null) throw new NullPointerException("unknown tag " + name);
        List<R> list = new ArrayList<>();
        for (NBT nbt : v) {
            list.add(function.apply(nbt));
        }
        return list;
    }

    public <R> List<R> getAsList(String name, Function<NBT, R> function, List<R> def) {
        ListNBT v = (ListNBT) get(name);
        if (v == null) return def;
        List<R> list = new ArrayList<>();
        for (NBT nbt : v) {
            list.add(function.apply(nbt));
        }
        return list;
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    @Override
    public Object getAsObject() {
        return tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        for (Map.Entry<String, NBT> entry : tags.entrySet()) {
            String name = entry.getKey();
            sb.append(quoteAndEscape(name)).append(":");
            sb.append(entry.getValue());
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        ;
        sb.append("}");
        return sb.toString();
    }

    public String toStringBeautifier(int lvl) {
        StringBuilder sb = new StringBuilder("{\n");

        for (Map.Entry<String, NBT> entry : tags.entrySet()) {
            NBT nbt = entry.getValue();
            String name = entry.getKey();
            sb.append(" ".repeat(lvl + 4)).append(quoteAndEscape(name)).append(": ").append(nbt.toStringBeautifier(lvl + 4)).append(",\n");
        }
        sb.setLength(sb.length() - 2);
        sb.append("\n").append(" ".repeat(lvl)).append("}");
        return sb.toString();
    }

    @Override
    public String quoteAndEscape(String name) {
        return name.contains("'") ||
                name.contains("\"") ||
                name.contains("\\") ||
                name.contains("{") ||
                name.contains("}") ||
                name.contains(":") ||
                name.contains("[") ||
                name.contains("]") ||
                name.contains(";") ||
                name.contains(" ") ||
                name.contains(",") ? super.quoteAndEscape(name) : name;
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
