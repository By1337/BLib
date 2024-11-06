package org.by1337.blib.chat.placeholder.auto;

import org.by1337.blib.chat.placeholder.Placeholder;
import org.by1337.blib.chat.placeholder.auto.annotations.*;
import org.by1337.blib.chat.placeholder.auto.name.CamelCaseToSnakeCaseNameFactory;
import org.by1337.blib.util.Pair;
import org.by1337.blib.util.invoke.LambdaMetafactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class PlaceholderFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger("BLib");

    private static final Map<Class<?>, PlaceholderNameFactory> typeToNameFactory = new HashMap<>();
    private static final Map<String, Pair<ClassLoader, Map<String, Function<Object, Object>>>> CACHE = new HashMap<>();

    public static Map<String, Supplier<Object>> findPlaceholders(Placeholder instance, Class<?> in) {
        Map<String, Function<Object, Object>> raw = findPlaceholders(in);
        if (raw.isEmpty()) return Map.of();
        Map<String, Supplier<Object>> result = new HashMap<>();
        raw.forEach((k, f) -> result.put(k, () -> f.apply(instance)));
        return result;
    }

    private static Map<String, Function<Object, Object>> findPlaceholders(Class<?> in) {
        if (!in.isAnnotationPresent(AutoPlaceholderGeneration.class)) return Map.of();
        var cached = CACHE.get(in.getCanonicalName());
        if (cached != null && cached.getLeft() == in.getClassLoader()) return cached.getRight();
        boolean includeAll = in.isAnnotationPresent(IncludeAllInPlaceholders.class);
        Map<String, Function<Object, Object>> placeholders = new HashMap<>();
        PlaceholderNameFactory nameFactory = getNameFactory(in);

        for (Field field : in.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (!includeAll && !field.isAnnotationPresent(IncludeInPlaceholders.class)) continue;
            if (field.isAnnotationPresent(ExcludeFromPlaceholders.class)) continue;
            field.setAccessible(true);
            processField(field, placeholders, nameFactory);
        }
        CACHE.put(in.getCanonicalName(), Pair.of(in.getClassLoader(), placeholders));
        return placeholders;
    }

    private static void processField(Field field, Map<String, Function<Object, Object>> map, PlaceholderNameFactory nameFactory) {
        try {
            field.setAccessible(true);
            Function<Object, Object> getter = LambdaMetafactoryUtil.getterOf(field);
            String name;
            if (field.isAnnotationPresent(PlaceholderName.class)) {
                name = field.getAnnotation(PlaceholderName.class).name();
            } else {
                name = nameFactory.toName(field);
            }
            map.put("{" + name + "}", getter);
        } catch (Throwable e) {
            LOGGER.error("Error processing field {}", field.getName(), e);
        }
    }


    private static PlaceholderNameFactory getNameFactory(Class<?> in) {
        if (in.isAnnotationPresent(CustomPlaceholderNameFactory.class)) {
            Class<?> type = in.getAnnotation(CustomPlaceholderNameFactory.class).value();
            return typeToNameFactory.computeIfAbsent(type, k -> {
                try {
                    return (PlaceholderNameFactory) type.getConstructor().newInstance();
                } catch (Throwable t) {
                    LOGGER.error("Failed to create placeholder name factory {}", type.getCanonicalName(), t);
                }
                return CamelCaseToSnakeCaseNameFactory.INSTANCE;
            });
        } else {
            return CamelCaseToSnakeCaseNameFactory.INSTANCE;
        }
    }

}
