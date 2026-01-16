package org.by1337.blib.unsafe;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URL;

public interface PluginClasspathUtil {
    PluginClasspathUtil INSTANCE = new PluginClasspathUtilImpl();
    void addUrl(Plugin plugin, File file);

    void addUrl(Plugin plugin, URL url);

    Class<?> defineClass(Plugin plugin, String name, byte[] b, int off, int len);
}
