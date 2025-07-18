package org.by1337.blib.core.util;

import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class NopCreator {
    public static <T> T create(Class<T> iface) {
        if (!iface.isInterface())
            throw new IllegalArgumentException("Not an interface: " + iface);

        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                (proxy, method, args) -> {
                    throw new UnsupportedOperationException(
                            "NOP: method " + method.getName() + " is not implemented");
                }
        );
    }
}
