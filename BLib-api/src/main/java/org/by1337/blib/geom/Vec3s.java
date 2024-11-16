package org.by1337.blib.geom;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.errorprone.annotations.Immutable;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Objects;

@Immutable
public class Vec3s {
    public static final Codec<Vec3s> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.SHORT.fieldOf("x").forGetter(Vec3s::getX),
            Codec.SHORT.fieldOf("y").forGetter(Vec3s::getY),
            Codec.SHORT.fieldOf("z").forGetter(Vec3s::getZ)
    ).apply(instance, Vec3s::new));

    public static Vec3s ZERO = new Vec3s((short) 0, (short) 0, (short) 0);
    public final short x;
    public final short y;
    public final short z;

    public Vec3s(short x, short y, short z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3s(Vec3f vec3i) {
        x = (short) vec3i.x;
        y = (short) vec3i.y;
        z = (short) vec3i.z;
    }

    public Vec3s(int x, int y, int z) {
        this.x = (short) x;
        this.y = (short) y;
        this.z = (short) z;
    }

    public Vec3s(double x, double y, double z) {
        this.x = (short) x;
        this.y = (short) y;
        this.z = (short) z;
    }

    public Vec3s(Vec3i vec3i) {
        x = (short) vec3i.x;
        y = (short) vec3i.y;
        z = (short) vec3i.z;
    }

    public Vec3s(Vec3l vec3i) {
        x = (short) vec3i.x;
        y = (short) vec3i.y;
        z = (short) vec3i.z;
    }

    public Vec3s(Vec3d vec3d) {
        x = (short) vec3d.x;
        y = (short) vec3d.y;
        z = (short) vec3d.z;
    }

    // auto generated start
    public Vec3s abs() {
        return new Vec3s(
                Math.abs(x),
                Math.abs(y),
                Math.abs(z)
        );
    }

    public Vec3s mul(short scale) {
        return new Vec3s(
                x * scale,
                y * scale,
                z * scale
        );
    }

    public Vec3s mul(short x1, short y1, short z1) {
        return new Vec3s(
                x * x1,
                y * y1,
                z * z1
        );
    }

    public Vec3s sub(short val) {
        return new Vec3s(
                x - val,
                y - val,
                z - val
        );
    }

    public Vec3s sub(short x1, short y1, short z1) {
        return new Vec3s(
                x - x1,
                y - y1,
                z - z1
        );
    }

    public Vec3s add(short val) {
        return new Vec3s(
                x + val,
                y + val,
                z + val
        );
    }

    public Vec3s add(short x1, short y1, short z1) {
        return new Vec3s(
                x + x1,
                y + y1,
                z + z1
        );
    }

    public Vec3s divide(short scale) {
        return new Vec3s(
                x / scale,
                y / scale,
                z / scale
        );
    }

    public Vec3s divide(short x1, short y1, short z1) {
        return new Vec3s(
                x / x1,
                y / y1,
                z / z1
        );
    }

    public short dot(short x1, short y1, short z1) {
        return (short) (x * x1 + y * y1 + z * z1);
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

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getZ() {
        return z;
    }

    public Vec3s setX(short x1) {
        return new Vec3s(x1, y, z);
    }

    public Vec3s setY(short y1) {
        return new Vec3s(x, y1, z);
    }

    public Vec3s setZ(short z1) {
        return new Vec3s(x, y, z1);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vec3s sub(Vec3d t1) {
        return new Vec3s(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3s mul(Vec3d t1) {
        return new Vec3s(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3s divide(Vec3d t1) {
        return new Vec3s(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3s add(Vec3d t1) {
        return new Vec3s(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public short dot(Vec3d t1) {
        return (short) (x * t1.x + y * t1.y + z * t1.z);
    }

    public double distance(Vec3d t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3d t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3s cross(Vec3d t1) {
        return new Vec3s(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3s sub(Vec3f t1) {
        return new Vec3s(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3s mul(Vec3f t1) {
        return new Vec3s(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3s divide(Vec3f t1) {
        return new Vec3s(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3s add(Vec3f t1) {
        return new Vec3s(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public short dot(Vec3f t1) {
        return (short) (x * t1.x + y * t1.y + z * t1.z);
    }

    public double distance(Vec3f t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3f t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3s cross(Vec3f t1) {
        return new Vec3s(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3s sub(Vec3i t1) {
        return new Vec3s(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3s mul(Vec3i t1) {
        return new Vec3s(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3s divide(Vec3i t1) {
        return new Vec3s(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3s add(Vec3i t1) {
        return new Vec3s(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public short dot(Vec3i t1) {
        return (short) (x * t1.x + y * t1.y + z * t1.z);
    }

    public double distance(Vec3i t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3i t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3s cross(Vec3i t1) {
        return new Vec3s(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3s sub(Vec3l t1) {
        return new Vec3s(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3s mul(Vec3l t1) {
        return new Vec3s(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3s divide(Vec3l t1) {
        return new Vec3s(
                (double) x / t1.x,
                (double) y / t1.y,
                (double) z / t1.z
        );
    }

    public Vec3s add(Vec3l t1) {
        return new Vec3s(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public short dot(Vec3l t1) {
        return (short) (x * t1.x + y * t1.y + z * t1.z);
    }

    public double distance(Vec3l t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3l t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3s cross(Vec3l t1) {
        return new Vec3s(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3s sub(Vec3s t1) {
        return new Vec3s(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3s mul(Vec3s t1) {
        return new Vec3s(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3s divide(Vec3s t1) {
        return new Vec3s(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3s add(Vec3s t1) {
        return new Vec3s(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public short dot(Vec3s t1) {
        return (short) (x * t1.x + y * t1.y + z * t1.z);
    }

    public double distance(Vec3s t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3s t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3s cross(Vec3s t1) {
        return new Vec3s(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3s sub(Vector t1) {
        return new Vec3s(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    public Vec3s mul(Vector t1) {
        return new Vec3s(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    public Vec3s divide(Vector t1) {
        return new Vec3s(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    public Vec3s add(Vector t1) {
        return new Vec3s(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }

    public short dot(Vector t1) {
        return (short) (x * t1.getX() + y * t1.getY() + z * t1.getZ());
    }

    public double distance(Vector t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }

    public double distanceSquared(Vector t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }

    public Vec3s cross(Vector t1) {
        return new Vec3s(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }

    public Vec3s sub(Location t1) {
        return new Vec3s(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    public Vec3s mul(Location t1) {
        return new Vec3s(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    public Vec3s divide(Location t1) {
        return new Vec3s(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    public Vec3s add(Location t1) {
        return new Vec3s(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }

    public short dot(Location t1) {
        return (short) (x * t1.getX() + y * t1.getY() + z * t1.getZ());
    }

    public double distance(Location t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }

    public double distanceSquared(Location t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }

    public Vec3s cross(Location t1) {
        return new Vec3s(
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
        Vec3s vec3s = (Vec3s) o;
        return x == vec3s.x && y == vec3s.y && z == vec3s.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vec3s[" + x + ", " + y + ", " + z + "]";
    }
}
