package org.by1337.v1_16_5;

import org.by1337.api.util.AsyncCatcher;
import org.jetbrains.annotations.NotNull;

public class AsyncCatcherImpl implements AsyncCatcher {
    @Override
    public void catchOp(@NotNull String identifier) {
        org.spigotmc.AsyncCatcher.catchOp(identifier);
    }
}
