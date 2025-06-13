package org.by1337.blib.nms.v1_20_2.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ItemStackSerializeV1_20_2 implements ItemStackSerialize {
    @Override
    public @NotNull String serialize(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        try {
            net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
            CompoundTag tag = new CompoundTag();
            item.save(tag);
            String serialize = tag.toString();
            return new String(Base64.getEncoder().encode(serialize.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize ItemStack", e);
        }
    }

    @Override
    public @NotNull ItemStack deserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asCraftMirror(
                    net.minecraft.world.item.ItemStack.of(TagParser.parseTag(new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)))
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }
    @Override
    public @NotNull String serializeAndCompress(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        try {
            net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
            CompoundTag tag = new CompoundTag();
            item.save(tag);
            return compress(tag.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize ItemStack", e);
        }
    }

    @Override
    public @NotNull ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of(
                    TagParser.parseTag(
                            decompress(data)
                    )
            ));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }

    @Override
    public String compress(String raw) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(raw.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.finish();
            return new String(Base64.getEncoder().encode(outputStream.toByteArray()), StandardCharsets.UTF_8);
        }
    }

    @Override
    public String decompress(String raw) throws IOException {
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