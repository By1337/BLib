package org.by1337.lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AsyncCatcher {
    public static void catchOp(String s){
        try {
            Class<?> clazz = Class.forName("org.spigotmc.AsyncCatcher");
            Method method = clazz.getMethod("catchOp", String.class);
            method.invoke(null, s);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
