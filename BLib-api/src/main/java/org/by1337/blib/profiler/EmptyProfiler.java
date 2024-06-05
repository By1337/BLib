package org.by1337.blib.profiler;

import net.kyori.adventure.text.Component;

public class EmptyProfiler implements IProfiler{
    @Override
    public void start(String name) {
    }

    @Override
    public void end(String name) {
    }

    @Override
    public Component report() {
        return Component.text("has no info!");
    }
}
