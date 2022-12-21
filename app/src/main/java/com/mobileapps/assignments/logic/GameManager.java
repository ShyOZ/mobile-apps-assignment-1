package com.mobileapps.assignments.logic;

import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.mobileapps.assignments.data.Obstacle;
import com.mobileapps.assignments.data.ObstacleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameManager {

    private int ticks;
    private int score;
    private int lives;
    private int playerLocation;
    private boolean isRunning = false;
    private final int spawnTime;
    private final int maxSpawnedObstacles;
    private final Random rand = new Random();
    private final int obstacleStartingDistance;
    private final ArrayList<Obstacle> activeObstacles;
    private final ArrayList<Integer> obstacleLanes;

    public GameManager(int lives, int lanes, int spawnTime, int obstacleStartingDistance, int maxSpawnedObstacles) {
        ticks = 0;
        score = 0;
        this.lives = lives;
        playerLocation = lanes / 2;
        activeObstacles = new ArrayList<>();
        this.spawnTime = spawnTime;
        this.maxSpawnedObstacles = maxSpawnedObstacles;
        this.obstacleStartingDistance = obstacleStartingDistance;
        obstacleLanes = IntStream.range(0, lanes).boxed().collect(Collectors.toCollection(ArrayList::new));
    }


    public void movePlayer(int direction) {
        if (direction > 0) {
            playerLocation++;
        } else {
            playerLocation--;
        }
    }

    public boolean checkCollision(Vibrator vibrator, MediaPlayer collisionSound, Toast collisionToast) {
        for (Obstacle obstacle : activeObstacles) {
            if (obstacle.isColliding(playerLocation)) {
                handleCollision(vibrator, collisionSound, collisionToast, obstacle.getType());
                activeObstacles.remove(obstacle);
                return true;
            }
        }
        return false;
    }

    private void handleCollision(Vibrator vibrator, MediaPlayer collisionSound, Toast collisionToast, ObstacleType type) {

        switch (type) {
            case DUFF:
                if (lives > 0) lives--;
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                collisionSound.start();
                collisionToast.show();
                break;
            case DONUT:
                score += 5;
                break;
        }
    }

    public boolean updateGame(Vibrator vibrator, MediaPlayer collisionSound, Toast collisionToast) {
        ticks++;
        if (ticks % spawnTime == 0) {
            ticks = 0;
            spawnObstacles(rand.nextInt(maxSpawnedObstacles) + 1);

        }

        for (Iterator<Obstacle> iterator = activeObstacles.iterator(); iterator.hasNext(); ) {
            Obstacle obstacle = iterator.next();
            obstacle.move();
            if (obstacle.isOffScreen()) {
                iterator.remove();
                if (lives > 0) score++;
            }
        }

        return checkCollision(vibrator, collisionSound, collisionToast);
    }

    private void spawnObstacles(int obstacleCount) {
        Collections.shuffle(obstacleLanes);
        for (int i = 0; i < obstacleCount; i++) {
            activeObstacles.add(new Obstacle().setLane(obstacleLanes.get(i)).setDistance(obstacleStartingDistance).setType(ObstacleType.getRandomObstacleType()));
        }
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getPlayerLocation() {
        return playerLocation;
    }

    public ArrayList<Obstacle> getActiveObstacles() {
        return activeObstacles;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void startGame() {
        isRunning = true;
    }
}
