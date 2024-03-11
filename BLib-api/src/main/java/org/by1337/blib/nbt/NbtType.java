package org.by1337.blib.nbt;

import org.by1337.blib.nbt.impl.*;
import org.by1337.blib.io.ByteBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum NbtType {
    BYTE {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            byteBuf.writeByte(((ByteNBT) nbt).getValue());
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            return ByteNBT.valueOf(byteBuf.readByte());
        }
    },
    DOUBLE {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            byteBuf.writeDouble(((DoubleNBT) nbt).getValue());
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            return new DoubleNBT(byteBuf.readDouble());
        }
    },
    FLOAT {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            byteBuf.writeFloat(((FloatNBT) nbt).getValue());
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            return new FloatNBT(byteBuf.readFloat());
        }
    },
    INT {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            byteBuf.writeVarInt(((IntNBT) nbt).getValue());
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            return IntNBT.valueOf(byteBuf.readVarInt());
        }
    },
    LONG {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            byteBuf.writeVarLong(((LongNBT) nbt).getValue());
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            return LongNBT.valueOf(byteBuf.readVarLong());
        }
    },
    SHORT {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            byteBuf.writeShort(((ShortNBT) nbt).getValue());
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            return ShortNBT.valueOf(byteBuf.readShort());
        }
    },
    STRING {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            byteBuf.writeUtf(((StringNBT) nbt).getValue());
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            return new StringNBT(byteBuf.readUtf());
        }
    },

    BYTE_ARR {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            ByteArrNBT byteArrNBT = (ByteArrNBT) nbt;
            byteBuf.writeVarInt(byteArrNBT.getValue().length);
            for (Byte b : byteArrNBT.getValue()) {
                byteBuf.writeByte(b);
            }
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            int len = byteBuf.readVarInt();
            byte[] arr = new byte[len];
            for (int i = 0; i < len; i++) {
                arr[i] = byteBuf.readByte();
            }
            return new ByteArrNBT(arr);
        }
    },
    INT_ARR {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            IntArrNBT intArrNBT = (IntArrNBT) nbt;
            byteBuf.writeVarInt(intArrNBT.getValue().length);
            for (Integer b : intArrNBT.getValue()) {
                byteBuf.writeVarInt(b);
            }
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            int len = byteBuf.readVarInt();
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = byteBuf.readVarInt();
            }
            return new IntArrNBT(arr);
        }
    },
    LONG_ARR {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            LongArrNBT longArrNBT = (LongArrNBT) nbt;
            byteBuf.writeVarInt(longArrNBT.getValue().length);
            for (Long b : longArrNBT.getValue()) {
                byteBuf.writeVarLong(b);
            }
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            int len = byteBuf.readVarInt();
            long[] arr = new long[len];
            for (int i = 0; i < len; i++) {
                arr[i] = byteBuf.readVarLong();
            }
            return new LongArrNBT(arr);
        }
    },
    LIST {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            ListNBT listNBT = (ListNBT) nbt;
            byteBuf.writeVarInt(listNBT.getList().size());
            for (NBT nbt1 : listNBT.getList()) {
                byteBuf.writeByte(nbt1.getType().ordinal());
                nbt1.getType().write(byteBuf, nbt1);
            }
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            int len = byteBuf.readVarInt();
            List<NBT> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                byte id = byteBuf.readByte();
                NbtType type = NbtType.values()[id];
                list.add(type.read(byteBuf));
            }
            return new ListNBT(list);
        }
    },

    COMPOUND {
        @Override
        public void write(ByteBuffer byteBuf, NBT nbt) {
            CompoundTag compoundTag = (CompoundTag) nbt;
            byteBuf.writeVarInt(compoundTag.getTags().size());
            for (Map.Entry<String, NBT> nbtEntry : compoundTag.getTags().entrySet()) {
                byteBuf.writeUtf(nbtEntry.getKey());
                NBT nbt1 = nbtEntry.getValue();
                byteBuf.writeByte(nbt1.getType().ordinal());
                nbt1.getType().write(byteBuf, nbt1);
            }
        }

        @Override
        public NBT read(ByteBuffer byteBuf) {
            int len = byteBuf.readVarInt();
            CompoundTag compoundTag = new CompoundTag();
            for (int i = 0; i < len; i++) {
                String name = byteBuf.readUtf();
                byte id = byteBuf.readByte();
                NbtType type = NbtType.values()[id];
                compoundTag.putTag(name, type.read(byteBuf));
            }
            return compoundTag;
        }
    };

    public abstract void write(ByteBuffer byteBuf, NBT nbt);

    public abstract NBT read(ByteBuffer byteBuf);
}
