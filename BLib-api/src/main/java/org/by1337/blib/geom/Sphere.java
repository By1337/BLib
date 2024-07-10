package org.by1337.blib.geom;

import com.google.errorprone.annotations.Immutable;

@Immutable
public final class Sphere {
    public final double radius;
    public final Vec3d center;

    public Sphere(double cx, double cy, double cz, double radius) {
        center = new Vec3d(cx, cy, cz);
        this.radius = radius;
    }

    public Sphere(Vec3d center, double radius) {
        this.radius = radius;
        this.center = center;
    }

    public boolean intersects(AABB aabb) {
        double closestX = clamp(center.getX(), aabb.getMinX(), aabb.getMaxX());
        double closestY = clamp(center.getY(), aabb.getMinY(), aabb.getMaxY());
        double closestZ = clamp(center.getZ(), aabb.getMinZ(), aabb.getMaxZ());

        double dx = closestX - center.getX();
        double dy = closestY - center.getY();
        double dz = closestZ - center.getZ();

        double distanceSquared = dx * dx + dy * dy + dz * dz;
        return distanceSquared <= radius * radius;
    }

    public boolean intersects(Sphere other) {
        double distanceSquared = center.distanceSquared(other.center);
        double radiusSum = this.radius + other.radius;
        return distanceSquared <= radiusSum * radiusSum;
    }

    public boolean intersects(Vec3d point) {
        double distanceSquared = center.distanceSquared(point);
        return distanceSquared <= radius * radius;
    }

    public AABB toAABB() {
        double minX = center.getX() - radius;
        double minY = center.getY() - radius;
        double minZ = center.getZ() - radius;
        double maxX = center.getX() + radius;
        double maxY = center.getY() + radius;
        double maxZ = center.getZ() + radius;
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public Sphere setRadius(double radius) {
        return new Sphere(center, radius);
    }
    public Sphere setCenter(Vec3d center) {
        return new Sphere(center, radius);
    }

    public Vec3d getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
