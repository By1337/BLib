package org.by1337.blib.geom;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.errorprone.annotations.Immutable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class Vec3i {
    public static final Codec<Vec3i> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Vec3i::getX),
            Codec.INT.fieldOf("y").forGetter(Vec3i::getY),
            Codec.INT.fieldOf("z").forGetter(Vec3i::getZ)
    ).apply(instance, Vec3i::new));

    public static final Vec3i ZERO = new Vec3i(0, 0, 0);
    public final int x;

    public final int y;

    public final int z;


    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(Block block) {
        this(block.getLocation());
    }

    public Vec3i(Location location) {
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
    }

    public Location toLocation(@Nullable World world) {
        return new Location(world, x, y, z);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public Block toBlock(@NotNull World world) {
        return world.getBlockAt(x, y, z);
    }

    public Vec3i(Vector v) {
        this((int) v.getX(), (int) v.getY(), (int) v.getZ());
    }

    public Vec3d toVec3d() {
        return new Vec3d(x, y, z);
    }

    public Vec3d blockCenter(int scale) {
        return new Vec3d(this).add(0.5, 0.5, 0.5);
    }

    public Vec3i mul(int scale) {
        return new Vec3i(
                x * scale,
                y * scale,
                z * scale
        );
    }

    public Vec3i sub(Vec3i t1, Vec3i t2) {
        return new Vec3i(
                t1.x - t2.x,
                t1.y - t2.y,
                t1.z - t2.z
        );
    }

    public Vec3i sub(Vec3i t1) {
        return new Vec3i(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3i sub(int x, int y, int z) {
        return new Vec3i(
                this.x - x,
                this.y - y,
                this.z - z
        );
    }

    public Vec3i add(Vec3i t1, Vec3i t2) {
        return new Vec3i(
                t1.x + t2.x,
                t1.y + t2.y,
                t1.z + t2.z
        );
    }

    public Vec3i add(int x, int y, int z) {
        return new Vec3i(
                this.x + x,
                this.y + y,
                this.z + z
        );
    }

    public Vec3i add(Vec3i t1) {
        return new Vec3i(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }


    public double dot(Vec3i v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        long bits = 7L;
        bits = 31L * bits + x;
        bits = 31L * bits + y;
        bits = 31L * bits + z;
        return (int) (bits ^ (bits >> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec3i v) {
            return (x == v.x) && (y == v.y) && (z == v.z);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vec3i[" + x + ", " + y + ", " + z + "]";
    }
}
