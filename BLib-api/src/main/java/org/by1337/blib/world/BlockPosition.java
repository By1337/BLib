package org.by1337.blib.world;

import org.bukkit.util.Vector;

import java.util.Objects;
@Deprecated
public class BlockPosition {
    private int x = 0;
    private int y = 0;
    private int z = 0;

    public BlockPosition() {
    }

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector toVector(){
        return new Vector(x, y, z);
    }
    public BlockPosition add(BlockPosition position) {
        return new BlockPosition(x + position.x, y + position.y, z + position.z);
    }
    public BlockPosition add(int x, int y, int z) {
        return new BlockPosition(this.x + x, this.y + y, this.z + z);
    }

    public BlockPosition subtract(BlockPosition position) {
        return new BlockPosition(x - position.x, y - position.y, z - position.z);
    }
    public BlockPosition subtract(int x, int y, int z) {
        return new BlockPosition(this.x - x, this.y - y, this.z - z);
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockPosition that)) return false;
        return getX() == that.getX() && getY() == that.getY() && getZ() == that.getZ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }
}
