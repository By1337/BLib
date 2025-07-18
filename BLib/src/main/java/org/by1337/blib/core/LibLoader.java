package org.by1337.blib.core;

import org.bukkit.plugin.Plugin;
import org.by1337.blib.core.unsafe.BLibUnsafeImpl;
import org.by1337.blib.core.unsafe.PluginClasspathUtilImpl;
import org.by1337.blib.net.RepositoryUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LibLoader {
    private static final String MAVEN_REPO = "https://repo1.maven.org/maven2";

    static void load(Path libraries, Plugin plugin) {
        List<Path> files = new ArrayList<>();
        if (!isClassExist("org.objectweb.asm.tree.ClassNode")) {
            files.add(RepositoryUtil.downloadIfNotExist(MAVEN_REPO, "org.ow2.asm", "asm-tree", "9.7.1", libraries));
            files.add(RepositoryUtil.downloadIfNotExist(MAVEN_REPO, "org.ow2.asm", "asm", "9.7.1", libraries));
            files.add(RepositoryUtil.downloadIfNotExist(MAVEN_REPO, "org.ow2.asm", "asm-commons", "9.7.1", libraries));
        }
        if (!isClassExist("org.joml.Vector3f")) {
            files.add(RepositoryUtil.downloadIfNotExist(MAVEN_REPO, "org.joml", "joml", "1.10.8", libraries));
        }
        var pathUtil = new PluginClasspathUtilImpl();
        for (Path file : files) {
            pathUtil.addUrl(plugin, file.toFile());
        }
    }

    private static boolean isClassExist(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
