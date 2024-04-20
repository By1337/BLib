package org.by1337.blib.nbt;

import org.by1337.blib.nbt.impl.ByteArrNBT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedNBT extends ByteArrNBT {
    public CompressedNBT(byte[] value) {
        super(value);
    }

    public NBT decompress() {
        try {
            NbtByteBuffer nbtByteBuffer = new DefaultNbtByteBuffer(decompress(getValue()));

            NbtType nbtType = NbtType.values()[nbtByteBuffer.readByte()];

            return nbtType.read(nbtByteBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompressedNBT compress(NBT nbt) {
        NbtByteBuffer nbtByteBuffer = new DefaultNbtByteBuffer();
        nbtByteBuffer.writeByte(nbt.getType().ordinal());
        nbt.getType().write(nbtByteBuffer, nbt);
        try {
            return new CompressedNBT(compress(nbtByteBuffer.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] compress(byte[] raw) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(raw);
            gzipOutputStream.finish();
            return outputStream.toByteArray();
        }
    }

    private byte[] decompress(byte[] raw) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(raw);
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
}
