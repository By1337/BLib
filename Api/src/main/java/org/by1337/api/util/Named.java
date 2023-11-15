package org.by1337.api.util;

import org.by1337.api.util.NameKey;
import org.jetbrains.annotations.NotNull;

public interface Named {
    @NotNull
    NameKey getName();
}
