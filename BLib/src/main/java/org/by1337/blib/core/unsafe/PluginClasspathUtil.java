package org.by1337.blib.core.unsafe;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.PluginClassLoader;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PluginClasspathUtil {
    private static final Field LIBRARY_LOADER_FIELD;

    public static void closeCustomLoads(PluginClassLoader plugin) {
        try {
            ClassLoader classLoader = (ClassLoader) LIBRARY_LOADER_FIELD.get(plugin);
            if (classLoader instanceof CustomClassLoader ccl) {
                ccl.close();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> defineClass(PluginClassLoader plugin, String name, byte[] b, int off, int len)
            throws ClassFormatError {
        try {
            ClassLoader classLoader = (ClassLoader) LIBRARY_LOADER_FIELD.get(plugin);
            CustomClassLoader ccl;
            if (classLoader instanceof CustomClassLoader) {
                ccl = (CustomClassLoader) classLoader;
            } else {
                ccl = swap(plugin);
            }
            return ccl.defineClass0(name, b, off, len);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addJarsToClasspath(PluginClassLoader plugin, File... files) {
        addJarsToClasspath(plugin, Bukkit.class.getClassLoader(), files);
    }

    public static void addJarsToClasspath(PluginClassLoader plugin, ClassLoader parent, File... files) {
        try {
            ClassLoader classLoader = (ClassLoader) LIBRARY_LOADER_FIELD.get(plugin);
            URL[] urls = Arrays.stream(files).map(f -> {
                try {
                    return f.toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }).toArray(URL[]::new);
            ClassLoader loader = new URLClassLoader(urls, parent);
            CustomClassLoader ccl;
            if (classLoader instanceof CustomClassLoader c) {
                ccl = c;
            } else {
                ccl = swap(plugin);
            }
            ccl.classLoaders.add(loader);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addJarToClasspath(PluginClassLoader plugin, File file) {
        addJarToClasspath(plugin, file, Bukkit.class.getClassLoader());
    }

    public static void addJarToClasspath(PluginClassLoader plugin, File file, ClassLoader parent) {
        try {
            ClassLoader classLoader = (ClassLoader) LIBRARY_LOADER_FIELD.get(plugin);
            CustomClassLoader ccl;
            if (classLoader instanceof CustomClassLoader c) {
                ccl = c;
            } else {
                ccl = swap(plugin);
            }
            ccl.addToLookup(file, parent);
        } catch (IllegalAccessException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static CustomClassLoader swap(PluginClassLoader loader) throws IllegalAccessException {
        ClassLoader classLoader = (ClassLoader) LIBRARY_LOADER_FIELD.get(loader);
        if (classLoader instanceof CustomClassLoader) {
            throw new IllegalStateException("library class loader is already swapped");
        }
        CustomClassLoader ccl = new CustomClassLoader();
        if (classLoader != null) {
            ccl.swapped = classLoader;
        }
        LIBRARY_LOADER_FIELD.set(loader, ccl);
        return ccl;
    }

    static {
        try {
            LIBRARY_LOADER_FIELD = PluginClassLoader.class.getDeclaredField("libraryLoader");
            LIBRARY_LOADER_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CustomClassLoader extends ClassLoader implements Closeable {
        private ClassLoader swapped;
        private final List<ClassLoader> classLoaders = new CopyOnWriteArrayList<>();

        public void addToLookup(File file, ClassLoader parent) throws MalformedURLException {
            classLoaders.add(new URLClassLoader(new URL[]{file.toURI().toURL()}, parent));
        }

        protected Class<?> defineClass0(String name, byte[] b, int off, int len)
                throws ClassFormatError {
            return defineClass(name, b, off, len);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            try {
                return super.loadClass(name, resolve);
            } catch (ClassNotFoundException ignored) {
            }
            for (ClassLoader classLoader : classLoaders) {
                try {
                    return classLoader.loadClass(name);
                } catch (ClassNotFoundException ignored) {
                }
            }
            if (swapped != null) {
                return swapped.loadClass(name);
            }
            throw new ClassNotFoundException(name);
        }

        public void close() {
            Throwable last = null;
            for (ClassLoader classLoader : classLoaders) {
                if (classLoader instanceof URLClassLoader urlClassLoader) {
                    try {
                        urlClassLoader.close();
                    } catch (Throwable e) {
                        last = e;
                    }
                }
            }
            if (last != null) {
                throw new RuntimeException(last);
            }
        }
    }

}
