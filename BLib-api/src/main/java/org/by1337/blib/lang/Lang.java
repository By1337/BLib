package org.by1337.blib.lang;

import org.by1337.blib.translation.Translation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Lang {
    private static Translation translation;
    private static Function<String, String> provider = s -> s;

    public static void loadTranslations(Translation translation) {
        if (Lang.translation != null) throw new UnsupportedOperationException();
        Lang.translation = translation;
        provider = s -> getOrDefault(translation.translate(s), s);
    }

    public static String getMessage(String key) {
        return provider.apply(key);
    }

    @NotNull
    private static <T> T getOrDefault(@Nullable T value, @NotNull T def) {
        if (value == null) return def;
        return value;
    }

}
