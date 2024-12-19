package org.by1337.blib.command.argument;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Sound;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.by1337.blib.lang.Lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ArgumentSound<T> extends Argument<T> {
    private static final Map<String, Sound> LOOKUP_BY_NAME;
    private static final List<String> KEYS_LIST;
    private static final List<String> FIRST_TWENTY_ITEMS;

    public ArgumentSound(String name) {
        super(name, List::of);
    }


    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        String str = reader.hasNext() ? ArgumentUtils.readString(reader) : "";
        if (str.isEmpty()) {
            return;
        }
        Sound sound = LOOKUP_BY_NAME.get(str.toLowerCase(Locale.ENGLISH));
        if (sound == null) {
            throw new CommandSyntaxError(Lang.getMessage("constant-not-found-more"), str, KEYS_LIST.subList(0, 5), KEYS_LIST.size() - 10);
        }
        argumentMap.put(name, sound);
    }

    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        String str = reader.hasNext() ? ArgumentUtils.readString(reader) : "";
        if (str.isEmpty())
            addSuggestions(builder, FIRST_TWENTY_ITEMS);
        else {
            List<String> result = new ArrayList<>();
            String input = str.toLowerCase(Locale.ENGLISH);
            int x = 0;
            for (String s : KEYS_LIST) {
                if (s.startsWith(input)) {
                    result.add(s);
                    if (x++ > 30) break;
                }
            }
            addSuggestions(builder, result);
        }
    }

    static {
        LOOKUP_BY_NAME = new HashMap<>();
        if (Sound.class.isEnum()) {
            for (Sound value : Sound.values()) {
                LOOKUP_BY_NAME.put(value.name().toLowerCase(Locale.ENGLISH), value);
                LOOKUP_BY_NAME.put(value.getKey().toString(), value);
                LOOKUP_BY_NAME.put(value.getKey().getKey(), value);
            }
        } else {
            try { // in versions >=1.21.3 Sound is no longer enum
                for (Field field : Sound.class.getFields()) {
                    field.setAccessible(true);
                    if (!Modifier.isStatic(field.getModifiers())) continue;
                    if (field.getType() != Sound.class) continue;
                    Sound sound = (Sound) field.get(null);

                    LOOKUP_BY_NAME.put(field.getName().toLowerCase(Locale.ENGLISH), sound);
                    LOOKUP_BY_NAME.put(sound.getKey().toString(), sound);
                    LOOKUP_BY_NAME.put(sound.getKey().getKey(), sound);

                }
            } catch (Throwable t) {
                throw new ExceptionInInitializerError(t);
            }
        }
        KEYS_LIST = List.copyOf(LOOKUP_BY_NAME.keySet());
        FIRST_TWENTY_ITEMS = KEYS_LIST.subList(0, 20);
    }
}