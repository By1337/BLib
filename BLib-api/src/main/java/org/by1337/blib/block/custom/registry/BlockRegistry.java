package org.by1337.blib.block.custom.registry;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.by1337.blib.BLib;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.custom.impl.MissingCustomBlock;
import org.by1337.blib.util.SpacedNameKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistry {
    private static BlockRegistry instance = new BlockRegistry();
    private final Map<SpacedNameKey, CustomBlock> LOOKUP = new HashMap<>();
    private final Map<String, Set<SpacedNameKey>> LOOKUP_BY_PLUGIN = new HashMap<>();

    private BlockRegistry() {
    }

    public void register(SpacedNameKey spacedNameKey, CustomBlock type) {
        Plugin plugin = getPlugin(type.getClass());
        if (LOOKUP.containsKey(spacedNameKey)) {
            plugin.getLogger().severe("Failed to register custom block!");
            throw new IllegalStateException("Failed to register custom block! id: " + spacedNameKey + " block: " + type.getClass().getCanonicalName());
        }
        LOOKUP.put(spacedNameKey, type);
        LOOKUP_BY_PLUGIN.computeIfAbsent(plugin.getName(), k -> new HashSet<>()).add(spacedNameKey);
    }

    public CustomBlock getCustomBlockOr(SpacedNameKey id, Function<SpacedNameKey, CustomBlock> block){
        var type = LOOKUP.get(id);
        if (type == null){
            BLib.getApi().getMessage().error("Missing custom block! Id: %s", id);
            return block.apply(id);
        }
        return type;
    }

    @NotNull
    public CustomBlock getCustomBlock(SpacedNameKey id){
        return getCustomBlockOr(id, MissingCustomBlock::new);
    }

    public Optional<CustomBlock> getCustomBlockOptional(SpacedNameKey id){
        return Optional.ofNullable(getCustomBlockOr(id, k -> null));
    }

    public void unregister(CustomBlock type) {
        Plugin plugin = getPlugin(type.getClass());
        LOOKUP.remove(type.getId());
        var set = LOOKUP_BY_PLUGIN.get(plugin.getName());
        if (set != null) {
            set.remove(type.getId());
            if (set.isEmpty()) {
                LOOKUP_BY_PLUGIN.remove(plugin.getName());
            }
        }
    }

    public void unregister(SpacedNameKey id) {
        unregister(LOOKUP.get(id));
    }

    public void unregisterAll(Plugin plugin) {
        var set = LOOKUP_BY_PLUGIN.remove(plugin.getName());
        if (set != null) {
            for (SpacedNameKey spacedNameKey : set) {
                LOOKUP.remove(spacedNameKey);
            }
        }
    }

    public Collection<CustomBlock> getAll(){
        return Collections.unmodifiableCollection(LOOKUP.values());
    }

    public static Plugin getPlugin(Class<? extends CustomBlock> type) {
        if (type.getClassLoader() instanceof PluginClassLoader pluginClassLoader) {
            return pluginClassLoader.getPlugin();
        }
        throw new IllegalStateException("Failed to get plugin!");
    }

    public static BlockRegistry get() {
        return instance;
    }
}
