package org.by1337.blib.nbt;

import org.by1337.blib.nbt.impl.*;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MojangNbtReader {

    public static void write(NBT nbt, DataOutput output) throws IOException {
        writeUnnamedTag(nbt, output);
    }

    public static void writeCompressed(NBT nbt, File file) throws IOException {
        try (OutputStream var2 = new FileOutputStream(file)) {
            writeCompressed(nbt, var2);
        }
    }

    public static void writeCompressed(NBT nbt, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream)))) {
            writeUnnamedTag(nbt, dataOutputStream);
        }
    }

    private static void writeUnnamedTag(NBT nbt, DataOutput output) throws IOException {
        MojangNBTSerializer type = MojangNBTSerializer.BLIB_TYPE_TO_MOJANG.get(nbt.getType());
        if (type == null) {
            output.writeByte(0);
            return;
        }
        output.writeByte(type.mojangId);
        output.writeUTF("");
        type.write(output, nbt);
    }

    public static CompoundTag readCompressed(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return readCompressed(inputStream);
        }
    }

    public static CompoundTag readCompressed(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(inputStream)))) {
            return read(dataInputStream);
        }
    }

    public static CompoundTag read(DataInput input) throws IOException {
        NBT nbt = readUnnamedTag(input);
        if (nbt instanceof CompoundTag compoundTag) {
            return compoundTag;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    @Nullable
    public static NBT readUnnamedTag(DataInput input) throws IOException {
        int type = input.readByte();
        if (type == 0) return null;
        input.readUTF();
        return MojangNBTSerializer.LOOKUP_BY_ID.get(type).read(input);
    }


    public enum MojangNBTSerializer {
        TAG_BYTE(1) {
            @Override
            public NBT read(DataInput input) throws IOException {
                return ByteNBT.valueOf(input.readByte());
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                output.write(((ByteNBT) nbt).getValue());
            }
        }, TAG_SHORT(2) {
            @Override
            public NBT read(DataInput input) throws IOException {
                return ShortNBT.valueOf(input.readShort());
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                output.writeShort(((ShortNBT) nbt).getValue());
            }
        }, TAG_INT(3) {
            @Override
            public NBT read(DataInput input) throws IOException {
                return IntNBT.valueOf(input.readInt());
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                output.writeInt(((IntNBT) nbt).getValue());
            }
        }, TAG_LONG(4) {
            @Override
            public NBT read(DataInput input) throws IOException {
                return LongNBT.valueOf(input.readLong());
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                output.writeLong(((LongNBT) nbt).getValue());
            }
        }, TAG_FLOAT(5) {
            @Override
            public NBT read(DataInput input) throws IOException {
                return new FloatNBT(input.readFloat());
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                output.writeFloat(((FloatNBT) nbt).getValue());
            }
        }, TAG_DOUBLE(6) {
            @Override
            public NBT read(DataInput input) throws IOException {
                return new DoubleNBT(input.readDouble());
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                output.writeDouble(((DoubleNBT) nbt).getValue());
            }
        }, TAG_BYTE_ARRAY(7) {
            @Override
            public NBT read(DataInput input) throws IOException {
                byte[] arr = new byte[input.readInt()];
                input.readFully(arr);
                return new ByteArrNBT(arr);
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                ByteArrNBT arr = (ByteArrNBT) nbt;
                output.writeInt(arr.getValue().length);
                output.write(arr.getValue());
            }
        }, TAG_STRING(8) {
            @Override
            public NBT read(DataInput input) throws IOException {
                return new StringNBT(input.readUTF());
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                output.writeUTF(((StringNBT) nbt).getValue());
            }
        }, TAG_LIST(9) {
            @Override
            public NBT read(DataInput input) throws IOException {
                int type = input.readByte();
                var list = new ListNBT();
                if (type == 0) {
                    input.readInt();
                    return list;
                }
                int size = input.readInt();
                for (int i = size; i > 0; i--) {
                    list.add(LOOKUP_BY_ID.get(type).read(input));
                }
                return list;
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                var list = ((ListNBT) nbt).getList();
                if (list.isEmpty()) {
                    output.writeByte(0);
                } else {
                    var n = list.get(0);
                    output.writeByte(BLIB_TYPE_TO_MOJANG.get(n.getType()).mojangId);
                }
                output.writeInt(list.size());
                for (NBT nbt1 : list) {
                    BLIB_TYPE_TO_MOJANG.get(nbt1.getType()).write(output, nbt1);
                }
            }
        }, TAG_COMPOUND(10) {
            @Override
            public NBT read(DataInput input) throws IOException {
                CompoundTag compoundTag = new CompoundTag();
                int id;
                while ((id = input.readByte()) != 0) {
                    String name = input.readUTF();
                    NBT nbt = LOOKUP_BY_ID.get(id).read(input);
                    compoundTag.putTag(name, nbt);
                }
                return compoundTag;
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                CompoundTag compoundTag = (CompoundTag) nbt;
                for (Map.Entry<String, NBT> entry : compoundTag.getTags().entrySet()) {
                    NBT nbt1 = entry.getValue();
                    String name = entry.getKey();
                    int nbtId = BLIB_TYPE_TO_MOJANG.get(nbt1.getType()).mojangId;
                    output.writeByte(nbtId);
                    if (nbtId != 0) {
                        output.writeUTF(name);
                        LOOKUP_BY_ID.get(nbtId).write(output, nbt1);
                    }
                }
                output.writeByte(0);
            }
        }, TAG_INT_ARRAY(11) {
            @Override
            public NBT read(DataInput input) throws IOException {
                int[] arr = new int[input.readInt()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = input.readInt();
                }
                return new IntArrNBT(arr);
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                IntArrNBT arr = (IntArrNBT) nbt;
                output.writeInt(arr.getValue().length);
                for (int i : arr.getValue()) {
                    output.writeInt(i);
                }
            }
        }, TAG_LONG_ARRAY(12) {
            @Override
            public NBT read(DataInput input) throws IOException {
                long[] arr = new long[input.readInt()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = input.readLong();
                }
                return new LongArrNBT(arr);
            }

            @Override
            public void write(DataOutput output, NBT nbt) throws IOException {
                LongArrNBT arr = (LongArrNBT) nbt;
                output.writeInt(arr.getValue().length);
                for (long i : arr.getValue()) {
                    output.writeLong(i);
                }
            }
        };

        private final int mojangId;
        private static final Map<MojangNBTSerializer, NbtType> MOJANG_TYPE_TO_BLIB_TYPE;
        private static final Map<NbtType, MojangNBTSerializer> BLIB_TYPE_TO_MOJANG;
        private static final Map<Integer, MojangNBTSerializer> LOOKUP_BY_ID;

        MojangNBTSerializer(int mojangId) {
            this.mojangId = mojangId;
        }

        public int getMojangId() {
            return mojangId;
        }

        public static MojangNBTSerializer getMojangTypeByNbtType(NbtType nbtType) {
            return BLIB_TYPE_TO_MOJANG.get(nbtType);
        }

        public abstract NBT read(DataInput input) throws IOException;

        public abstract void write(DataOutput output, NBT nbt) throws IOException;

        static {
            Map<MojangNBTSerializer, NbtType> mojang_type_to_blib_type_tmp = new HashMap<>();
            Map<NbtType, MojangNBTSerializer> blib_type_to_mojang_tmp = new HashMap<>();

            mojang_type_to_blib_type_tmp.put(TAG_BYTE, NbtType.BYTE);
            blib_type_to_mojang_tmp.put(NbtType.BYTE, TAG_BYTE);

            mojang_type_to_blib_type_tmp.put(TAG_DOUBLE, NbtType.DOUBLE);
            blib_type_to_mojang_tmp.put(NbtType.DOUBLE, TAG_DOUBLE);

            mojang_type_to_blib_type_tmp.put(TAG_FLOAT, NbtType.FLOAT);
            blib_type_to_mojang_tmp.put(NbtType.FLOAT, TAG_FLOAT);

            mojang_type_to_blib_type_tmp.put(TAG_INT, NbtType.INT);
            blib_type_to_mojang_tmp.put(NbtType.INT, TAG_INT);

            mojang_type_to_blib_type_tmp.put(TAG_LONG, NbtType.LONG);
            blib_type_to_mojang_tmp.put(NbtType.LONG, TAG_LONG);

            mojang_type_to_blib_type_tmp.put(TAG_SHORT, NbtType.SHORT);
            blib_type_to_mojang_tmp.put(NbtType.SHORT, TAG_SHORT);

            mojang_type_to_blib_type_tmp.put(TAG_STRING, NbtType.STRING);
            blib_type_to_mojang_tmp.put(NbtType.STRING, TAG_STRING);

            mojang_type_to_blib_type_tmp.put(TAG_BYTE_ARRAY, NbtType.BYTE_ARR);
            blib_type_to_mojang_tmp.put(NbtType.BYTE_ARR, TAG_BYTE_ARRAY);

            mojang_type_to_blib_type_tmp.put(TAG_INT_ARRAY, NbtType.INT_ARR);
            blib_type_to_mojang_tmp.put(NbtType.INT_ARR, TAG_INT_ARRAY);

            mojang_type_to_blib_type_tmp.put(TAG_LONG_ARRAY, NbtType.LONG_ARR);
            blib_type_to_mojang_tmp.put(NbtType.LONG_ARR, TAG_LONG_ARRAY);

            mojang_type_to_blib_type_tmp.put(TAG_LIST, NbtType.LIST);
            blib_type_to_mojang_tmp.put(NbtType.LIST, TAG_LIST);

            mojang_type_to_blib_type_tmp.put(TAG_COMPOUND, NbtType.COMPOUND);
            blib_type_to_mojang_tmp.put(NbtType.COMPOUND, TAG_COMPOUND);

            Map<Integer, MojangNBTSerializer> lookup_by_id_tmp = new HashMap<>();

            for (MojangNBTSerializer value : values()) {
                lookup_by_id_tmp.put(value.mojangId, value);
            }
            MOJANG_TYPE_TO_BLIB_TYPE = Collections.unmodifiableMap(mojang_type_to_blib_type_tmp);
            BLIB_TYPE_TO_MOJANG = Collections.unmodifiableMap(blib_type_to_mojang_tmp);
            LOOKUP_BY_ID = Collections.unmodifiableMap(lookup_by_id_tmp);

        }
    }

}
