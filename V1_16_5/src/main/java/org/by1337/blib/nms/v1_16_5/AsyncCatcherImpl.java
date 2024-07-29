package org.by1337.blib.nms.v1_16_5;

import org.by1337.blib.util.AsyncCatcher;
import org.jetbrains.annotations.NotNull;
@Deprecated
public class AsyncCatcherImpl implements AsyncCatcher {
    @Override
    public void catchOp(@NotNull String identifier) {
        org.spigotmc.AsyncCatcher.catchOp(identifier);
    }
}
