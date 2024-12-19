package org.by1337.blib.util;

import blib.com.mojang.serialization.Codec;
import org.by1337.blib.configuration.serialization.DefaultCodecs;
import org.by1337.blib.geom.Vec3i;

public enum Direction {
    DOWN(0, -1, 0, Axis.NEGATIVE_Y),
    UP(0, 1, 0, Axis.POSITIVE_Y),
    NORTH(0, 0, -1, Axis.NEGATIVE_Z),
    SOUTH(0, 0, 1, Axis.POSITIVE_Z),
    WEST(-1, 0, 0, Axis.NEGATIVE_X),
    EAST(1, 0, 0, Axis.POSITIVE_X);
    public static final Codec<Direction> CODEC = DefaultCodecs.createEnumCodec(Direction.class);
    private final Vec3i direction;
    private final Axis axis;

    Direction(int x, int y, int z, Axis axis) {
        this(new Vec3i(x, y, z), axis);
    }

    Direction(Vec3i direction, Axis axis) {
        this.direction = direction;
        this.axis = axis;
    }

    public boolean isHorizontal() {
        return this != DOWN && this != UP;
    }

    public boolean isVertical() {
        return this == DOWN || this == UP;
    }

    public Vec3i getDirection() {
        return direction;
    }

    public Axis getAxis() {
        return axis;
    }

    public Direction getOpposite() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }

    public Direction getClockWise() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            default -> throw new IllegalStateException("Unable to get CW facing of " + this);
        };
    }

    public enum Axis {
        NEGATIVE_Y(DOWN),
        POSITIVE_Y(UP),
        NEGATIVE_X(WEST),
        POSITIVE_X(EAST),
        NEGATIVE_Z(NORTH),
        POSITIVE_Z(SOUTH);
        public static final Codec<Axis> CODEC = DefaultCodecs.createEnumCodec(Axis.class);
        private final Direction direction;

        Axis(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }
    }

}
