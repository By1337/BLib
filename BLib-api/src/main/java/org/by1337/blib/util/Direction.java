package org.by1337.blib.util;

import org.by1337.blib.geom.Vec3i;

public enum Direction {
    DOWN(0, -1, 0, Axis.NEGATIVE_Y),
    UP(0, 1, 0, Axis.POSITIVE_Y),
    NORTH(0, 0, -1, Axis.NEGATIVE_Z),
    SOUTH(0, 0, 1, Axis.POSITIVE_Z),
    WEST(-1, 0, 0, Axis.NEGATIVE_X),
    EAST(1, 0, 0, Axis.POSITIVE_X);
    private final Vec3i direction;
    private final Axis axis;

    Direction(int x, int y, int z, Axis axis) {
        this(new Vec3i(x, y, z), axis);
    }

    Direction(Vec3i direction, Axis axis) {
        this.direction = direction;
        this.axis = axis;
    }

    public Vec3i getDirection() {
        return direction;
    }

    public Axis getAxis() {
        return axis;
    }

    public enum Axis {
        NEGATIVE_Y(DOWN),
        POSITIVE_Y(UP),
        NEGATIVE_X(WEST),
        POSITIVE_X(EAST),
        NEGATIVE_Z(NORTH),
        POSITIVE_Z(SOUTH);
        private final Direction direction;

        Axis(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }
    }

}
