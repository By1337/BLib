package org.by1337.blib.chat;

import org.jetbrains.annotations.NotNull;

@Deprecated(forRemoval = true, since = "1.0.7.1-beta")
public abstract class Placeholder {
    private final String placeholder;

    public Placeholder(@NotNull String placeholder) {
        this.placeholder = placeholder;
    }

    @NotNull
    public String getPlaceholder() {
        return placeholder;
    }

    @NotNull
    public abstract String getValue();
}
