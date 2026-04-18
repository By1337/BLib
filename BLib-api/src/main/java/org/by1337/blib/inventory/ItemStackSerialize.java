package org.by1337.blib.inventory;

import dev.by1337.core.BCore;
import dev.by1337.core.bridge.inventory.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * An interface for serializing and deserializing Bukkit ItemStacks to/from string representations.
 */
public interface ItemStackSerialize {
    ItemStackSerializer bridge = BCore.getItemStackSerializer();
    ItemStackSerialize INSTANCE = new ItemStackSerialize() {};

    @NotNull
    default String serialize(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        // String snbt = bridge.toSNbt(itemStack, null);
        return Base64.getEncoder().encodeToString(toByteArray(itemStack));
    }

    @NotNull
    default String serializeAndCompress(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        return serialize(itemStack);
    }

    @NotNull
    default ItemStack deserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return fromByteArray(Base64.getDecoder().decode(data));
        } catch (Exception e) {
            try {
                return bridge.fromSNbt(new String(
                        Base64.getDecoder().decode(data),
                        StandardCharsets.UTF_8
                ));
            } catch (Exception e1) {
                e1.addSuppressed(e);
                try {
                    //maybe gzip
                    byte[] gzip = decompress(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
                    return bridge.fromSNbt(new String(gzip, StandardCharsets.UTF_8));
                } catch (Exception e2) {
                    e2.addSuppressed(e1);
                    throw new IllegalArgumentException("bad data! " + data, e2);
                }
            }
        }

    }

    @NotNull
    default ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException {
        return deserialize(data);
    }

    default byte[] toByteArray(@NotNull ItemStack itemStack) {
        return BCore.getItemStackSerializer().serialize(itemStack, null);
    }

    default ItemStack fromByteArray(@NotNull byte[] bytes) {
        return BCore.getItemStackSerializer().deserialize(bytes, null);
    }

    default byte[] compress(byte[] array) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(array);
            gzipOutputStream.finish();
            return outputStream.toByteArray();
        }
    }


    default byte[] decompress(byte[] array) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
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

    default String compress(String raw) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(raw.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.finish();
            return new String(Base64.getEncoder().encode(outputStream.toByteArray()), StandardCharsets.UTF_8);
        }
    }

    default String decompress(String raw) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(raw.getBytes(StandardCharsets.UTF_8)));
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }
}
