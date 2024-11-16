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
    public Vec3i(Vec3d vec3i) {
        x = (int) vec3i.x;
        y = (int) vec3i.y;
        z = (int) vec3i.z;
    }

    public Vec3i(Vec3f vec3i) {
        x = (int) vec3i.x;
        y = (int) vec3i.y;
        z = (int) vec3i.z;
    }
    public Vec3i(Vec3l vec3l) {
        x = (int) vec3l.x;
        y = (int) vec3l.y;
        z = (int) vec3l.z;
    }
    public Vec3i(Vec3s vec3s) {
        x = vec3s.x;
        y = vec3s.y;
        z = vec3s.z;
    }

    public Vec3i(double x, double y, double z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
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

    // auto generated start
    public Vec3i mul(int scale) {
        return new Vec3i(
                x * scale,
                y * scale,
                z * scale
        );
    }
    public Vec3i mul(int x1, int y1, int z1) {
        return new Vec3i(
                x * x1,
                y * y1,
                z * z1
        );
    }
    public Vec3i sub(int val) {
        return new Vec3i(
                x - val,
                y - val,
                z - val
        );
    }
    public Vec3i sub(int x1, int y1, int z1) {
        return new Vec3i(
                x - x1,
                y - y1,
                z - z1
        );
    }
    public Vec3i add(int val) {
        return new Vec3i(
                x + val,
                y + val,
                z + val
        );
    }
    public Vec3i add(int x1, int y1, int z1) {
        return new Vec3i(
                x + x1,
                y + y1,
                z + z1
        );
    }
    public Vec3i divide(int scale) {
        return new Vec3i(
                x / scale,
                y / scale,
                z / scale
        );
    }
    public Vec3i divide(int x1, int y1, int z1) {
        return new Vec3i(
                x / x1,
                y / y1,
                z / z1
        );
    }
    public int dot(int x1, int y1, int z1) {
        return x * x1 + y * y1 + z * z1;
    }
    public double distance(double x1, double y1, double z1) {
        return Math.sqrt(square(x - x1) + square(y - y1) + square(z - z1));
    }
    public double distanceSquared(double x1, double y1, double z1) {
        return square(x - x1) + square(y - y1) + square(z - z1);
    }
    public Vector toVector() {
        return new Vector(x, y, z);
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
    public Vec3i setX(int x1) {
        return new Vec3i(x1, y, z);
    }
    public Vec3i setY(int y1) {
        return new Vec3i(x, y1, z);
    }
    public Vec3i setZ(int z1) {
        return new Vec3i(x, y, z1);
    }
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vec3i sub(Vec3d t1) {
        return new Vec3i(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }
    public Vec3i mul(Vec3d t1) {
        return new Vec3i(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }
    public Vec3i divide(Vec3d t1) {
        return new Vec3i(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }
    public Vec3i add(Vec3d t1) {
        return new Vec3i(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }
    public int dot(Vec3d t1) {
        return (int) (x * t1.x + y * t1.y + z * t1.z);
    }
    public double distance(Vec3d t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }
    public double distanceSquared(Vec3d t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }
    public Vec3i cross(Vec3d t1) {
        return new Vec3i(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }
    public Vec3i sub(Vec3f t1) {
        return new Vec3i(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }
    public Vec3i mul(Vec3f t1) {
        return new Vec3i(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }
    public Vec3i divide(Vec3f t1) {
        return new Vec3i(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }
    public Vec3i add(Vec3f t1) {
        return new Vec3i(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }
    public int dot(Vec3f t1) {
        return (int) (x * t1.x + y * t1.y + z * t1.z);
    }
    public double distance(Vec3f t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }
    public double distanceSquared(Vec3f t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }
    public Vec3i cross(Vec3f t1) {
        return new Vec3i(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }
    public Vec3i sub(Vec3i t1) {
        return new Vec3i(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }
    public Vec3i mul(Vec3i t1) {
        return new Vec3i(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }
    public Vec3i divide(Vec3i t1) {
        return new Vec3i(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }
    public Vec3i add(Vec3i t1) {
        return new Vec3i(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }
    public int dot(Vec3i t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }
    public double distance(Vec3i t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }
    public double distanceSquared(Vec3i t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }
    public Vec3i cross(Vec3i t1) {
        return new Vec3i(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }
    public Vec3i sub(Vec3l t1) {
        return new Vec3i(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }
    public Vec3i mul(Vec3l t1) {
        return new Vec3i(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }
    public Vec3i divide(Vec3l t1) {
        return new Vec3i(
                (double) x / t1.x,
                (double) y / t1.y,
                (double) z / t1.z
        );
    }
    public Vec3i add(Vec3l t1) {
        return new Vec3i(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }
    public int dot(Vec3l t1) {
        return (int) (x * t1.x + y * t1.y + z * t1.z);
    }
    public double distance(Vec3l t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }
    public double distanceSquared(Vec3l t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }
    public Vec3i cross(Vec3l t1) {
        return new Vec3i(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }
    public Vec3i sub(Vec3s t1) {
        return new Vec3i(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }
    public Vec3i mul(Vec3s t1) {
        return new Vec3i(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }
    public Vec3i divide(Vec3s t1) {
        return new Vec3i(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }
    public Vec3i add(Vec3s t1) {
        return new Vec3i(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }
    public int dot(Vec3s t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }
    public double distance(Vec3s t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }
    public double distanceSquared(Vec3s t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }
    public Vec3i cross(Vec3s t1) {
        return new Vec3i(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }
    public Vec3i sub(Vector t1) {
        return new Vec3i(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }
    public Vec3i mul(Vector t1) {
        return new Vec3i(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }
    public Vec3i divide(Vector t1) {
        return new Vec3i(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }
    public Vec3i add(Vector t1) {
        return new Vec3i(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }
    public int dot(Vector t1) {
        return (int) (x * t1.getX() + y * t1.getY() + z * t1.getZ());
    }
    public double distance(Vector t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }
    public double distanceSquared(Vector t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }
    public Vec3i cross(Vector t1) {
        return new Vec3i(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }
    public Vec3i sub(Location t1) {
        return new Vec3i(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }
    public Vec3i mul(Location t1) {
        return new Vec3i(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }
    public Vec3i divide(Location t1) {
        return new Vec3i(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }
    public Vec3i add(Location t1) {
        return new Vec3i(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }
    public int dot(Location t1) {
        return (int) (x * t1.getX() + y * t1.getY() + z * t1.getZ());
    }
    public double distance(Location t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }
    public double distanceSquared(Location t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }
    public Vec3i cross(Location t1) {
        return new Vec3i(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }
    // auto generated end
    private double square(double d) {
        return d * d;
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
