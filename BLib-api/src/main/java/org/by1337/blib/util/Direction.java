package org.by1337.blib.util;

import org.by1337.blib.geom.Vec3i;

public enum Direction {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0),
    NEGATIVE_Y(DOWN),
    POSITIVE_Y(UP),
    NEGATIVE_X(WEST),
    POSITIVE_X(EAST),
    NEGATIVE_Z(NORTH),
    POSITIVE_Z(SOUTH);

    private final Vec3i direction;
    private final Direction root;

    Direction(int x, int y, int z) {
        this(new Vec3i(x, y, z));
    }

    Direction(Vec3i direction) {
        this.direction = direction;
        root = this;
    }

    Direction(Direction direction) {
        this.direction = direction.direction;
        root = direction;
    }

    public Vec3i getDirection() {
        return direction;
    }

    public Direction getRoot() {
        return root;
    }
}
