package org.by1337.blib.world;

import java.util.Objects;

public class Vector2D {

    public double x;
    public double z;

    public Vector2D(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public Vector2D() {
        x = 0;
        z = 0;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2D vector2D)) return false;
        return Double.compare(getX(), vector2D.getX()) == 0 && Double.compare(getZ(), vector2D.getZ()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getZ());
    }
}
