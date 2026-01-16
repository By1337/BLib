package org.by1337.blib.nbt;

import dev.by1337.core.BCore;
import dev.by1337.core.bridge.nbt.NbtBridge;
import dev.by1337.core.util.nbt.BinaryNbt;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.by1337.blib.nbt.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ParseCompoundTag {
    ParseCompoundTag INSTANCE = new ParseCompoundTag() {
        private NbtBridge bridge = BCore.getNbtBridge();

        @Override
        public @NotNull CompoundTag copy(@NotNull ItemStack itemStack) {
            return (CompoundTag)toBLib(bridge.of(itemStack, null));
        }

        @Override
        public @NotNull ItemStack create(@NotNull CompoundTag compoundTag) {
            return bridge.create((BinaryNbt.CompoundTag)ofBLib(compoundTag), null);
        }

        @Override
        public CompletableFuture<@Nullable CompoundTag> readOfflinePlayerData(@NotNull UUID player) {
            throw new UnsupportedOperationException("readOfflinePlayerData");
        }

        @Override
        public @NotNull CompoundTag pdcToCompoundTag(@NotNull PersistentDataContainer persistentDataContainer) {
            return (CompoundTag)toBLib(bridge.of(persistentDataContainer));
        }

        @Override
        public Object toNMS(@NotNull NBT nbt) {
            return bridge.toNMS(ofBLib(nbt));
        }

        @Override
        public NBT fromNMS(@NotNull Object nmsObj) {
            return toBLib(bridge.ofNMS(nmsObj));
        }

        public static final byte TAG_END = 0;
        public static final byte TAG_BYTE = 1;
        public static final byte TAG_SHORT = 2;
        public static final byte TAG_INT = 3;
        public static final byte TAG_LONG = 4;
        public static final byte TAG_FLOAT = 5;
        public static final byte TAG_DOUBLE = 6;
        public static final byte TAG_BYTE_ARRAY = 7;
        public static final byte TAG_STRING = 8;
        public static final byte TAG_INT_ARRAY = 11;
        public static final byte TAG_LONG_ARRAY = 12;
        public static final byte TAG_LIST = 9;
        public static final byte TAG_COMPOUND = 10;

        public NBT toBLib(BinaryNbt.NbtTag tag) {
            return switch (tag.getId()) {
                case TAG_END -> null;
                case TAG_BYTE -> ByteNBT.valueOf(((BinaryNbt.ByteTag) tag).value());
                case TAG_SHORT -> ShortNBT.valueOf(((BinaryNbt.ShortTag) tag).value());
                case TAG_INT -> IntNBT.valueOf(((BinaryNbt.IntTag) tag).value());
                case TAG_LONG -> LongNBT.valueOf(((BinaryNbt.LongTag) tag).value());
                case TAG_FLOAT -> new FloatNBT(((BinaryNbt.FloatTag) tag).value());
                case TAG_DOUBLE -> new DoubleNBT(((BinaryNbt.DoubleTag) tag).value());
                case TAG_STRING -> new StringNBT(((BinaryNbt.StringTag) tag).value());
                case TAG_BYTE_ARRAY -> new ByteArrNBT(((BinaryNbt.ByteArrayTag) tag).value());
                case TAG_INT_ARRAY -> new IntArrNBT(((BinaryNbt.IntArrayTag) tag).value());
                case TAG_LONG_ARRAY -> new LongArrNBT(((BinaryNbt.LongArrayTag) tag).value());
                case TAG_LIST -> {
                    ListNBT listTag = new ListNBT();
                    for (BinaryNbt.NbtTag nbtTag : ((BinaryNbt.ListTag) tag).tags()) {
                        listTag.add(toBLib(nbtTag));
                    }
                    yield listTag;
                }
                case TAG_COMPOUND -> {
                    CompoundTag compoundTag = new CompoundTag();
                    for (var entry : ((BinaryNbt.CompoundTag) tag).tags().entrySet()) {
                        compoundTag.putTag(entry.getKey(), toBLib(entry.getValue()));
                    }
                    yield compoundTag;
                }
                default -> throw new IllegalArgumentException("Unexpected type: " + tag);
            };
        }


        public BinaryNbt.NbtTag ofBLib(NBT nms) {
            return switch (nms.getType()) {
                case BYTE -> new BinaryNbt.ByteTag(((ByteNBT) nms).getValue());
                case SHORT -> new BinaryNbt.ShortTag(((ShortNBT) nms).getValue());
                case INT -> new BinaryNbt.IntTag(((IntNBT) nms).getValue());
                case LONG -> new BinaryNbt.LongTag(((LongNBT) nms).getValue());
                case FLOAT -> new BinaryNbt.FloatTag(((FloatNBT) nms).getValue());
                case DOUBLE -> new BinaryNbt.DoubleTag(((DoubleNBT) nms).getValue());
                case STRING -> new BinaryNbt.StringTag(((StringNBT) nms).getValue());
                case BYTE_ARR -> {
                    byte[] v = ((ByteArrNBT) nms).getValue();
                    yield new BinaryNbt.ByteArrayTag(v);
                }
                case INT_ARR -> {
                    int[] v = ((IntArrNBT) nms).getValue();
                    yield new BinaryNbt.IntArrayTag(v);
                }
                case LONG_ARR -> {
                    long[] v = ((LongArrNBT) nms).getValue();
                    yield new BinaryNbt.LongArrayTag(v);
                }
                case LIST -> {
                    ListNBT list = (ListNBT) nms;
                    BinaryNbt.ListTag out = new BinaryNbt.ListTag();
                    for (NBT element : list) {
                        out.add(ofBLib(element));
                    }
                    yield out;
                }
                case COMPOUND -> {
                    CompoundTag compound = (CompoundTag) nms;
                    BinaryNbt.CompoundTag out = new BinaryNbt.CompoundTag();
                    for (String key : compound.getTags().keySet()) {
                        out.put(key, ofBLib(compound.get(key)));
                    }
                    yield out;
                }
                default -> throw new IllegalArgumentException("Unexpected NMS tag type: " + nms.getClass());
            };
        }
    };

    @NotNull CompoundTag copy(@NotNull ItemStack itemStack);

    @NotNull ItemStack create(@NotNull CompoundTag compoundTag);

    CompletableFuture<@Nullable CompoundTag> readOfflinePlayerData(@NotNull UUID player);

    @NotNull CompoundTag pdcToCompoundTag(@NotNull PersistentDataContainer persistentDataContainer);

    Object toNMS(@NotNull NBT nbt);

    NBT fromNMS(@NotNull Object nmsObj);
}
