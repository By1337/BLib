package org.by1337.blib.chat.placeholder;

import org.by1337.blib.chat.Placeholderable;

public class BiPlaceholder implements Placeholderable {
    private final Placeholderable first;
    private final Placeholderable second;

    public BiPlaceholder(Placeholderable first, Placeholderable second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String replace(String string) {
        return first.replace(second.replace(string));
    }
}
