package org.by1337.blib.block.custom.registry;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.Location;
import org.by1337.blib.block.custom.data.CustomBlockData;
import org.by1337.blib.util.Pair;
import org.by1337.blib.world.BlockPosition;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WorldRegistry<T> {
    public static final WorldRegistry<CustomBlockData> CUSTOM_BLOCK_REGISTRY = new WorldRegistry<>();
    private final Map<String, WorldData<T>> worldDataMap = new HashMap<>();

    public void add(Location location, T data) {
        add(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                data
        );
    }
    public void add(String world, int x, int y, int z, T value) {
        worldDataMap
                .computeIfAbsent(world, k -> new WorldData<>())
                .add(x, y, z, value);
    }

    public void clear() {
        worldDataMap.clear();
    }

    @Nullable
    @CanIgnoreReturnValue
    public T remove(String world, int x, int y, int z) {
        WorldData<T> worldData = worldDataMap.get(world);
        if (worldData != null) {
            return worldData.remove(x, y, z);
        }
        return null;
    }

    @Nullable
    @CanIgnoreReturnValue
    public T remove(Location location) {
        return remove(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Nullable
    public T get(Location location) {
        return get(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Nullable
    public T get(String world, int x, int y, int z) {
        WorldData<T> worldData = worldDataMap.get(world);
        return (worldData != null) ? worldData.get(x, y, z) : null;
    }

    public Collection<T> getAllInWorld(String world) {
        var data = worldDataMap.get(world);
        if (data == null) return new ArrayList<>();
        Collection<T> list = new ArrayList<>();
        for (WorldData.PosData<WorldData.PosData<T>> value : data.xData.values()) {
            for (WorldData.PosData<T> tPosData : value.data.values()) {
                list.addAll(tPosData.data.values());
            }
        }
        return list;
    }

    public Map<String, List<Pair<BlockPosition, T>>> getAll() {
        Map<String, List<Pair<BlockPosition, T>>> map = new HashMap<>();
        for (String s : worldDataMap.keySet()) {
            var data = worldDataMap.get(s);
            var list = data.getAll();
            if (!list.isEmpty()) {
                List<Pair<BlockPosition, T>> list1 = new ArrayList<>(list);
                map.put(s, list1);
            }
        }
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldRegistry<?> that = (WorldRegistry<?>) o;
        return Objects.equals(worldDataMap, that.worldDataMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldDataMap);
    }

    static class WorldData<T> {
        private final Map<Integer, PosData<PosData<T>>> xData = new HashMap<>();

        public void add(int x, int y, int z, T value) {
            var yData = xData.getOrDefault(x, new PosData<>());
            var zData = yData.data.getOrDefault(y, new PosData<>());
            zData.add(z, value);
            yData.data.put(y, zData);
            xData.put(x, yData);
        }

        public List<Pair<BlockPosition, T>> getAll() {
            List<Pair<BlockPosition, T>> list = new ArrayList<>();
            for (Map.Entry<Integer, PosData<PosData<T>>> xData : xData.entrySet()) {
                for (Map.Entry<Integer, PosData<T>> yData : xData.getValue().data.entrySet()) {
                    for (Map.Entry<Integer, T> zData : yData.getValue().data.entrySet()) {
                        list.add(Pair.of(
                                new BlockPosition(xData.getKey(), yData.getKey(), zData.getKey()), zData.getValue()
                        ));
                    }
                }
            }
            return list;
        }

        @Nullable
        public T get(int x, int y, int z) {
            var yData = xData.get(x);
            if (yData == null) return null;
            var zData = yData.data.get(y);
            if (zData == null) return null;
            return zData.get(z);
        }

        public T remove(int x, int y, int z) {
            var yData = xData.get(x);
            if (yData == null) return null;
            var zData = yData.data.get(y);
            if (zData == null) return null;
            var val = zData.data.remove(z);
            if (zData.data.isEmpty()) {
                yData.data.remove(y);
                if (yData.data.isEmpty()) {
                    xData.remove(x);
                }
            }
            return val;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WorldData<?> worldData = (WorldData<?>) o;
            return Objects.equals(xData, worldData.xData);
        }

        @Override
        public int hashCode() {
            return Objects.hash(xData);
        }

        static class PosData<T> {
            private final Map<Integer, T> data = new HashMap<>();

            public void add(int i, T value) {
                data.put(i, value);
            }

            @Nullable
            public T get(int i) {
                return data.get(i);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                PosData<?> posData = (PosData<?>) o;
                return Objects.equals(data, posData.data);
            }

            @Override
            public int hashCode() {
                return Objects.hash(data);
            }
        }
    }
}
