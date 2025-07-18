package org.by1337.blib.nms.V1_21_5.inventory;

import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.V1_20_6.inventory.ItemStackSerializeV1206;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NMSAccessor(forClazz = ItemStackSerialize.class, forVersions = {Version.V1_21_5})
public class ItemStackSerializeV1215 implements ItemStackSerialize {
    private final ItemStackSerializeV1206 nms = new ItemStackSerializeV1206();

    @Override
    public @NotNull ItemStack deserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asCraftMirror(
                    net.minecraft.world.item.ItemStack.parse(
                            MinecraftServer.getServer().registryAccess(),
                            TagParser.parseCompoundFully(new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8))
                    ).get()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }

    @Override
    public @NotNull ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.parse(
                            MinecraftServer.getServer().registryAccess(),
                            TagParser.parseCompoundFully(
                                    decompress(data)
                            )
                    ).get()
            )
                    ;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }

    @Override
    public @NotNull String serializeAndCompress(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        return nms.serializeAndCompress(itemStack);
    }

    @Override
    public String compress(String raw) throws IOException {
        return nms.compress(raw);
    }

    @Override
    public String decompress(String raw) throws IOException {
        return nms.decompress(raw);
    }

    @Override
    public @NotNull String serialize(@NotNull ItemStack itemStack) throws IllegalArgumentException {
        return nms.serialize(itemStack);
    }
}