package org.by1337.blib.block.data;

import org.bukkit.Location;
import org.bukkit.plugin.java.PluginClassLoader;
import org.by1337.blib.block.CustomBlock;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.util.SpacedNameKey;
import org.by1337.blib.world.BLocation;

public class CustomBlockData {
    private final CompoundTag data;

    public CustomBlockData(CompoundTag data) {
        this.data = data;
    }

    public int getBlockX() {
        return data.getAsCompoundTag("info").getAsInt("x");
    }

    public int getBlockY() {
        return data.getAsCompoundTag("info").getAsInt("y");
    }

    public int getBlockZ() {
        return data.getAsCompoundTag("info").getAsInt("z");
    }

    public String getWorld() {
        return data.getAsCompoundTag("info").getAsString("world");
    }

    public String getPlugin() {
        return data.getAsCompoundTag("info").getAsString("plugin");
    }

    public SpacedNameKey getId() {
        return new SpacedNameKey(data.getAsCompoundTag("info").getAsString("id"));
    }

    public CompoundTag getData() {
        return data;
    }

    public static CustomBlockData create(CustomBlock customBlock, Location location) {
        return create(customBlock, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static CustomBlockData create(CustomBlock customBlock, String world, int x, int y, int z) {
        CompoundTag data = new CompoundTag();
        CompoundTag info = new CompoundTag();
        info.putString("world", world);
        info.putInt("x", x);
        info.putInt("y", y);
        info.putInt("z", z);
        info.putString("plugin", getPlugin(customBlock.getClass()));
        info.putString("id", customBlock.getId().toString());
        data.putTag("info", info);
        return new CustomBlockData(data);
    }


    private static String getPlugin(Class<? extends CustomBlock> clazz) {
        if (clazz.getClassLoader() instanceof PluginClassLoader pluginClassLoader) {
            return pluginClassLoader.getPlugin().getName();
        }
        return "Unknown";
    }
}
