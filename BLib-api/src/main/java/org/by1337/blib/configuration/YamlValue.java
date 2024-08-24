package org.by1337.blib.configuration;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.by1337.blib.configuration.adapter.AdapterRegistry;
import org.by1337.blib.configuration.reflection.GsonYamlConvertor;
import org.by1337.blib.geom.*;
import org.by1337.blib.util.NameKey;
import org.by1337.blib.util.Pair;
import org.by1337.blib.util.SpacedNameKey;
import org.by1337.blib.world.BlockPosition;
import org.by1337.blib.world.Vector2D;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YamlValue {
    private final @Nullable Object value;

    public YamlValue(@Nullable Object value) {
        this.value = value;
    }

    public <T> T getAs(Class<T> type) {
        return AdapterRegistry.getAs(value, type);
    }

    public <T> T getAs(Class<T> type, T def) {
        if (value == null) return def;
        return getAs(type);
    }

    public <T> T deserializeFromYaml(Class<T> type) {
        return GsonYamlConvertor.deserializeFromYaml(type, new YamlContext(YamlContext.getMemorySection(value)));
    }


    public <T> List<T> getAsList(Function<YamlValue, T> function, List<T> def) {
        if (value == null) return def;
        return getAsList(function);
    }

    public <T> List<T> getAsList(Class<T> type) {
        return getAsList(v -> v.getAs(type));
    }

    public <T> List<T> getAsList(Function<YamlValue, T> function) {
        return stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public Stream<YamlValue> stream() {
        return ((List<?>) value).stream().map(YamlValue::new);
    }

    public Stream<Pair<YamlValue, YamlValue>> mapStream() {
        Map<String, ?> map0 = YamlContext.getMemorySection(value).getValues(false);
        return map0.entrySet().stream().map(e -> Pair.of(new YamlValue(e.getKey()), new YamlValue(e.getValue())));
    }

    public <V> Map<String, V> getAsMap(Class<V> valueType) {
        return getAsMap(String.class, valueType);
    }

    public <V> Map<String, V> getAsMap(Class<V> valueType, Map<String, V> def) {
        return getAsMap(String.class, valueType, def);
    }

    public <K, V> Map<K, V> getAsMap(Class<K> keyType, Class<V> valueType, Map<K, V> def) {
        if (value == null) return def;
        return getAsMap(keyType, valueType);
    }

    public <K, V> Map<K, V> getAsMap(Class<K> keyType, Class<V> valueType) {
        return getAsMap(k -> k.getAs(keyType), v -> v.getAs(valueType));
    }

    public <K, V> Map<K, V> getAsMap(Function<YamlValue, K> keyMapper, Function<YamlValue, V> valueMapper, Map<K, V> def) {
        if (value == null) return def;
        return getAsMap(keyMapper, valueMapper);
    }

    public <V> Map<String, V> getAsMap(Function<YamlValue, V> valueMapper, Map<String, V> def) {
        if (value == null) return def;
        return getAsMap(valueMapper);
    }

    public <V> Map<String, V> getAsMap(Function<YamlValue, V> valueMapper) {
        return getAsMap(YamlValue::getAsString, valueMapper);
    }

    public <K, V> Map<K, V> getAsMap(Function<YamlValue, K> keyMapper, Function<YamlValue, V> valueMapper) {
        Map<String, ?> map0 = YamlContext.getMemorySection(value).getValues(false);
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<String, ?> entry : map0.entrySet()) {
            map.put(keyMapper.apply(new YamlValue(entry.getKey())), valueMapper.apply(new YamlValue(entry.getValue())));
        }
        return map;
    }

    public boolean isPrimitive() {
        return !(value instanceof Map<?, ?>) && !(value instanceof ConfigurationSection);
    }

    public Double getAsDouble(Double def) {
        return getAs(Double.class, def);
    }

    public Double getAsDouble() {
        return getAs(Double.class);
    }

    public Float getAsFloat(Float def) {
        return getAs(Float.class, def);
    }

    public Float getAsFloat() {
        return getAs(Float.class);
    }

    public Integer getAsInteger(Integer def) {
        return getAs(Integer.class, def);
    }

    public Integer getAsInteger() {
        return getAs(Integer.class);
    }

    public Long getAsLong(Long def) {
        return getAs(Long.class, def);
    }

    public Long getAsLong() {
        return getAs(Long.class);
    }

    public Boolean getAsBoolean(Boolean def) {
        return getAs(Boolean.class, def);
    }

    public Boolean getAsBoolean() {
        return getAs(Boolean.class);
    }

    public Short getAsShort(Short def) {
        return getAs(Short.class, def);
    }

    public Short getAsShort() {
        return getAs(Short.class);
    }

    public NameKey getAsNameKey(NameKey def) {
        return getAs(NameKey.class, def);
    }

    public NameKey getAsNameKey() {
        return getAs(NameKey.class);
    }

    public Material getAsMaterial(Material def) {
        return getAs(Material.class, def);
    }

    public Material getAsMaterial() {
        return getAs(Material.class);
    }

    public Particle getAsParticle(Particle def) {
        return getAs(Particle.class, def);
    }

    public Particle getAsParticle() {
        return getAs(Particle.class);
    }

    public ItemStack getAsItemStack(ItemStack def) {
        return getAs(ItemStack.class, def);
    }

    public ItemStack getAsItemStack() {
        return getAs(ItemStack.class);
    }

    public Color getAsColor(Color def) {
        return getAs(Color.class, def);
    }

    public Color getAsColor() {
        return getAs(Color.class);
    }

    public SpacedNameKey getAsSpacedNameKey(SpacedNameKey def) {
        return getAs(SpacedNameKey.class, def);
    }

    public SpacedNameKey getAsSpacedNameKey() {
        return getAs(SpacedNameKey.class);
    }

    public String getAsString(String def) {
        return getAs(String.class, def);
    }

    public String getAsString() {
        return getAs(String.class);
    }

    public Object getAsObject(Object def) {
        return getAs(Object.class, def);
    }

    public Object getAsObject() {
        return getAs(Object.class);
    }

    public UUID getAsUUID(UUID def) {
        return getAs(UUID.class, def);
    }

    public UUID getAsUUID() {
        return getAs(UUID.class);
    }

    public Biome getAsBiome(Biome def) {
        return getAs(Biome.class, def);
    }

    public Biome getAsBiome() {
        return getAs(Biome.class);
    }

    public ItemFlag getAsItemFlag(ItemFlag def) {
        return getAs(ItemFlag.class, def);
    }

    public ItemFlag getAsItemFlag() {
        return getAs(ItemFlag.class);
    }

    public Vector getAsVector(Vector def) {
        return getAs(Vector.class, def);
    }

    public Vector getAsVector() {
        return getAs(Vector.class);
    }

    public BlockPosition getAsBlockPosition(BlockPosition def) {
        return getAs(BlockPosition.class, def);
    }

    public BlockPosition getAsBlockPosition() {
        return getAs(BlockPosition.class);
    }

    public Vector2D getAsVector2D(Vector2D def) {
        return getAs(Vector2D.class, def);
    }

    public Vector2D getAsVector2D() {
        return getAs(Vector2D.class);
    }

    public YamlContext getAsYamlContext(YamlContext def) {
        return getAs(YamlContext.class, def);
    }

    public YamlContext getAsYamlContext() {
        return getAs(YamlContext.class);
    }

    public MemorySection getAsMemorySection(MemorySection def) {
        return getAs(MemorySection.class, def);
    }

    public MemorySection getAsMemorySection() {
        return getAs(MemorySection.class);
    }

    public AABB getAsAABB(AABB def) {
        return getAs(AABB.class, def);
    }

    public AABB getAsAABB() {
        return getAs(AABB.class);
    }

    public IntAABB getAsIntAABB(IntAABB def) {
        return getAs(IntAABB.class, def);
    }

    public IntAABB getAsIntAABB() {
        return getAs(IntAABB.class);
    }

    public Vec2d getAsVec2d(Vec2d def) {
        return getAs(Vec2d.class, def);
    }

    public Vec2d getAsVec2d() {
        return getAs(Vec2d.class);
    }

    public Vec2i getAsVec2i(Vec2i def) {
        return getAs(Vec2i.class, def);
    }

    public Vec2i getAsVec2i() {
        return getAs(Vec2i.class);
    }

    public Vec3d getAsVec3d(Vec3d def) {
        return getAs(Vec3d.class, def);
    }

    public Vec3d getAsVec3d() {
        return getAs(Vec3d.class);
    }

    public Vec3i getAsVec3i(Vec3i def) {
        return getAs(Vec3i.class, def);
    }

    public Vec3i getAsVec3i() {
        return getAs(Vec3i.class);
    }

}
