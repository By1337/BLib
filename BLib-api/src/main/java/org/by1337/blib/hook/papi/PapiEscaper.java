package org.by1337.blib.hook.papi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Function;

public class PapiEscaper {
    private static final LocalExpansionManager LOCAL_EXPANSION_MANAGER;
    private static final Function<String, Boolean> HAS_PLACEHOLDER;

    static {
        if (Bukkit.getServer() != null && !Bukkit.getServer().getClass().getCanonicalName().contains("MockitoMock")) {
            LOCAL_EXPANSION_MANAGER = PlaceholderAPIPlugin.getInstance().getLocalExpansionManager();
            HAS_PLACEHOLDER = s -> LOCAL_EXPANSION_MANAGER.getExpansion(s) != null;
        } else {
            LOCAL_EXPANSION_MANAGER = null;
            HAS_PLACEHOLDER = s -> true;
        }
    }

    public static String escape(@NotNull final String text) {

        final char[] chars = text.toCharArray();
        final StringBuilder builder = new StringBuilder(text.length());

        final StringBuilder identifier = new StringBuilder();
        final StringBuilder parameters = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            final char l = chars[i];

            if (l != '%' || i + 1 >= chars.length) {
                builder.append(l);
                continue;
            }

            boolean identified = false;
            boolean invalid = true;
            boolean hadSpace = false;

            while (++i < chars.length) {
                final char p = chars[i];

                if (p == ' ' && !identified) {
                    hadSpace = true;
                    break;
                }
                if (p == '%') {
                    invalid = false;
                    break;
                }

                if (p == '_' && !identified) {
                    identified = true;
                    continue;
                }

                if (identified) {
                    parameters.append(p);
                } else {
                    identifier.append(p);
                }
            }

            final String identifierString = identifier.toString();
            final String parametersString = parameters.toString();

            identifier.setLength(0);
            parameters.setLength(0);

            boolean validPlaceholder = !invalid && HAS_PLACEHOLDER.apply(identifierString.toLowerCase(Locale.ROOT));


            if (!invalid && validPlaceholder) {
                builder.append('%');
            }
            builder.append('%').append(identifierString);

            if (identified) {
                builder.append('_').append(parametersString);
            }

            if (hadSpace) {
                builder.append(' ');
            }

            if (!invalid) {
                builder.append('%');
                if (validPlaceholder)
                    builder.append('%');
            }
        }

        return builder.toString();
    }
}
