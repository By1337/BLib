package org.by1337.blib.geom;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.errorprone.annotations.Immutable;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Objects;

@Immutable
public class Vec3f {
    public static final Codec<Vec3f> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("x").forGetter(Vec3f::getX),
            Codec.FLOAT.fieldOf("y").forGetter(Vec3f::getY),
            Codec.FLOAT.fieldOf("z").forGetter(Vec3f::getZ)
    ).apply(instance, Vec3f::new));

    public static Vec3f ZERO = new Vec3f(0, 0, 0);

    public final float x;
    public final float y;
    public final float z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    public Vec3f(Vec3i vec3i) {
        x = vec3i.getX();
        y = vec3i.getY();
        z = vec3i.getZ();
    }

    public Vec3f(Vec3d vec3i) {
        x = (float) vec3i.getX();
        y = (float) vec3i.getY();
        z = (float) vec3i.getZ();
    }

    public Vec3f(Vec3l vec3l) {
        x = (float) vec3l.getX();
        y = (float) vec3l.getY();
        z = (float) vec3l.getZ();
    }

    public Vec3f(Vec3s vec3s) {
        x = vec3s.getX();
        y = vec3s.getY();
        z = vec3s.getZ();
    }

    public Vec3f(Vector v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d toVec3d() {
        return new Vec3d(this);
    }

    public Vec3f mul(double scale) {
        return new Vec3f(
                x * scale,
                y * scale,
                z * scale
        );
    }

    public double lengthSquared() {
        return square(x) + square(y) + square(z);
    }

    public Vec3f normalize() {
        double norm = 1.0 / length();
        return new Vec3f(
                x * norm,
                y * norm,
                z * norm
        );
    }

    public Vec3f multiply(double m) {
        return new Vec3f(
                x * m,
                y * m,
                z * m
        );
    }

    public Vec3i toBlockPos() {
        return new Vec3i(NumberUtil.floor(x), NumberUtil.floor(y), NumberUtil.floor(z));
    }

    private double square(double d) {
        return d * d;
    }

    // auto generated start
    public Vec3f mul(float scale) {
        return new Vec3f(
                x * scale,
                y * scale,
                z * scale
        );
    }

    public Vec3f mul(float x1, float y1, float z1) {
        return new Vec3f(
                x * x1,
                y * y1,
                z * z1
        );
    }

    public Vec3f sub(float val) {
        return new Vec3f(
                x - val,
                y - val,
                z - val
        );
    }

    public Vec3f sub(float x1, float y1, float z1) {
        return new Vec3f(
                x - x1,
                y - y1,
                z - z1
        );
    }

    public Vec3f add(float val) {
        return new Vec3f(
                x + val,
                y + val,
                z + val
        );
    }

    public Vec3f add(float x1, float y1, float z1) {
        return new Vec3f(
                x + x1,
                y + y1,
                z + z1
        );
    }

    public Vec3f divide(float scale) {
        return new Vec3f(
                x / scale,
                y / scale,
                z / scale
        );
    }

    public Vec3f divide(float x1, float y1, float z1) {
        return new Vec3f(
                x / x1,
                y / y1,
                z / z1
        );
    }

    public float dot(float x1, float y1, float z1) {
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vec3f setX(float x1) {
        return new Vec3f(x1, y, z);
    }

    public Vec3f setY(float y1) {
        return new Vec3f(x, y1, z);
    }

    public Vec3f setZ(float z1) {
        return new Vec3f(x, y, z1);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vec3f sub(Vec3d t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f mul(Vec3d t1) {
        return new Vec3f(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3f divide(Vec3d t1) {
        return new Vec3f(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3f add(Vec3d t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public float dot(Vec3d t1) {
        return (float) (x * t1.x + y * t1.y + z * t1.z);
    }

    public double distance(Vec3d t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3d t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3f cross(Vec3d t1) {
        return new Vec3f(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3f sub(Vec3f t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f mul(Vec3f t1) {
        return new Vec3f(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3f divide(Vec3f t1) {
        return new Vec3f(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3f add(Vec3f t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public float dot(Vec3f t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    public double distance(Vec3f t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3f t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3f cross(Vec3f t1) {
        return new Vec3f(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3f sub(Vec3i t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f mul(Vec3i t1) {
        return new Vec3f(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3f divide(Vec3i t1) {
        return new Vec3f(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3f add(Vec3i t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public float dot(Vec3i t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    public double distance(Vec3i t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3i t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3f cross(Vec3i t1) {
        return new Vec3f(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3f sub(Vec3l t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f mul(Vec3l t1) {
        return new Vec3f(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3f divide(Vec3l t1) {
        return new Vec3f(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3f add(Vec3l t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public float dot(Vec3l t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    public double distance(Vec3l t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3l t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3f cross(Vec3l t1) {
        return new Vec3f(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3f sub(Vec3s t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f mul(Vec3s t1) {
        return new Vec3f(
                x * t1.x,
                y * t1.y,
                z * t1.z
        );
    }

    public Vec3f divide(Vec3s t1) {
        return new Vec3f(
                x / t1.x,
                y / t1.y,
                z / t1.z
        );
    }

    public Vec3f add(Vec3s t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public float dot(Vec3s t1) {
        return x * t1.x + y * t1.y + z * t1.z;
    }

    public double distance(Vec3s t1) {
        return Math.sqrt(square(x - t1.x) + square(y - t1.y) + square(z - t1.z));
    }

    public double distanceSquared(Vec3s t1) {
        return square(x - t1.x) + square(y - t1.y) + square(z - t1.z);
    }

    public Vec3f cross(Vec3s t1) {
        return new Vec3f(
                y * t1.z - z * t1.y,
                t1.x * z - t1.z * x,
                x * t1.y - y * t1.x
        );
    }

    public Vec3f sub(Vector t1) {
        return new Vec3f(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    public Vec3f mul(Vector t1) {
        return new Vec3f(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    public Vec3f divide(Vector t1) {
        return new Vec3f(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    public Vec3f add(Vector t1) {
        return new Vec3f(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }

    public float dot(Vector t1) {
        return (float) (x * t1.getX() + y * t1.getY() + z * t1.getZ());
    }

    public double distance(Vector t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }

    public double distanceSquared(Vector t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }

    public Vec3f cross(Vector t1) {
        return new Vec3f(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }

    public Vec3f sub(Location t1) {
        return new Vec3f(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    public Vec3f mul(Location t1) {
        return new Vec3f(
                x * t1.getX(),
                y * t1.getY(),
                z * t1.getZ()
        );
    }

    public Vec3f divide(Location t1) {
        return new Vec3f(
                x / t1.getX(),
                y / t1.getY(),
                z / t1.getZ()
        );
    }

    public Vec3f add(Location t1) {
        return new Vec3f(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }

    public float dot(Location t1) {
        return (float) (x * t1.getX() + y * t1.getY() + z * t1.getZ());
    }

    public double distance(Location t1) {
        return Math.sqrt(square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ()));
    }

    public double distanceSquared(Location t1) {
        return square(x - t1.getX()) + square(y - t1.getY()) + square(z - t1.getZ());
    }

    public Vec3f cross(Location t1) {
        return new Vec3f(
                y * t1.getZ() - z * t1.getY(),
                t1.getX() * z - t1.getZ() * x,
                x * t1.getY() - y * t1.getX()
        );
    }
    // auto generated end


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3f vec3f = (Vec3f) o;
        return Float.compare(x, vec3f.x) == 0 && Float.compare(y, vec3f.y) == 0 && Float.compare(z, vec3f.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vec3f[" + x + ", " + y + ", " + z + "]";
    }
}
