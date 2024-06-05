package org.by1337.blib.fastutil;

import org.by1337.blib.configuration.YamlContext;

public class FastUtilSetting {
    private final int maxReplacesPerTick;
    private final long maxTimeOut;

    public FastUtilSetting(int maxReplacesPerTick, long maxTimeOut) {
        this.maxReplacesPerTick = maxReplacesPerTick;
        this.maxTimeOut = maxTimeOut;
    }

    public FastUtilSetting(YamlContext context) {
        maxTimeOut = context.getAsLong("max-time-out");
        maxReplacesPerTick = context.getAsInteger("max-replaces-per-tick");
    }

    public int getMaxReplacesPerTick() {
        return maxReplacesPerTick;
    }

    public long getMaxTimeOut() {
        return maxTimeOut;
    }
}
