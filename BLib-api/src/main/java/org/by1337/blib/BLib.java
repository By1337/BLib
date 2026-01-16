package org.by1337.blib;

import org.bukkit.Bukkit;
import org.by1337.blib.command.CommandUtil;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class BLib {
    private static Api api;

    public static void setApi(@NotNull Api api) {
        if (BLib.api != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Api");
        }
        BLib.api = api;
    }


    @NotNull
    public static Logger getLogger() {
        return api.getLogger();
    }

    public static @NotNull CommandUtil getCommandUtil() {
        return api.getCommandUtil();
    }

    public static void catchOp(@NotNull String identifier) {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Asynchronous " + identifier + "!");
        }
    }

    @NotNull
    public static Api getApi() {
        return api;
    }
}

