package com.codingame.game.yinsh;

public enum Direction {
    TOP_LEFT(0,-1,-1),
    TOP_RIGHT(1,0,-1),
    RIGHT(2, 1, 0),
    DOWN_RIGHT(3, 1, 1),
    DOWN_LEFT(4, 0, 1),
    LEFT(5, -1, 0);

    private int value;
    private int x;
    private int y;

    Direction(int value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}