package org.by1337.blib.chat.util;

@Deprecated(since = "1.0.7.1")
public enum LogLevel {
    NONE(-1),
    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(2);

    private final int lvl;

    LogLevel(int lvl) {
        this.lvl = lvl;
    }

    public int getLvl() {
        return lvl;
    }
}
