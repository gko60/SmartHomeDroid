package com.homematic.bidcos.rpc;

public enum Direction {
    NONE(0),
    SENDER(1),
    RECEIVER(2);

    private int value;

    Direction(int value) {
        this.value = value;
    }

    public static Direction get(int value) {
        for (Direction direction : Direction.values()) {
            if (direction.getValue() == value) {
                return direction;
            }
        }

        return Direction.NONE;
    }

    public int getValue() {
        return this.value;
    }
}
