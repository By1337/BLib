package org.by1337.blib.configuration.serialization;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.DataResult;
import blib.com.mojang.serialization.DynamicOps;
import blib.com.mojang.serialization.codecs.PrimitiveCodec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import org.by1337.blib.BLib;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.Experimental
public class BukkitCodecs {
    public static final PrimitiveCodec<NamespacedKey> NAMESPACED_KEY = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<NamespacedKey> read(DynamicOps<T> ops, T t) {
            return ops.getStringValue(t).map(s -> s.contains(":") ? NamespacedKey.fromString(s) : NamespacedKey.minecraft(s));
        }

        @Override
        public <T> T write(DynamicOps<T> ops, NamespacedKey namespacedKey) {
            return ops.createString(namespacedKey.toString());
        }
    };
    public static final Codec<Enchantment> ENCHANTMENT = new PrimitiveCodec<>() {

        @Override
        public <T> DataResult<Enchantment> read(DynamicOps<T> ops, T t) {
            return NAMESPACED_KEY.read(ops, t).map(Enchantment::getByKey);
        }

        @Override
        public <T> T write(DynamicOps<T> ops, Enchantment enchantment) {
            return ops.createString(enchantment.getKey().toString());
        }
    };
    public static final Codec<Vector> VECTOR = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x").forGetter(Vector::getX),
            Codec.DOUBLE.fieldOf("y").forGetter(Vector::getY),
            Codec.DOUBLE.fieldOf("z").forGetter(Vector::getZ)
    ).apply(instance, Vector::new));

    public static final PrimitiveCodec<World> WORLD = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<World> read(DynamicOps<T> ops, T t) {
            return ops.getStringValue(t).map(Bukkit::getWorld);
        }

        @Override
        public <T> T write(DynamicOps<T> ops, World world) {
            return ops.createString(world.getName());
        }
    };
    public static final Codec<Location> LOCATION = RecordCodecBuilder.create(instance -> instance.group(
            Codec.optionalField("world", WORLD, true).forGetter(loc -> Optional.ofNullable(loc.getWorld())),
            Codec.DOUBLE.fieldOf("x").forGetter(Location::getX),
            Codec.DOUBLE.fieldOf("y").forGetter(Location::getY),
            Codec.DOUBLE.fieldOf("z").forGetter(Location::getZ),
            Codec.FLOAT.fieldOf("pitch").forGetter(Location::getPitch),
            Codec.FLOAT.fieldOf("yaw").forGetter(Location::getYaw)
    ).apply(instance, BukkitCodecs::createLoc));

    private static Location createLoc(Optional<World> world, double x, double y, double z, float yaw, float pitch) {
        return new Location(world.orElse(null), x, y, z, yaw, pitch);
    }

    public static final PrimitiveCodec<ItemStack> ITEM_STACK = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<ItemStack> read(DynamicOps<T> ops, T t) {
            return ops.getStringValue(t).map(BLib.getApi().getItemStackSerialize()::deserialize);
        }

        @Override
        public <T> T write(DynamicOps<T> ops, ItemStack itemStack) {
            return ops.createString(BLib.getApi().getItemStackSerialize().serialize(itemStack));
        }
    };

    public static final PrimitiveCodec<Material> MATERIAL = createEnumCodec(Material.class);
    public static final PrimitiveCodec<Biome> BIOME = createEnumCodec(Biome.class);
    public static final PrimitiveCodec<DyeColor> DYE_COLOR = createEnumCodec(DyeColor.class);
    public static final PrimitiveCodec<EntityType> ENTITY_TYPE = createEnumCodec(EntityType.class);
    public static final PrimitiveCodec<EntityEffect> ENTITY_EFFECT = createEnumCodec(EntityEffect.class);
    public static final PrimitiveCodec<PotionType> POTION_TYPE = createEnumCodec(PotionType.class);
    public static final PrimitiveCodec<ItemFlag> ITEM_FLAG = createEnumCodec(ItemFlag.class);
    public static final PrimitiveCodec<InventoryType> INVENTORY_TYPE = createEnumCodec(InventoryType.class);

    public static <T extends Enum<T>> PrimitiveCodec<T> createEnumCodec(final Class<T> type) {
        return new PrimitiveCodec<>() {
            @Override
            public <T1> DataResult<T> read(DynamicOps<T1> ops, T1 t1) {
                return ops.getStringValue(t1).flatMap(s -> {
                    try {
                        return DataResult.success(Enum.valueOf(type, s));
                    } catch (IllegalArgumentException e) {
                        try {
                            return DataResult.success(Enum.valueOf(type, s.toUpperCase()));
                        } catch (IllegalArgumentException e1) {
                            return DataResult.error(() -> "Unknown enum value: " + s);
                        }
                    }
                });
            }

            @Override
            public <T1> T1 write(DynamicOps<T1> ops, T t) {
                return ops.createString(t.name());
            }
        };
    }
}
