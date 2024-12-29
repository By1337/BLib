package org.by1337.blib.geom;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.errorprone.annotations.Immutable;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;

import java.util.Objects;


@Immutable
public class Vec3l {
    public static final Codec<Vec3l> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("x").forGetter(Vec3l::getX),
            Codec.LONG.fieldOf("y").forGetter(Vec3l::getY),
            Codec.LONG.fieldOf("z").forGetter(Vec3l::getZ)
    ).apply(instance, Vec3l::new));

    public static Vec3l ZERO = new Vec3l(0, 0, 0);

    public final long x;
    public final long y;
    public final long z;

    public Vec3l(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3l(Vec3i vec3i) {
        x = vec3i.x;
        y = vec3i.y;
        z = vec3i.z;
    }

    public Vec3l(Vec3d vec3i) {
        x = (long) vec3i.x;
        y = (long) vec3i.y;
        z = (long) vec3i.z;
    }

    public Vec3l(Vec3f vec3i) {
        x = (long) vec3i.x;
        y = (long) vec3i.y;
        z = (long) vec3i.z;
    }

    public Vec3l(Vec3s vec3s) {
        x = vec3s.x;
        y = vec3s.y;
        z = vec3s.z;
    }

    public Vec3l(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3l(double x, double y, double z) {
        this.x = (long) x;
        this.y = (long) y;
        this.z = (long) z;
    }

    // auto generated start
    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(long scale) {
        return new Vec3l(
                x * scale,
                y * scale,
                z * scale
        );
    }

    @Contract(value = " -> new", pure = true)
    public Vec3l abs() {
        return new Vec3l(
                Math.abs(x),
                Math.abs(y),
                Math.abs(z)
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3l mul(long x1, long y1, long z1) {
        return new Vec3l(
                x * x1,
                y * y1,
                z * z1
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(long val) {
        return new Vec3l(
                x - val,
                y - val,
                z - val
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3l sub(long x1, long y1, long z1) {
        return new Vec3l(
                x - x1,
                y - y1,
                z - z1
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(long val) {
        return new Vec3l(
                x + val,
                y + val,
                z + val
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3l add(long x1, long y1, long z1) {
        return new Vec3l(
                x + x1,
                y + y1,
                z + z1
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(long scale) {
        return new Vec3l(
                x / scale,
                y / scale,
                z / scale
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3l divide(long x1, long y1, long z1) {
        return new Vec3l(
                x / x1,
                y / y1,
                z / z1
        );
    }

    @Contract(pure = true)
    public long dot(long x1, long y1, long z1) {
        return x * x1 + y * y1 + z * z1;
    }

    @Contract(pure = true)
    public double distance(double x1, double y1, double z1) {
        return Math.sqrt(square(x - x1) + square(y - y1) + square(z - z1));
    }

    @Contract(pure = true)
    public double distanceSquared(double x1, double y1, double z1) {
        return square(x - x1) + square(y - y1) + square(z - z1);
    }

    @Contract(pure = true)
    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l setX(long x1) {
        return new Vec3l(x1, y, z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l setY(long y1) {
        return new Vec3l(x, y1, z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l setZ(long z1) {
        return new Vec3l(x, y, z1);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(Vec3d t1) {
        return new Vec3l(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(Vec3d t1) {
        return new Vec3l(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(Vec3d t1) {
        return new Vec3l(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(Vec3d t1) {
        return new Vec3l(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public double dot(Vec3d t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    @Contract(pure = true)
    public double distance(Vec3d t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    @Contract(pure = true)
    public double distanceSquared(Vec3d t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l cross(Vec3d t1) {
        return new Vec3l(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(Vec3f t1) {
        return new Vec3l(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(Vec3f t1) {
        return new Vec3l(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(Vec3f t1) {
        return new Vec3l(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(Vec3f t1) {
        return new Vec3l(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public float dot(Vec3f t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    @Contract(pure = true)
    public double distance(Vec3f t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    @Contract(pure = true)
    public double distanceSquared(Vec3f t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l cross(Vec3f t1) {
        return new Vec3l(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(Vec3i t1) {
        return new Vec3l(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(Vec3i t1) {
        return new Vec3l(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(Vec3i t1) {
        return new Vec3l(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(Vec3i t1) {
        return new Vec3l(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public long dot(Vec3i t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    @Contract(pure = true)
    public double distance(Vec3i t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    @Contract(pure = true)
    public double distanceSquared(Vec3i t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l cross(Vec3i t1) {
        return new Vec3l(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(Vec3l t1) {
        return new Vec3l(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(Vec3l t1) {
        return new Vec3l(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(Vec3l t1) {
        return new Vec3l(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(Vec3l t1) {
        return new Vec3l(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public long dot(Vec3l t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    @Contract(pure = true)
    public double distance(Vec3l t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    @Contract(pure = true)
    public double distanceSquared(Vec3l t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l cross(Vec3l t1) {
        return new Vec3l(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(Vec3s t1) {
        return new Vec3l(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(Vec3s t1) {
        return new Vec3l(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(Vec3s t1) {
        return new Vec3l(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(Vec3s t1) {
        return new Vec3l(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public long dot(Vec3s t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    @Contract(pure = true)
    public double distance(Vec3s t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    @Contract(pure = true)
    public double distanceSquared(Vec3s t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l cross(Vec3s t1) {
        return new Vec3l(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(Vector t1) {
        return new Vec3l(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(Vector t1) {
        return new Vec3l(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(Vector t1) {
        return new Vec3l(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(Vector t1) {
        return new Vec3l(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }

    @Contract(pure = true)
    public double dot(Vector t1) {
        return x * t1.getX() + y * t1.getY() + z * t1.getZ();
    }

    @Contract(pure = true)
    public double distance(Vector t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }

    @Contract(pure = true)
    public double distanceSquared(Vector t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l cross(Vector t1) {
        return new Vec3l(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l sub(Location t1) {
        return new Vec3l(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l mul(Location t1) {
        return new Vec3l(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l divide(Location t1) {
        return new Vec3l(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l add(Location t1) {
        return new Vec3l(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }

    @Contract(pure = true)
    public double dot(Location t1) {
        return x * t1.getX() + y * t1.getY() + z * t1.getZ();
    }

    @Contract(pure = true)
    public double distance(Location t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }

    @Contract(pure = true)
    public double distanceSquared(Location t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3l cross(Location t1) {
        return new Vec3l(
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3l vec3l = (Vec3l) o;
        return x == vec3l.x && y == vec3l.y && z == vec3l.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vec3l[" + x + ", " + y + ", " + z + "]";
    }
}
