package org.by1337.blib.nms.v1_16_5.inventory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.jetbrains.annotations.NotNull;

public class ItemStackSerializeV1_16_5 implements ItemStackSerialize {
    @NotNull
    public String serialize(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        try {
            net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
            CompoundTag itemTag = new CompoundTag();
            i.save(itemTag);
            String serialize = itemTag.toString();
            return new String(Base64.getEncoder().encode(serialize.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } catch (Exception var5) {
            throw new IllegalArgumentException("Failed to serialize ItemStack", var5);
        }
    }

    @NotNull
    public ItemStack deserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asBukkitCopy(
                    net.minecraft.world.item.ItemStack.of_(
                            TagParser.parse(new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8))
                    )
            );
        } catch (Exception var3) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", var3);
        }
    }

    @NotNull
    public String serializeAndCompress(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        try {
            net.minecraft.world.item.ItemStack i = CraftItemStack.asNMSCopy(itemStack);
            CompoundTag itemTag = new CompoundTag();
            i.save(itemTag);
            return this.compress(itemTag.toString());
        } catch (Exception var4) {
            throw new IllegalArgumentException("Failed to serialize ItemStack", var4);
        }
    }

    @NotNull
    public ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of_(TagParser.parse(this.decompress(data))));
        } catch (Exception var3) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", var3);
        }
    }

    public String compress(String raw) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String var4;
        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);

            try {
                gzipOutputStream.write(raw.getBytes(StandardCharsets.UTF_8));
                gzipOutputStream.finish();
                var4 = new String(Base64.getEncoder().encode(outputStream.toByteArray()), StandardCharsets.UTF_8);
            } catch (Throwable var8) {
                try {
                    gzipOutputStream.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }

                throw var8;
            }

            gzipOutputStream.close();
        } catch (Throwable var9) {
            try {
                outputStream.close();
            } catch (Throwable var6) {
                var9.addSuppressed(var6);
            }

            throw var9;
        }

        outputStream.close();
        return var4;
    }

    public String decompress(String raw) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(raw.getBytes(StandardCharsets.UTF_8)));

        String var7;
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                try {
                    byte[] buffer = new byte[1024];

                    int bytesRead;
                    while((bytesRead = gzipInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    var7 = outputStream.toString(StandardCharsets.UTF_8);
                } catch (Throwable var11) {
                    try {
                        outputStream.close();
                    } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                    }

                    throw var11;
                }

                outputStream.close();
            } catch (Throwable var12) {
                try {
                    gzipInputStream.close();
                } catch (Throwable var9) {
                    var12.addSuppressed(var9);
                }

                throw var12;
            }

            gzipInputStream.close();
        } catch (Throwable var13) {
            try {
                inputStream.close();
            } catch (Throwable var8) {
                var13.addSuppressed(var8);
            }

            throw var13;
        }

        inputStream.close();
        return var7;
    }
}
