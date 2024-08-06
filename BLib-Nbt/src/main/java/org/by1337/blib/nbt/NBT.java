package org.by1337.blib.nbt;

public abstract class NBT {
    public abstract NbtType getType();

    @Deprecated
    public String toStringBeautifier(int lvl) {
        return toStringBeautifier();
    }

    public String toStringBeautifier() {
        return toString(NBTToStringStyle.BEAUTIFIER);
    }

    public String toString() {
        return toString(NBTToStringStyle.COMPACT);
    }

    public String toString(NBTToStringStyle style) {
        return NBTToString.appendNBT(this, style, new StringBuilder(), 0).toString();
    }

    public abstract Object getAsObject();

    public void write(NbtByteBuffer buffer) {
        getType().write(buffer, this);
    }

    public CompressedNBT getAsCompressedNBT() {
        return CompressedNBT.compress(this);
    }

    public abstract NBT copy();

    @SuppressWarnings("unchecked")
    public <T extends NBT> T as(Class<T> t) {
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends NBT> T as(Class<T> t, T def) {
        if (!this.getClass().isAssignableFrom(t)) return def;
        return (T) this;
    }
}
