package org.by1337.blib.core.nms;

import org.by1337.blib.core.BLib;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.util.Version;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class NMSBootstrap {
    private static final ClassLoader LOADER = NMSBootstrap.class.getClassLoader();

    public static void bootstrap() {
        try {
            bootstrap0();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void bootstrap0() throws Throwable {
        NmsFactory factory = NmsFactory.get();

        String data = new String(BLib.getInstance().getResource("nms-path.txt").readAllBytes(), StandardCharsets.UTF_8);

        String[] array = data.split("\n");

        for (String s : array) {
            if (s.isEmpty()) continue;
            String[] split = s.split(" ");
            String nmsAccessor = split[0];
            Class<T> apiClass = (Class<T>) Class.forName(split[1]);
            try {
                Class<?> impl = Class.forName(split[0], false, LOADER);
                NMSAccessor acc = impl.getAnnotation(NMSAccessor.class);
                List<Version> versions = new ArrayList<>(List.of(acc.forVersions()));
                Version[] values = Version.values();
                if (acc.from() != Version.UNKNOWN) {
                    Version to;
                    if (acc.to() == Version.UNKNOWN) {
                        to = Version.LAST_VERSION;
                    } else {
                        to = acc.to();
                    }
                    versions.addAll(Arrays.asList(values).subList(acc.from().ordinal(), to.ordinal() + 1));
                }
                ClassCreator<? extends T> nmsCreator = new ClassCreator<>(nmsAccessor);
                for (Version version : versions) {
                    factory.register(apiClass, version, nmsCreator);
                }
                factory.addNmsClass(nmsAccessor, nmsCreator);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ClassCreator<T> implements Supplier<T> {
        private final String className;

        public ClassCreator(String className) {
            this.className = className;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get() {
            try {
                Class<?> cl = Class.forName(className);
                Constructor<?> constructor = cl.getConstructor();
                return (T) constructor.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "ClassCreator{" +
                    "className='" + className + '\'' +
                    '}';
        }
    }

}
