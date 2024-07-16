package org.by1337.blib.geom;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.by1337.blib.util.Direction;
import org.jetbrains.annotations.NotNull;

public class AABB {
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    public AABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = x1;
        this.minY = y1;
        this.minZ = z1;
        this.maxX = x2;
        this.maxY = y2;
        this.maxZ = z2;
        resize();
    }

    public AABB(Vec3d v, Vec3d v1) {
        resize(v.getX(), v.getY(), v.getZ(), v1.getX(), v1.getY(), v1.getZ());
    }

    public AABB(Vector v, Vector v1) {
        resize(v.getX(), v.getY(), v.getZ(), v1.getX(), v1.getY(), v1.getZ());
    }

    public void resize() {
        double minX = this.minX;
        double minY = this.minY;
        double minZ = this.minZ;
        double maxX = this.maxX;
        double maxY = this.maxY;
        double maxZ = this.maxZ;
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public void resize(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public BoundingBox toBoundingBox() {
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void expand(Direction direction, double d) {
        switch (direction) {
            case DOWN -> minY -= d;
            case UP -> maxY += d;
            case WEST -> minX -= d;
            case EAST -> maxX += d;
            case NORTH -> minZ -= d;
            case SOUTH -> maxZ += d;
        }
        resize();
    }

    public boolean isPointInsideAABB(Vec3d vec3d) {
        return vec3d.getX() >= minX &&
                vec3d.getX() <= maxX &&
                vec3d.getY() >= minY &&
                vec3d.getY() <= maxY &&
                vec3d.getZ() >= minZ &&
                vec3d.getZ() <= maxZ;
    }

    public boolean intersect(AABB aabb) {
        return minX <= aabb.maxX &&
                maxX >= aabb.minX &&
                minY <= aabb.maxY &&
                maxY >= aabb.minY &&
                minZ <= aabb.maxZ &&
                maxZ >= aabb.minZ;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public void setMinZ(double minZ) {
        this.minZ = minZ;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public void setMaxZ(double maxZ) {
        this.maxZ = maxZ;
    }

    public double getWidthX() {
        return this.maxX - this.minX;
    }

    public double getWidthZ() {
        return this.maxZ - this.minZ;
    }

    public double getHeight() {
        return this.maxY - this.minY;
    }

    public double getCenterX() {
        return this.minX + this.getWidthX() * 0.5;
    }

    public double getCenterY() {
        return this.minY + this.getHeight() * 0.5;
    }

    public double getCenterZ() {
        return this.minZ + this.getWidthZ() * 0.5;
    }

    public @NotNull Vector getCenterAsVector() {
        return new Vector(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public @NotNull Vec3d getCenter() {
        return new Vec3d(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public int hashCode() {
        int result = 1;
        long temp = Double.doubleToLongBits(this.maxX);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxY);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.maxZ);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minX);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minY);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.minZ);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AABB aabb = (AABB) o;
        return Double.compare(minX, aabb.minX) == 0 && Double.compare(minY, aabb.minY) == 0 && Double.compare(minZ, aabb.minZ) == 0 && Double.compare(maxX, aabb.maxX) == 0 && Double.compare(maxY, aabb.maxY) == 0 && Double.compare(maxZ, aabb.maxZ) == 0;
    }

    @Override
    public String toString() {
        return "AABB{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                '}';
    }
}
