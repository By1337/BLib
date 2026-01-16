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
        String snbt = bridge.toSNbt(itemStack, null);
        return Base64.getEncoder().encodeToString(snbt.getBytes(StandardCharsets.UTF_8));
    }

    @NotNull
    default String serializeAndCompress(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        try {
            return Base64.getEncoder().encodeToString(compress(bridge.toSNbt(itemStack, null).getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @NotNull
    default ItemStack deserialize(@NotNull String data) throws IllegalArgumentException {
        return bridge.fromSNbt(new String(
                Base64.getDecoder().decode(data),
                StandardCharsets.UTF_8
        ));
    }

    @NotNull
    default ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return bridge.fromSNbt(new String(
                    Base64.getDecoder().decode(decompress(data.getBytes(StandardCharsets.UTF_8))),
                    StandardCharsets.UTF_8
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
