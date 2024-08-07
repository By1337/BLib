package org.by1337.blib.nbt.impl;

import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.NbtType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ListNBT extends NBT implements Collection<NBT> {

    private final List<NBT> list;
    private NbtType innerType;
    private boolean allowMultipleType = false;

    public ListNBT() {
        this(new ArrayList<>());
    }

    private ListNBT(List<NBT> list, NbtType innerType, boolean allowMultipleType) {
        this.list = list;
        this.innerType = innerType;
        this.allowMultipleType = allowMultipleType;
    }

    public ListNBT(List<NBT> list, boolean allowMultipleType) {
        this.allowMultipleType = true;
        this.list = new ArrayList<>(list.size());
        addAll(list);
    }

    public ListNBT(List<NBT> list) {
        this.list = new ArrayList<>(list.size());
        addAll(list);
    }

    public boolean add(NBT nbt) {
        if (innerType != null) {
            if (!allowMultipleType && innerType != nbt.getType()) {
                throw new NBTCastException("type " + innerType + " to " + nbt.getType());
            }
        } else {
            innerType = nbt.getType();
        }
        list.add(nbt);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends NBT> c) {
        for (NBT nbt : c) {
            add(nbt);
        }
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
        innerType = null;
    }

    @Nullable
    public NbtType getInnerType() {
        return innerType;
    }

    @Override
    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Object getAsObject() {
        return list;
    }

    @Override
    public ListNBT copy() {
        final List<NBT> list1 = new ArrayList<>(list.size());
        for (NBT nbt : list) {
            list1.add(nbt.copy());
        }
        return new ListNBT(list1, innerType, allowMultipleType);
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

    @Override
    public NbtType getType() {
        return NbtType.LIST;
    }

    @NotNull
    @Override
    public Iterator<NBT> iterator() {
        return (Iterator<NBT>) list.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return list.toArray(a);
    }

    public boolean isAllowMultipleType() {
        return allowMultipleType;
    }

    public static class NBTCastException extends ClassCastException {
        public NBTCastException() {
        }

        public NBTCastException(String s) {
            super(s);
        }
    }
}
