package com.mobileapps.assignment1.data;

import androidx.annotation.NonNull;

public class Obstacle {
    int lane;
    int distance;

    public Obstacle(){}

    public Obstacle setLane(int lane) {
        this.lane = lane;
        return this;
    }

    public Obstacle setDistance(int distance) {
        this.distance = distance;
        return this;
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

    @NonNull
    @Override
    public String toString() {
        return "Obstacle{" +
                "lane=" + lane +
                ", distance=" + distance +
                '}';
    }
}
