package org.by1337.blib.geom;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.errorprone.annotations.Immutable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class Vec3d {
    public static final Codec<Vec3d> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x").forGetter(Vec3d::getX),
            Codec.DOUBLE.fieldOf("y").forGetter(Vec3d::getY),
            Codec.DOUBLE.fieldOf("z").forGetter(Vec3d::getZ)
    ).apply(instance, Vec3d::new));

    public static final Vec3d ZERO = new Vec3d(0, 0, 0);

    public final double x;

    public final double y;

    public final double z;


    public Vec3d(Location location) {
        x = location.getX();
        y = location.getY();
        z = location.getZ();
    }

    public Vec3d(Vec3i vec3i) {
        x = vec3i.x;
        y = vec3i.y;
        z = vec3i.z;
    }

    public Vec3d(Vec3f vec3i) {
        x = vec3i.x;
        y = vec3i.y;
        z = vec3i.z;
    }

    public Vec3d(Vec3l vec3l) {
        x = vec3l.x;
        y = vec3l.y;
        z = vec3l.z;
    }

    public Vec3d(Vec3s vec3s) {
        x = vec3s.x;
        y = vec3s.y;
        z = vec3s.z;
    }

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation(@Nullable World world) {
        return new Location(world, x, y, z);
    }

    public Vec3d(Vec3d v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d(Vector v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public static Vec3d sub(Vec3d t1, Vec3d t2) {
        return new Vec3d(
                t1.x - t2.x,
                t1.y - t2.y,
                t1.z - t2.z
        );
    }

    public static Vec3d add(Vec3d t1, Vec3d t2) {
        return new Vec3d(
                t1.x + t2.x,
                t1.y + t2.y,
                t1.z + t2.z
        );
    }


    public double lengthSquared() {
        return square(x) + square(y) + square(z);
    }

    @Contract(value = " -> new", pure = true)
    public Vec3d normalize() {
        double norm = 1.0 / length();
        return new Vec3d(
                x * norm,
                y * norm,
                z * norm
        );
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static Vec3d cross(Vec3d v1, Vec3d v2) {
        return new Vec3d(
                v1.y * v2.z - v1.z * v2.y,
                v2.x * v1.z - v2.z * v1.x,
                v1.x * v2.y - v1.y * v2.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d multiply(double m) {
        return new Vec3d(
                x * m,
                y * m,
                z * m
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double y = angleCos * this.y - angleSin * this.z;
        double z = angleSin * this.y + angleCos * this.z;
        return new Vec3d(
                x, y, z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double x = angleCos * this.x + angleSin * this.z;
        double z = -angleSin * this.x + angleCos * this.z;
        return new Vec3d(
                x, y, z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d rotateAroundZ(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double x = angleCos * this.x - angleSin * this.y;
        double y = angleSin * this.x + angleCos * this.y;
        return new Vec3d(
                x, y, z
        );
    }

    public Vec3i toBlockPos() {
        return new Vec3i(NumberUtil.floor(x), NumberUtil.floor(y), NumberUtil.floor(z));
    }

    private double square(double d) {
        return d * d;
    }


    // auto generated start
    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(double scale) {
        return new Vec3d(
                x * scale,
                y * scale,
                z * scale
        );
    }

    @Contract(value = " -> new", pure = true)
    public Vec3d abs() {
        return new Vec3d(
                Math.abs(x),
                Math.abs(y),
                Math.abs(z)
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3d mul(double x1, double y1, double z1) {
        return new Vec3d(
                x * x1,
                y * y1,
                z * z1
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(double val) {
        return new Vec3d(
                x - val,
                y - val,
                z - val
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3d sub(double x1, double y1, double z1) {
        return new Vec3d(
                x - x1,
                y - y1,
                z - z1
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(double val) {
        return new Vec3d(
                x + val,
                y + val,
                z + val
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3d add(double x1, double y1, double z1) {
        return new Vec3d(
                x + x1,
                y + y1,
                z + z1
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(double scale) {
        return new Vec3d(
                x / scale,
                y / scale,
                z / scale
        );
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public Vec3d divide(double x1, double y1, double z1) {
        return new Vec3d(
                x / x1,
                y / y1,
                z / z1
        );
    }

    @Contract(pure = true)
    public double dot(double x1, double y1, double z1) {
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d setX(double x1) {
        return new Vec3d(x1, y, z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d setY(double y1) {
        return new Vec3d(x, y1, z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d setZ(double z1) {
        return new Vec3d(x, y, z1);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(Vec3d t1) {
        return new Vec3d(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(Vec3d t1) {
        return new Vec3d(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(Vec3d t1) {
        return new Vec3d(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(Vec3d t1) {
        return new Vec3d(
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
    public Vec3d cross(Vec3d t1) {
        return new Vec3d(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(Vec3f t1) {
        return new Vec3d(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(Vec3f t1) {
        return new Vec3d(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(Vec3f t1) {
        return new Vec3d(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(Vec3f t1) {
        return new Vec3d(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public double dot(Vec3f t1) {
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
    public Vec3d cross(Vec3f t1) {
        return new Vec3d(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(Vec3i t1) {
        return new Vec3d(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(Vec3i t1) {
        return new Vec3d(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(Vec3i t1) {
        return new Vec3d(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(Vec3i t1) {
        return new Vec3d(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public double dot(Vec3i t1) {
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
    public Vec3d cross(Vec3i t1) {
        return new Vec3d(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(Vec3l t1) {
        return new Vec3d(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(Vec3l t1) {
        return new Vec3d(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(Vec3l t1) {
        return new Vec3d(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(Vec3l t1) {
        return new Vec3d(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public double dot(Vec3l t1) {
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
    public Vec3d cross(Vec3l t1) {
        return new Vec3d(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(Vec3s t1) {
        return new Vec3d(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(Vec3s t1) {
        return new Vec3d(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(Vec3s t1) {
        return new Vec3d(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(Vec3s t1) {
        return new Vec3d(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    @Contract(pure = true)
    public double dot(Vec3s t1) {
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
    public Vec3d cross(Vec3s t1) {
        return new Vec3d(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(Vector t1) {
        return new Vec3d(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(Vector t1) {
        return new Vec3d(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(Vector t1) {
        return new Vec3d(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(Vector t1) {
        return new Vec3d(
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
    public Vec3d cross(Vector t1) {
        return new Vec3d(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d sub(Location t1) {
        return new Vec3d(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d mul(Location t1) {
        return new Vec3d(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d divide(Location t1) {
        return new Vec3d(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    public Vec3d add(Location t1) {
        return new Vec3d(
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
    public Vec3d cross(Location t1) {
        return new Vec3d(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }
    // auto generated end

    @Override
    public int hashCode() {
        long bits = 7L;
        bits = 31L * bits + Double.doubleToLongBits(x);
        bits = 31L * bits + Double.doubleToLongBits(y);
        bits = 31L * bits + Double.doubleToLongBits(z);
        return (int) (bits ^ (bits >> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec3d v) {
            return (x == v.x) && (y == v.y) && (z == v.z);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vec3d[" + x + ", " + y + ", " + z + "]";
    }
}
