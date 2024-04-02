package org.by1337.blib.translation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.OfflinePlayer;
import org.by1337.blib.chat.util.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class Translation {
    @Nullable
    private final Locale useLocale;
    private final Locale defaultLocale;
    private final Map<Locale, Map<String, String>> messages;
    private final Map<String, Map<String, String>> byLanguage;
    private final Message message;

    public Translation(@Nullable Locale useLocale, Locale defaultLocale, Map<Locale, Map<String, String>> messages, Message message) {
        this.useLocale = useLocale;
        this.defaultLocale = defaultLocale;
        this.messages = messages;
        this.message = message;
        byLanguage = new HashMap<>();
        messages.forEach((k, v) -> {
            byLanguage.put(k.getLanguage(), v);
        });
    }

    public static Translation fromJson(Reader reader, Message message) {
        return createGson(message).fromJson(reader, Translation.class);
    }
    public static Gson createGson(Message message){
        return new GsonBuilder()
                .registerTypeAdapter(Translation.class, new TranslationAdapter(message))
                .create();
    }

    @Nullable
    public String translate(String s, @Nullable Locale locale) {
        return translateByLocale(s, useLocale == null ? locale : useLocale);
    }

    @Nullable
    public String translate(String s) {
        return translateByLocale(s, useLocale);
    }

    @Nullable
    public String translateByLocale(String s, @Nullable Locale locale) {
        Map<String, String> map = null;
        if (locale != null) {
            map = messages.get(locale);
            if (map == null) {
                map = byLanguage.get(locale.getLanguage());
            }
        }
        if (map == null) {
            map = messages.get(defaultLocale);
        }
        return map == null ? s : map.get(s);
    }

    @NotNull
    public Component translate(@NotNull Component component) {
        return translate(component, null, null);
    }

    @NotNull
    public Component translate(@NotNull Component component, @Nullable Locale locale, @Nullable OfflinePlayer player) {
        Component component0;
        if (component instanceof TranslatableComponent translatable) {
            String key = translatable.key();
            String translation = translate(key, locale);
            if (translation != null) {
                Object[] args = translatable.args().stream().map(message::getContent).toArray();
                if (args.length != 0) {
                    component0 = message.componentBuilderNoTranslate(String.format(translation, args), player);
                } else {
                    component0 = message.componentBuilderNoTranslate(translation, player);
                }
            } else {
                component0 = component;
            }
        } else {
            component0 = component;
        }
        List<Component> children = new ArrayList<>(component0.children());
        children.replaceAll(c -> translate(c, locale, player));
        return component0.children(children);
    }

    static class TranslationAdapter implements JsonDeserializer<Translation> {

        private final Message message;

        public TranslationAdapter(Message message) {
            this.message = message;
        }

        @Override
        public Translation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String rawUseLocale = jsonObject.getAsJsonPrimitive("use-locale").getAsString();
            String rawDefaultLocale = jsonObject.getAsJsonPrimitive("default-locale").getAsString();

            Type mapType = new TypeToken<Map<String, Map<String, String>>>() {
            }.getType();


            Map<String, Map<String, String>> rawMessages = context.deserialize(jsonObject.getAsJsonObject("messages"), mapType);


            Locale useLocale;
            if (!rawUseLocale.equals("auto-detect")) {
                String[] arr = rawUseLocale.split("_");
                useLocale = new Locale(arr[0], arr[1]);
            } else {
                useLocale = null;
            }
            String[] arr = rawDefaultLocale.split("_");
            Locale defaultLocale = new Locale(arr[0], arr[1]);

            Map<Locale, Map<String, String>> messages = new HashMap<>();

            rawMessages.forEach((k, v) -> {
                v.forEach((locale, msg) -> {
                    String[] arr0 = locale.split("_");
                    var l = new Locale(arr0[0], arr0[1]);
                    var map = messages.getOrDefault(l, new HashMap<>());
                    map.put(k, msg);
                    messages.put(l, map);
                });
            });

            return new Translation(useLocale, defaultLocale, messages, message);
        }
    }
}
