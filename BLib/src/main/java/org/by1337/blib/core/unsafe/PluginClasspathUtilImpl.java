package org.by1337.blib.core.unsafe;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.by1337.blib.unsafe.PluginClasspathUtil;
import org.by1337.blib.util.invoke.LambdaMetafactoryUtil;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

public class PluginClasspathUtilImpl implements PluginClasspathUtil {
    private static final MethodHandle ADD_URL;
    private static final MethodHandle DEFINE_CLASS;
    private static final Function<Plugin, ClassLoader> GET_CLASS_LOADER;


    public void addUrl(Plugin plugin, File file) {
        try {
            addUrl(plugin, file.toPath().toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUrl(Plugin plugin, URL url) {
        try {
            ADD_URL.invoke(GET_CLASS_LOADER.apply(plugin), url);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> defineClass(Plugin plugin, String name, byte[] b, int off, int len) {
        try {
            return (Class<?>) DEFINE_CLASS.invoke(GET_CLASS_LOADER.apply(plugin), name, b, off, len);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafe.get(null);
            Field implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");


            MethodHandles.Lookup lookup = (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(implLookup), unsafe.staticFieldOffset(implLookup));
            Class<?> urlClassType = Class.forName("java.net.URLClassLoader");

            ADD_URL = lookup.findVirtual(urlClassType, "addURL", MethodType.methodType(Void.TYPE, URL.class));
            DEFINE_CLASS = lookup.findVirtual(urlClassType, "defineClass", MethodType.methodType(Class.class, String.class, byte[].class, int.class, int.class));

            Field classLoader = JavaPlugin.class.getDeclaredField("classLoader");
            classLoader.setAccessible(true);
            GET_CLASS_LOADER = LambdaMetafactoryUtil.getterOf(classLoader);
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }
}
