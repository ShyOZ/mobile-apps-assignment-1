package com.mobileapps.assignment1.data;

public class Obstacle {
    int lane;
    int distance;

    public Obstacle(int lane, int distance) {
        this.lane = lane;
        this.distance = distance;
    }

    public int getLane() {
        return lane;
    }

    public int getDistance() {
        return distance;
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
