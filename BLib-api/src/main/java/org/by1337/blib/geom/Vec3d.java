package org.by1337.blib.geom;

import com.google.common.primitives.Doubles;
import com.google.errorprone.annotations.Immutable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
@Immutable
public final class Vec3d {

    public final double x;

    public final double y;

    public final double z;


    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(Vec3d v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d(Vector v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d mul(double scale) {
        return new Vec3d(
                x * scale,
                y * scale,
                z * scale
        );
    }

    public Vec3d sub(Vec3d t1, Vec3d t2) {
        return new Vec3d(
                t1.x - t2.x,
                t1.y - t2.y,
                t1.z - t2.z
        );
    }

    public Vec3d sub(Vec3d t1) {
        return new Vec3d(
                x - t1.x,
                y - t1.y,
                z - t1.z
        );
    }

    public Vec3d add(Vec3d t1, Vec3d t2) {
        return new Vec3d(
                t1.x + t2.x,
                t1.y + t2.y,
                t1.z + t2.z
        );
    }

    public Vec3d add(Vec3d t1) {
        return new Vec3d(
                x + t1.x,
                y + t1.y,
                z + t1.z
        );
    }

    public Vec3d add(double x, double y, double z) {
        return new Vec3d(
                this.x + x,
                this.y + y,
                this.z + z
        );
    }

    public Vec3d sub(double x, double y, double z) {
        return new Vec3d(
                this.x - x,
                this.y - y,
                this.z - z
        );
    }

    public Vec3d divide(double x, double y, double z) {
        return new Vec3d(
                this.x / x,
                this.y / y,
                this.z / z
        );
    }

    public Vec3d divide(Vec3d vec3d) {
        return new Vec3d(
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

    public Vec3d normalize() {
        double norm = 1.0 / length();
        return new Vec3d(
                x * norm,
                y * norm,
                z * norm
        );
    }

    public Vec3d cross(Vec3d v1, Vec3d v2) {
        return new Vec3d(
                v1.y * v2.z - v1.z * v2.y,
                v2.x * v1.z - v2.z * v1.x,
                v1.x * v2.y - v1.y * v2.x
        );
    }

    public Vec3d multiply(double m) {
        return new Vec3d(
                x * m,
                y * m,
                z * m
        );
    }

    public Vec3d rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double y = angleCos * this.y - angleSin * this.z;
        double z = angleSin * this.y + angleCos * this.z;
        return new Vec3d(
                x, y, z
        );
    }

    public Vec3d rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double x = angleCos * this.x + angleSin * this.z;
        double z = -angleSin * this.x + angleCos * this.z;
        return new Vec3d(
                x, y, z
        );
    }

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

    public double dot(Vec3d v1) {
        return this.x * v1.x + this.y * v1.y + this.z * v1.z;
    }

    public double distance(Vec3d o) {
        return Math.sqrt(square(x - o.x) + square(y - o.y) + square(z - o.z));
    }

    public double distanceSquared(Vec3d o) {
        return square(x - o.x) + square(y - o.y) + square(z - o.z);
    }

    private double square(double d) {
        return d * d;
    }

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
