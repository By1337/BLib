package org.by1337.blib.nms.V1_21_6.inventory;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.nms.V1_21_5.inventory.ItemStackSerializeV1215;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NMSAccessor(forClazz = ItemStackSerialize.class, from = Version.V1_21_6, to = Version.V1_21_8)
public class ItemStackSerializeV1216 implements ItemStackSerialize {
    private final ItemStackSerializeV1215 nms = new ItemStackSerializeV1215();

    @Override
    public @NotNull ItemStack deserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return parse(data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }

    private ItemStack parse(String data) throws CommandSyntaxException {
        var nms = net.minecraft.world.item.ItemStack.CODEC
                .parse(
                        MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                        TagParser.parseCompoundFully(new String(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8))
                ).getOrThrow();
        return CraftItemStack.asCraftMirror(nms);
    }

    @Override
    public @NotNull ItemStack decompressAndDeserialize(@NotNull String data) throws IllegalArgumentException {
        try {
            return parse(decompress(data));
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