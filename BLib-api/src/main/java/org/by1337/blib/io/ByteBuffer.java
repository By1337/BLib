package org.by1337.blib.io;

import org.by1337.blib.nbt.NbtByteBuffer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ByteBuffer implements NbtByteBuffer {
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
        long value = 0L;
        int shift = 0;

        byte b;
        do {
            b = this.readByte();
            value |= (long) (b & 127) << shift++ * 7;
            if (shift > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b & 128) == 128);

        return value;
    }

    public int readVarInt() {
        int value = 0;
        int shift = 0;

        byte b;
        do {
            b = this.readByte();
            value |= (b & 127) << shift++ * 7;
            if (shift > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b & 128) == 128);

        return value;
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

    public void writeBoolean(boolean b) {
        writeByte(b ? 1 : 0);
    }

    public boolean readBoolean() {
        return readByte() == 1;
    }

    public <T> void writeList(Collection<T> list, BiConsumer<ByteBuffer, T> consumer) {
        writeVarInt(list.size());
        for (T t : list) {
            consumer.accept(this, t);
        }
    }

    public <T> List<T> readList(Function<ByteBuffer, T> function) {
        int size = readVarInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(function.apply(this));
        }
        return list;
    }

    public void writeStringList(Collection<String> list) {
        writeList(list, (ByteBuffer::writeUtf));
    }

    public List<String> readStringList() {
        return readList(ByteBuffer::readUtf);
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
