package org.by1337.blib.nms.v1_16_5.inventory;


import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
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

public class ItemStackSerializeV1_16_5 implements ItemStackSerialize {
    @Override
    public @NotNull String serialize(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        try {
            net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound itemTag = new NBTTagCompound();
            i.save(itemTag);
            String serialize = itemTag.toString();
            return new String(Base64.getEncoder().encode(serialize.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize ItemStack", e);
        }
    }

    @Override
    public @NotNull ItemStack deserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asBukkitCopy(
                    net.minecraft.server.v1_16_R3.ItemStack.a(MojangsonParser.parse(new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)))
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }

    @Override
    public @NotNull String serializeAndCompress(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        try {
            net.minecraft.server.v1_16_R3.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound itemTag = new NBTTagCompound();
            i.save(itemTag);
            return compress(itemTag.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize ItemStack", e);
        }
    }

    @Override
    public @NotNull ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R3.ItemStack.a(
                    MojangsonParser.parse(
                            decompress(data)
                    )
            ));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }

    private String compress(String raw) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(raw.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.finish();
            return new String(Base64.getEncoder().encode(outputStream.toByteArray()), StandardCharsets.UTF_8);
        }
    }

    private String decompress(String raw) throws IOException {
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
