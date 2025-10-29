package org.by1337.blib.util.reflect;

import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Reflect {


    public static final Unsafe unsafe;
    public static final MethodHandles.Lookup implLookup;

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    public static MethodHandles.Lookup getImplLookup() {
        return implLookup;
    }


    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);

            Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");

            implLookup = (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field));

        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }
}
