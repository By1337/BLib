package org.by1337.blib.chat.placeholder.auto.name;

import org.by1337.blib.chat.placeholder.auto.PlaceholderNameFactory;

import java.lang.reflect.Field;

public class EmptyNameFactory implements PlaceholderNameFactory {
    public static final EmptyNameFactory INSTANCE = new EmptyNameFactory();

    @Override
    public String toName(Field name) {
        return name.getName();
    }
}
