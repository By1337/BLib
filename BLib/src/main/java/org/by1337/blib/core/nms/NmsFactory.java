package org.by1337.blib.core.nms;

import org.by1337.blib.core.BLib;
import org.by1337.blib.core.nms.verify.NMSVerify;
import org.by1337.blib.core.util.NopCreator;
import org.by1337.blib.util.Version;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NmsFactory {
    private static final Version DEFAULT_VERSION;
    public static final NmsFactory INSTANCE = new NmsFactory();
    private static final Logger log = LoggerFactory.getLogger("BLib#NMS");
    private final Map<Class<?>, ByVersionHolder> creators = new HashMap<>();
    private final Map<String, Supplier<?>> nmsSuppliers = new HashMap<>();

    private NmsFactory() {
    }

    public static NmsFactory get() {
        return INSTANCE;
    }

    public void addNmsClass(String clazz, Supplier<?> supplier) {
        if (nmsSuppliers.put(clazz, supplier) != null) {
            log.warn("NMS already registered for class {}", clazz);
        }
    }

    public <T> void register(Class<T> clazz, Version version, Supplier<? extends T> supplier) {
        creators.computeIfAbsent(clazz, k -> new ByVersionHolder(clazz)).put(version, new LazyLoad<>(supplier));
    }

    public <T> T get(Class<T> cl) {
        return get(cl, DEFAULT_VERSION);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> cl, Version version) {
        try {
            var byVersionHolder = creators.get(cl);
            if (byVersionHolder != null) {
                var creator = byVersionHolder.findFor(version);
                if (creator != null) {
                    return (T) creator.get();
                }
            } else {
                log.warn("No ByVersionHolder registered for {}", cl);
            }
        } catch (Throwable e) {
            log.warn("Could not get ByVersionHolder for class {}", cl, e);
        }
        log.error("Use NOP impl for {}", cl.getTypeName());
        return NopCreator.create(cl);
    }

    public Map<String, Supplier<?>> nmsSuppliers() {
        return Collections.unmodifiableMap(nmsSuppliers);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NmsFactory{\n");
        for (Map.Entry<Class<?>, ByVersionHolder> entry : creators.entrySet()) {
            sb.append(entry.getKey().getSimpleName()).append(" ").append(entry.getValue()).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }

    static {
        String version = System.getProperty("blib.nms.version");
        if (version != null) {
            var v = Version.getById(version);
            if (v == null) {
                log.warn("Unknown NMS version {}", version);
                DEFAULT_VERSION = Version.VERSION;
            } else {
                DEFAULT_VERSION = v;
            }
        } else {
            DEFAULT_VERSION = Version.VERSION;
        }
        NMSBootstrap.bootstrap();
    }

    private static class LazyLoad<T> implements Supplier<T> {
        private final Supplier<@NotNull T> supplier;
        private T value;

        public LazyLoad(Supplier<@NotNull T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            try {
                return value == null ? value = supplier.get() : value;
            } catch (Throwable t) {
                if (BLib.DEBUG) {
                    BLib.getInstance().getSLF4JLogger().error("Exception while calling LazyLoad", t);
                }
                return null;
            }
        }

        @Override
        public String toString() {
            return "LazyLoad{" +
                    "supplier=" + supplier +
                    '}';
        }
    }

    private static class ByVersionHolder {
        private static final Version[] VALUES = Version.values();
        private final EnumMap<Version, LazyLoad<?>> map = new EnumMap<>(Version.class);
        private final Class<?> nms;

        private ByVersionHolder(Class<?> nms) {
            this.nms = nms;
        }

        public void put(Version version, LazyLoad<?> value) {
            map.put(version, value);
        }

        public LazyLoad<?> findFor(Version version) {
            LazyLoad<?> v;
            try {
                v = map.get(version);
                if (v != null) {
                    new NMSVerify().verify(v.get().getClass());
                    return v;
                }
                v = map.get(Version.UNKNOWN);
                if (v != null) {
                    new NMSVerify().verify(v.get().getClass());
                    return v;
                }
            } catch (Throwable t) {
                log.error("Failed to create native nms impl for {}", nms.getTypeName());
            }
            log.warn("Not found native {} implementation for version {}", nms.getTypeName(), version.getVer());
            for (int i = VALUES.length - 1; i >= 0; i--) {
                var ver = VALUES[i];
                v = map.get(ver);
                if (v == null) continue;
                try {
                    Object obj = v.get();
                    if (obj == null) throw new NullPointerException();
                    new NMSVerify().verify(obj.getClass());
                } catch (Throwable t) {
                    log.warn(t.getMessage(), t);
                    continue;
                }
                log.info("Use {} nms version for {}", ver.getVer(), nms.getTypeName());
                return v;
            }

            return null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Version, LazyLoad<?>> entry : map.entrySet()) {
                Version version = entry.getKey();
                LazyLoad<?> value = entry.getValue();
                sb.append(version.getVer()).append(": ").append(value).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            return sb.toString();
        }
    }
}
