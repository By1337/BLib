package org.by1337.blib.io;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ByteBuffer {
    private final Buffer buffer;

    public ByteBuffer(byte[] arr) {
        this.buffer = new ReadBuffer(arr);
    }

    public ByteBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer() {
        this.buffer = new WriteBuffer();
    }

    public void writeByte(byte b) {
        buffer.write(b);
    }

    public void writeByte(int b) {
        writeByte((byte) b);
    }

    public void writeDouble(double b) {
        writeVarLong((Double.doubleToRawLongBits(b)));
    }

    public double readDouble() {
        return Double.longBitsToDouble(readVarLong());
    }

    public long readVarLong() {
        long var1 = 0L;
        int var3 = 0;

        byte var4;
        do {
            var4 = this.readByte();
            var1 |= (long) (var4 & 127) << var3++ * 7;
            if (var3 > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((var4 & 128) == 128);

        return var1;
    }

    public int readVarInt() {
        int var1 = 0;
        int var2 = 0;

        byte var3;
        do {
            var3 = this.readByte();
            var1 |= (var3 & 127) << var2++ * 7;
            if (var2 > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((var3 & 128) == 128);

        return var1;
    }

    public void writeVarLong(long l) {
        while ((l & -128L) != 0L) {
            this.writeByte((int) (l & 127L) | 128);
            l >>>= 7;
        }

        this.writeByte((int) l);
    }

    public void writeVarInt(int i) {
        while ((i & -128) != 0) {
            this.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.writeByte(i);
    }

    public void writeUUID(UUID uuid) {
        writeVarLong(uuid.getMostSignificantBits());
        writeVarLong(uuid.getLeastSignificantBits());
    }

    public UUID readUUID() {
        return new UUID(readVarLong(), readVarLong());
    }

    public void writeShort(int value) {
        buffer.write((byte) (value >>> 8));
        buffer.write((byte) (value));
    }

    public short readShort() {
        int ch1 = readByte();
        int ch2 = readByte();
        return (short) ((ch1 << 8) + (ch2));
    }

    public void writeUtf(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length);
        writeBytes(bytes);
    }

    public String readUtf() {
        int len = readVarInt();
        byte[] arr = new byte[len];
        readBytes(arr);
        return new String(arr, StandardCharsets.UTF_8);
    }

    public void readBytes(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = readByte();
        }
    }

    public void writeBytes(byte[] bytes) {
        for (byte b : bytes) {
            writeByte(b);
        }
    }

    public void writeFloat(float f) {
        writeVarInt(Float.floatToRawIntBits(f));
    }

    public float readFloat() {
        return Float.intBitsToFloat(readVarInt());
    }

    public byte readByte() {
        return buffer.next();
    }

    public byte[] toByteArray() {
        return buffer.toByteArray();
    }

    public int readableBytes() {
        return buffer.readableBytes();
    }

    public interface Buffer {
        byte next();

        int pos();

        int readableBytes();

        byte[] toByteArray();

        void write(byte b);
    }

    private static class WriteBuffer implements Buffer {

        private int pos = 0;
        private final List<Byte> bytes = new ArrayList<>();

        @Override
        public byte next() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int pos() {
            return pos;
        }

        @Override
        public int readableBytes() {
            return bytes.size();
        }

        @Override
        public byte[] toByteArray() {
            byte[] arr = new byte[bytes.size()];
            int x = 0;
            for (Byte b : bytes) {
                arr[x] = b;
                x++;
            }
            return arr;
        }

        @Override
        public void write(byte b) {
            bytes.add(b);
        }
    }

    private static class ReadBuffer implements Buffer {

        private final byte[] source;
        private int pos = 0;

        private ReadBuffer(byte[] source) {
            this.source = source;
        }

        @Override
        public byte next() {
            return source[pos++];
        }

        @Override
        public int pos() {
            return pos;
        }

        @Override
        public int readableBytes() {
            return source.length - pos;
        }

        @Override
        public byte[] toByteArray() {
            return source;
        }

        @Override
        public void write(byte b) {
            throw new UnsupportedOperationException("only read!");
        }
    }
}
