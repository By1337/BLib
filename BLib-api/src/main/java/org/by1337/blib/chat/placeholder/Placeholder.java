package org.by1337.blib.chat.placeholder;

import org.by1337.blib.chat.Placeholderable;
import org.by1337.blib.chat.placeholder.auto.PlaceholderFinder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class Placeholder implements Placeholderable {
    public static final Placeholder EMPTY = new Placeholder();
    protected final Map<String, Supplier<Object>> placeholders = new HashMap<>();

    public Placeholder() {
        registerPlaceholders(PlaceholderFinder.findPlaceholders(this, getClass()).entrySet());
    }

    public void registerPlaceholder(@NotNull String placeholder, Supplier<Object> supplier) {
        placeholders.put(placeholder, supplier);
    }

    public void registerPlaceholders(Placeholder placeholder) {
        registerPlaceholders(placeholder.placeholders.entrySet());
    }
    public void registerPlaceholders(Collection<Map.Entry<String, Supplier<Object>>> list) {
        for (var entry : list) {
            registerPlaceholder(entry.getKey(), entry.getValue());
        }
    }

    public static Placeholder empty(){
        return EMPTY;
    }

    @Override
    public String replace(String string) {
        StringBuilder sb = new StringBuilder(string);
        for (Map.Entry<String, Supplier<Object>> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();
            int len = placeholder.length();
            int pos = sb.indexOf(placeholder);
            while (pos != -1) {
                var replaceTo = String.valueOf(entry.getValue().get());
                sb.replace(pos, pos + len, replaceTo);
                pos = sb.indexOf(placeholder, pos + replaceTo.length());
            }
        }
        return sb.toString();
    }

    public Set<Map.Entry<String, Supplier<Object>>> entrySet() {
        return placeholders.entrySet();
    }

}
