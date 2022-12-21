package com.mobileapps.assignments.data;

public class Obstacle {
    private int lane = 0;
    private int distance = -1;
    private ObstacleType type = ObstacleType.DUFF;

    public Obstacle() {
    }

    public Obstacle setLane(int lane) {
        this.lane = lane;
        return this;
    }

    public Obstacle setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public Obstacle setType(ObstacleType type) {
        this.type = type;
        return this;
    }

    public int getLane() {
        return lane;
    }

    public int getDistance() {
        return distance;
    }

    public ObstacleType getType() {
        return type;
    }

    public void move() {
        distance--;
    }

    public boolean isColliding(int playerLane) {
        return lane == playerLane && distance == 0;
    }

    public boolean isOffScreen() {
        return distance < 0;
    }
}
