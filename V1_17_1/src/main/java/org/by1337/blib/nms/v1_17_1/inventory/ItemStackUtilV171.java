package org.by1337.blib.nms.v1_17_1.inventory;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.*;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.by1337.blib.inventory.ItemStackUtil;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nms.v1_17_1.nbt.ParseCompoundTagV171;
import org.by1337.blib.util.invoke.LambdaMetafactoryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemStackUtilV171 implements ItemStackUtil {
    private static final Function<CraftItemStack, net.minecraft.world.item.ItemStack> HANDLE_GETTER;

    @Override
    public void setItemStackWithoutCopy(Inventory to, ItemStack who, int index) {
        net.minecraft.world.item.ItemStack nms;
        if (who instanceof CraftItemStack craftItemStack) {
            nms = Objects.requireNonNullElse(HANDLE_GETTER.apply(craftItemStack), net.minecraft.world.item.ItemStack.EMPTY);
        } else {
            nms = CraftItemStack.asNMSCopy(who);
        }
        if (to instanceof CraftResultInventory cri) {
            if (index < cri.getIngredientsInventory().getContainerSize()) {
                cri.getIngredientsInventory().setItem(index, nms);
            } else {
                cri.getResultInventory().setItem(index - cri.getIngredientsInventory().getContainerSize(), nms);
            }
        } else if (to instanceof CraftInventoryCrafting cic) {
            if (index < cic.getResultInventory().getContainerSize()) {
                cic.getResultInventory().setItem(index, nms);
            } else {
                cic.getMatrixInventory().setItem(index - cic.getResultInventory().getContainerSize(), nms);
            }
        } else if (to instanceof CraftInventoryPlayer cip) {
            cip.getInventory().setItem(index, nms);
            if (cip.getHolder() != null) {
                ServerPlayer player = ((CraftPlayer) cip.getHolder()).getHandle();
                if (index < net.minecraft.world.entity.player.Inventory.getSelectionSize()) {
                    index += 36;
                } else if (index > 39) {
                    index += 5;
                } else if (index > 35) {
                    index = 8 - (index - 36);
                }
                player.connection.send(new ClientboundContainerSetSlotPacket(player.inventoryMenu.containerId, player.inventoryMenu.incrementStateId(), index, nms));
            }
        } else if (to instanceof CraftInventory ci) {
            ci.getInventory().setItem(index, nms);
        } else {
            throw new UnsupportedOperationException("Unknown inventory impl " + to.getClass());
        }
    }

    @Override
    public ItemStack asBukkitMirror(@NotNull Object itemStack) {
        return CraftItemStack.asCraftMirror((net.minecraft.world.item.ItemStack) itemStack);
    }

    @Override
    public Object asNMSCopyItemStack(@NotNull ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public Object copyNMSItemStack(@NotNull Object itemStack) {
        return ((net.minecraft.world.item.ItemStack) itemStack).copy();
    }

    @Override
    public void setDisplayName(Component component, Object itemStack) {
        setDisplayName(PaperAdventure.asJsonString(component, Locale.ENGLISH), itemStack);
    }

    @Override
    public void setDisplayName(String json, Object itemStack) {
        var display = getDisplay((net.minecraft.world.item.ItemStack) itemStack);
        display.putString("Name", json);
    }

    @Override
    public void setLore(List<Component> jsons, Object itemStack) {
        setLoreJson(jsons.stream().map(c -> PaperAdventure.asJsonString(c, Locale.ENGLISH)).toList(), itemStack);
    }

    @Override
    public void setNBTToTag(String key, NBT nbt, Object itemStack) {
        var item = (net.minecraft.world.item.ItemStack) itemStack;
        var tag = item.getOrCreateTag();
        tag.put(key, ParseCompoundTagV171.convert(nbt));
    }

    @Override
    public void setLoreJson(List<String> jsons, Object itemStack) {
        var display = getDisplay((net.minecraft.world.item.ItemStack) itemStack);
        var list = new ListTag();
        for (String json : jsons) {
            list.add(StringTag.valueOf(json));
        }
        display.put("Lore", list);
    }

    @Override
    public List<Component> getLore(Object itemStack){
        return getJsonLore(itemStack).stream().map(s -> GsonComponentSerializer.gson().deserialize(s)).collect(Collectors.toList());
    }

    @Override
    public List<String> getJsonLore(Object itemStack){
        var display = getDisplay((net.minecraft.world.item.ItemStack) itemStack);
        var list = display.getList("Lore", CraftMagicNumbers.NBT.TAG_STRING);
        List<String> result = new ArrayList<>();
        list.forEach(n -> result.add(n.getAsString()));
        return result;
    }
    @Override
    public @Nullable Component getDisplayName(Object itemStack) {
        var name = getJsonDisplayName(itemStack);
        return name == null ? null : GsonComponentSerializer.gson().deserialize(name);
    }

    @Override
    public @Nullable String getJsonDisplayName(Object itemStack) {
        var display = getDisplay((net.minecraft.world.item.ItemStack) itemStack);
        if (display.contains("Name", CraftMagicNumbers.NBT.TAG_STRING)){
            return display.getString("Name");
        }
        return null;
    }

    @Override
    public void setCount(int count, Object itemStack) {
        ((net.minecraft.world.item.ItemStack) itemStack).setCount(count);
    }

    @Override
    public void setDamage(int count, Object itemStack) {
        ((net.minecraft.world.item.ItemStack) itemStack).setDamageValue(count);
    }

    @Override
    public boolean isJsonSupport() {
        return true;
    }

    private static CompoundTag getDisplay(net.minecraft.world.item.ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        CompoundTag display;
        if (tag.contains("display", CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            display = tag.getCompound("display");
        } else {
            display = new CompoundTag();
            tag.put("display", display);
        }
        return display;
    }

    static {
        try {
            Field field = CraftItemStack.class.getDeclaredField("handle");
            field.setAccessible(true);
            HANDLE_GETTER = LambdaMetafactoryUtil.getterOf(field);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
