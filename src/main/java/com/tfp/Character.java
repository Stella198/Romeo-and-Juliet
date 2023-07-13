package com.tfp;

public class Character {
    private String name;
    private Position position;

    public Character(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public int calculateTimeToPosition(int x, int y) {
        int currentX = position.getX();
        int currentY = position.getY();
        return Math.abs(x - currentX) + Math.abs(y - currentY);
    }
}
