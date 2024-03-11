package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ListNBT extends NBT implements Iterable<NBT> {

    private final List<NBT> list;
    private NbtType type;

    public ListNBT() {
        this(new ArrayList<>());
    }

    public ListNBT(List<NBT> list) {
        this.list = list;
        if (!list.isEmpty()){
            type = list.get(0).getType();
            for (NBT nbt : list) {
                if (type != nbt.getType()) {
                    throw new NBTCastException("type " + type + " to " + nbt.getType());
                }
            }
        }
    }

    public void add(NBT nbt) {
        if (type != null) {
            if (type != nbt.getType()) {
                throw new NBTCastException("type " + type + " to " + nbt.getType());
            }
        } else {
            type = nbt.getType();
        }
        list.add(nbt);
    }

    @Nullable
    public NbtType getInnerType(){
        return type;
    }

    @Override
    public Object getAsObject() {
        return list;
    }
    @Override
    public String toString() {
        return listToString();
    }

    public List<NBT> getList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListNBT listNBT = (ListNBT) o;
        return Objects.equals(list, listNBT.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    public String listToString() {
        StringBuilder sb = new StringBuilder("[");
        for (NBT v : list) {
            sb.append(v).append(",");
        }
        if (!list.isEmpty())
            sb.setLength(sb.length() - 1);
        return sb.append("]").toString();
    }

    @Override
    public String toStringBeautifier(int lvl) {
        String space = " ".repeat(lvl + 4);
        StringBuilder sb = new StringBuilder(" [\n");
        for (NBT v : list) {
            sb.append(space).append(v.toStringBeautifier(lvl + 4)).append(",\n");
        }
        if (!list.isEmpty())
            sb.setLength(sb.length() - 2);
        else sb.setLength(sb.length() - 1);
        return sb.append("\n").append(" ".repeat(lvl)).append("]").toString();
    }

    @Override
    public NbtType getType() {
        return NbtType.LIST;
    }

    @NotNull
    @Override
    public Iterator<NBT> iterator() {
        return list.iterator();
    }

    public static class NBTCastException extends ClassCastException{
        public NBTCastException() {
        }

        public NBTCastException(String s) {
            super(s);
        }
    }
}
