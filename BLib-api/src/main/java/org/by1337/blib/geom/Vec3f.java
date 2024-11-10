package org.by1337.blib.geom;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.errorprone.annotations.Immutable;
import org.bukkit.util.Vector;

@Immutable
public class Vec3f {
    public static final Codec<Vec3f> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("x").forGetter(Vec3f::getX),
            Codec.FLOAT.fieldOf("y").forGetter(Vec3f::getY),
            Codec.FLOAT.fieldOf("z").forGetter(Vec3f::getZ)
    ).apply(instance, Vec3f::new));

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

    public Vec3f sub(Vec3f t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f sub(Vec3i t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f sub(Vec3d t1) {
        return new Vec3f(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3f sub(Vector t1) {
        return new Vec3f(
                x - t1.getX(),
                y - t1.getY(),
                z - t1.getZ()
        );
    }

    public Vec3f add(Vec3f t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public Vec3f add(Vector t1) {
        return new Vec3f(
                x + t1.getX(),
                y + t1.getY(),
                z + t1.getZ()
        );
    }

    public Vec3f add(Vec3i t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public Vec3f add(Vec3d t1) {
        return new Vec3f(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public Vec3f add(float x, float y, float z) {
        return new Vec3f(
                this.x + x,
                this.y + y,
                this.z + z
        );
    }

    public Vec3f sub(float x, float y, float z) {
        return new Vec3f(
                this.x - x,
                this.y - y,
                this.z - z
        );
    }

    public Vec3f divide(float x, float y, float z) {
        return new Vec3f(
                this.x / x,
                this.y / y,
                this.z / z
        );
    }

    public Vec3f divide(Vec3f vec3d) {
        return new Vec3f(
                this.x / vec3d.x,
                this.y / vec3d.y,
                this.z / vec3d.z
        );
    }

    public Vec3f divide(Vec3i vec3d) {
        return new Vec3f(
                this.x / vec3d.x,
                this.y / vec3d.y,
                this.z / vec3d.z
        );
    }

    public Vec3f divide(Vec3d vec3d) {
        return new Vec3f(
                this.x / vec3d.x,
                this.y / vec3d.y,
                this.z / vec3d.z
        );
    }


    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
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

    public double dot(Vec3f v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    public double dot(Vec3i v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    public double dot(Vec3d v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    public double distance(Vec3d o) {
        return Math.sqrt(square(x - o.x) + square(y - o.y) + square(z - o.z));
    }

    public double distance(Vec3f o) {
        return Math.sqrt(square(x - o.x) + square(y - o.y) + square(z - o.z));
    }

    public double distance(Vec3i o) {
        return Math.sqrt(square(x - o.x) + square(y - o.y) + square(z - o.z));
    }

    public double distanceSquared(Vec3d o) {
        return square(x - o.x) + square(y - o.y) + square(z - o.z);
    }

    public double distanceSquared(Vec3f o) {
        return square(x - o.x) + square(y - o.y) + square(z - o.z);
    }

    public double distanceSquared(Vec3i o) {
        return square(x - o.x) + square(y - o.y) + square(z - o.z);
    }

    private double square(double d) {
        return d * d;
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
}
