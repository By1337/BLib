package org.by1337.blib.fastutil.block;

public interface BlockReplacerProcessor {

    void unload(boolean force);

    default void unload() {
        unload(false);
    }
}
