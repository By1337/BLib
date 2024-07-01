package org.by1337.blib.geom;

import org.bukkit.util.Vector;

public class Vec3i {

    private int x;

    private int y;

    private int z;

    public Vec3i() {
    }

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d toVec3d(){
        return new Vec3d(x, y, z);
    }
    public Vec3i(Vec3i v) {
        set(v);
    }

    public Vec3i(Vector v) {
        set((int) v.getX(), (int) v.getY(), (int) v.getZ());
    }

    public void set(Vec3i v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void mul(int scale) {
        x *= scale;
        y *= scale;
        z *= scale;
    }

    public void sub(Vec3i t1, Vec3i t2) {
        this.x = t1.x - t2.x;
        this.y = t1.y - t2.y;
        this.z = t1.z - t2.z;
    }

    public void sub(Vec3i t1) {
        this.x -= t1.x;
        this.y -= t1.y;
        this.z -= t1.z;
    }

    public void add(Vec3i t1, Vec3i t2) {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public void add(Vec3i t1) {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }


    public void cross(Vec3i v1, Vec3i v2) {
        int tmpX;
        int tmpY;

        tmpX = v1.y * v2.z - v1.z * v2.y;
        tmpY = v2.x * v1.z - v2.z * v1.x;
        this.z = v1.x * v2.y - v1.y * v2.x;
        this.x = tmpX;
        this.y = tmpY;
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
