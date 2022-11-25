package com.mobileapps.assignment1.logic;

import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.mobileapps.assignment1.data.Obstacle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameManager {

    private int ticks;
    private int score;
    private int lives;
    private final int lanes;
    int player_location;
    private final int ticks_per_obstacle;
    private final int obstacle_starting_distance;
    private final ArrayList<Obstacle> active_obstacles;
    Random rand = new Random();

    public GameManager(int lives, int lanes, int ticks_per_obstacle, int obstacle_starting_distance) {
        ticks = 0;
        score = 0;
        this.lanes = lanes;
        this.lives = lives;
        active_obstacles = new ArrayList<>();
        this.player_location = lanes / 2;
        this.ticks_per_obstacle = ticks_per_obstacle;
        this.obstacle_starting_distance = obstacle_starting_distance;
    }

    public void movePlayer(int direction) {
        if (direction > 0) {
            player_location++;
        } else {
            player_location--;
        }
    }

    public void addObstacle(Obstacle obstacle) {
        active_obstacles.add(obstacle);
    }

    public boolean checkCollision(Vibrator vibrator, MediaPlayer collision_sound, Toast collision_toast) {
        for (Obstacle obstacle : active_obstacles) {
            if (obstacle.isColliding(player_location)) {
                if (lives > 0)
                    lives--;
                active_obstacles.remove(obstacle);
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                collision_sound.start();
                collision_toast.show();
                return true;
            }
        }

        return false;
    }

    public boolean updateGame(Vibrator vibrator, MediaPlayer collision_sound, Toast collision_toast) {
        if (lives > 0)
            score++;
        ticks++;
        if (ticks % ticks_per_obstacle == 0) {
            ticks = 0;
            addObstacle(new Obstacle()
                    .setLane(rand.nextInt(lanes))
                    .setDistance(obstacle_starting_distance));
        }

        for (Iterator<Obstacle> iterator = active_obstacles.iterator(); iterator.hasNext(); ) {
            Obstacle obstacle = iterator.next();
            obstacle.move();
            if (obstacle.isOffScreen())
                iterator.remove();
        }

        return checkCollision(vibrator, collision_sound, collision_toast);
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getPlayerLocation() {
        return player_location;
    }

    public ArrayList<Obstacle> getActiveObstacles() {
        return active_obstacles;
    }
}
