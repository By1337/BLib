package org.by1337.blib.profiler;

import net.kyori.adventure.text.Component;

public interface IProfiler {
    void start(String name);

    void end(String name);

    Component report();
}
