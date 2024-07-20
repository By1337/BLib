package org.by1337.blib.geom;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.by1337.blib.util.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class IntAABB {
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    public IntAABB(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.minX = x1;
        this.minY = y1;
        this.minZ = z1;
        this.maxX = x2;
        this.maxY = y2;
        this.maxZ = z2;
        resize();
    }

    public static IntAABB fromAABB(AABB aabb){
        return new IntAABB(
                floor(aabb.getMinX()),
                floor(aabb.getMinY()),
                floor(aabb.getMinZ()),
                floor(aabb.getMaxX()),
                floor(aabb.getMaxY()),
                floor(aabb.getMaxZ())
        );
    }

    private static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }
    public int getBlockCount(){
        return (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
    }
    public IntAABB(Vec3i v, Vec3i v1) {
        resize(v.getX(), v.getY(), v.getZ(), v1.getX(), v1.getY(), v1.getZ());
    }

    public List<Vec3i> getAllPointsInAABB() {
        List<Vec3i> list = new ArrayList<>((maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1));
        forEachPointInAABB(list::add);
        return Collections.unmodifiableList(list);
    }

    private void forEachPointInAABB(Consumer<Vec3i> consumer) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    consumer.accept(new Vec3i(x, y, z));
                }
            }
        }
    }

    public void resize() {
        int minX = this.minX;
        int minY = this.minY;
        int minZ = this.minZ;
        int maxX = this.maxX;
        int maxY = this.maxY;
        int maxZ = this.maxZ;
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public void resize(int x1, int y1, int z1, int x2, int y2, int z2) {
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

    public AABB toAABB() {
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void expand(Direction direction, int d) {
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

    public boolean isPointInsideAABB(Vec3i vec3i) {
        return vec3i.getX() >= minX &&
                vec3i.getX() <= maxX &&
                vec3i.getY() >= minY &&
                vec3i.getY() <= maxY &&
                vec3i.getZ() >= minZ &&
                vec3i.getZ() <= maxZ;
    }

    public boolean intersect(IntAABB IntAABB) {
        return minX <= IntAABB.maxX &&
                maxX >= IntAABB.minX &&
                minY <= IntAABB.maxY &&
                maxY >= IntAABB.minY &&
                minZ <= IntAABB.maxZ &&
                maxZ >= IntAABB.minZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }

    public int getWidthX() {
        return this.maxX - this.minX;
    }

    public int getWidthZ() {
        return this.maxZ - this.minZ;
    }

    public int getHeight() {
        return this.maxY - this.minY;
    }

    public int getCenterX() {
        return this.minX + this.getWidthX() >> 1;
    }

    public int getCenterY() {
        return this.minY + this.getHeight() >> 1;
    }

    public int getCenterZ() {
        return this.minZ + this.getWidthZ() >> 1;
    }

    public @NotNull Vector getCenterAsVector() {
        return new Vector(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public @NotNull Vec3d getCenter() {
        return new Vec3d(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public int hashCode() {
        int result = 1;
        long temp = this.maxX;
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = this.maxY;
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = this.maxZ;
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = this.minX;
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = this.minY;
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = this.minZ;
        result = 31 * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntAABB IntAABB = (IntAABB) o;
        return Double.compare(minX, IntAABB.minX) == 0 && Double.compare(minY, IntAABB.minY) == 0 && Double.compare(minZ, IntAABB.minZ) == 0 && Double.compare(maxX, IntAABB.maxX) == 0 && Double.compare(maxY, IntAABB.maxY) == 0 && Double.compare(maxZ, IntAABB.maxZ) == 0;
    }

    @Override
    public String toString() {
        return "IntAABB{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                '}';
    }

}
