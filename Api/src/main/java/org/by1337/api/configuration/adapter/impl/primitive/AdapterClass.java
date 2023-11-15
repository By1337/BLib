package org.by1337.api.configuration.adapter.impl.primitive;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.by1337.api.util.NameKey;
import org.by1337.api.configuration.adapter.PrimitiveAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A primitive adapter for serializing and deserializing Java Class objects, with support for aliases.
 */
public class AdapterClass implements PrimitiveAdapter<Class<?>> {
    private static final Map<String, Class<?>> aliases;

    /**
     * Serialize a Java Class object to its canonical name or alias.
     *
     * @param obj The Class object to be serialized.
     * @return The canonical name or alias of the Class.
     */
    @Override
    public Object serialize(Class<?> obj) {
        return getAliasOrClassName(obj);
    }

    /**
     * Deserialize a Java Class object from a source object, using aliases if available.
     *
     * @param src The source object containing the Class name or alias.
     * @return The deserialized Class object.
     * @throws RuntimeException if the Class is not found or an alias is not registered.
     */
    @Override
    public Class<?> deserialize(Object src) {
        try {
            String s = String.valueOf(src);
            if (aliases.containsKey(s)) {
                return aliases.get(s);
            }
            return Class.forName(s);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Retrieve the alias or canonical name for a given Class.
     *
     * @param clazz The Class for which to find the alias or canonical name.
     * @return The alias or canonical name for the Class.
     */
    public static String getAliasOrClassName(Class<?> clazz) {
        for (Map.Entry<String, Class<?>> entry : aliases.entrySet()) {
            if (entry.getValue().equals(clazz)) {
                return entry.getKey();
            }
        }
        return clazz.getName();
    }


    /**
     * Register an alias for a Class.
     *
     * @param alias The alias for the Class.
     * @param clazz The Class to be associated with the alias.
     * @throws IllegalStateException if the alias is already registered.
     */
    public static void registerAlias(@NotNull String alias, @NotNull Class<?> clazz) {
        if (aliases.containsKey(alias)) {
            throw new IllegalStateException("Alias '" + alias + "' is already registered.");
        }
        aliases.put(alias, clazz);
    }

    /**
     * Unregister an alias for a Class.
     *
     * @param alias The alias to be unregistered.
     */
    public static void unregisterAlias(@NotNull String alias) {
        aliases.remove(alias);
    }

    static {
        aliases = new HashMap<>();

        registerAlias("color", Color.class);
        registerAlias("int", Integer.class);
        registerAlias("string", String.class);
        registerAlias("material", Material.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("long", Long.class);
        registerAlias("particle", Particle.class);
        registerAlias("name-key", NameKey.class);
        registerAlias("short", Short.class);
        registerAlias("byte", Byte.class);
    }
}
