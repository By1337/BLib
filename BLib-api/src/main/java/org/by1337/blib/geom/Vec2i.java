package org.by1337.blib.geom;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.errorprone.annotations.Immutable;

@Immutable
public final class Vec2i {
    public static final Codec<Vec2i> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(Vec2i::getX),
            Codec.INT.fieldOf("y").forGetter(Vec2i::getY)
    ).apply(instance, Vec2i::new));

    public final int x;
    public final int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static double distanceSq(int x1, int y1, int x2, int y2) {
        x1 -= x2;
        y1 -= y2;
        return (x1 * x1 + y1 * y1);
    }

    public static double distance(int x1, int y1, int x2, int y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public double distanceSq(int vx, int vy) {
        vx -= x;
        vy -= y;
        return (vx * vx + vy * vy);
    }

    public double distanceSq(Vec2i v) {
        int vx = v.x - this.x;
        int vy = v.y - this.y;
        return (vx * vx + vy * vy);
    }

    public double distance(int vx, int vy) {
        vx -= x;
        vy -= y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    public double distance(Vec2i v) {
        int vx = v.x - this.x;
        int vy = v.y - this.y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        long bits = 7L;
        bits = 31L * bits + x;
        bits = 31L * bits + y;
        return (int) (bits ^ (bits >> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec2i v) {
            return (x == v.x) && (y == v.y);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vec2i[" + x + ", " + y + "]";
    }

}
