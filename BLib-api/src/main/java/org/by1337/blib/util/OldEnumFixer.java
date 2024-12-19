package org.by1337.blib.util;

import org.by1337.blib.text.MessageFormatter;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

@ApiStatus.Internal
public class OldEnumFixer {
    private static final Class<?> OLD_ENUM;
    private static final Map<Class<?>, EnumData<?>> ENUMS = new HashMap<>();


    public static boolean isOldEnum(Class<?> cl) {
        return OLD_ENUM != null && OLD_ENUM.isAssignableFrom(cl);
    }

    public static <T> T[] values(Class<T> cl) {
        return getData(cl).values;
    }

    public static <T> String name(T value) {
        return reqNonNull(getData(value.getClass()).toName.get(value), "Unknown enum constant {}.{}", value.getClass().getCanonicalName(), value);
    }

    public static <T> T valueOf(String name, Class<T> cl) {
        return reqNonNull(getData(cl).lookupByName.get(name), "No enum constant {}.{}", cl.getCanonicalName(), name);
    }

    public static <T> Map<String, T> getNameToValueMap(Class<T> cl) {
        return getData(cl).lookupByName;
    }
    public static <T> Map<T, String> getValueToNameMap(Class<T> cl) {
        return getData(cl).toName;
    }

    private static <T> T reqNonNull(T val, String message, Object... args) {
        if (val == null) throw new IllegalArgumentException(MessageFormatter.apply(message, args));
        return val;
    }

    @SuppressWarnings("unchecked")
    private static <T> EnumData<T> getData(Class<T> cl) {
        EnumData<T> data = (EnumData<T>) ENUMS.computeIfAbsent(cl, ignore -> new EnumData<>(cl));
        if (data.loader.equals(cl.getClassLoader())) return data;
        var newData = new EnumData<>(cl);
        ENUMS.put(cl, newData);
        return newData;
    }

    static {
        Class<?> cl;
        try {
            cl = Class.forName("org.bukkit.util.OldEnum");
        } catch (ClassNotFoundException ignore) {
            cl = null;
        }
        OLD_ENUM = cl;
    }

    private static class EnumData<T> {
        private final ClassLoader loader;
        private final Class<T> clazz;
        private final T[] values;
        private final Map<String, T> lookupByName;
        private final Map<T, String> toName;

        @SuppressWarnings("unchecked")
        public EnumData(Class<T> clazz) {
            this.clazz = clazz;
            loader = clazz.getClassLoader();
            Map<String, T> lookupByName0 = new HashMap<>();
            Map<T, String> toName0 = new IdentityHashMap<>();
            try {
                Method method = null;
                try {
                    method = clazz.getMethod("values");
                } catch (NoSuchMethodException ignore) {
                    for (Class<?> anInterface : clazz.getInterfaces()) {
                        try {
                            method = anInterface.getMethod("values");
                            break;
                        } catch (NoSuchMethodException ignore2) {
                        }
                    }
                }
                if (method == null) {
                    throw new NoSuchMethodException(clazz.getCanonicalName() + ".values()");
                }
                method.setAccessible(true);
                values = (T[]) method.invoke(null);
                for (T value : values) {
                    lookupByName0.put(value.toString(), value);
                    toName0.put(value, value.toString());
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            lookupByName = Collections.unmodifiableMap(lookupByName0);
            toName = Collections.unmodifiableMap(toName0);
        }
    }
}
