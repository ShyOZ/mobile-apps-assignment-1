package com.mobileapps.assignment1.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.mobileapps.assignment1.R;
import com.mobileapps.assignment1.logic.GameManager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    GameManager gameManager;

    private ExtendedFloatingActionButton up_button;
    private ExtendedFloatingActionButton down_button;

    private ShapeableImageView img_background;

    private LinearLayout game_area;
    private final ArrayList<ConstraintLayout> lane_layouts = new ArrayList<>();
    private final ArrayList<ShapeableImageView> player_images = new ArrayList<>();

    private final ArrayList<ArrayList<ShapeableImageView>> obstacles = new ArrayList<>();
    private final ArrayList<ShapeableImageView> collision_obstacles = new ArrayList<>();

    private TextView game_score;

    private ConstraintLayout lives_area;
    private final ArrayList<ShapeableImageView> lives_images = new ArrayList<>();

    GameFieldInitializer gameInitializer;

    private MediaPlayer collisionSound;
    private Toast collisionToast;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        collisionSound = MediaPlayer.create(this, R.raw.nelson_ha_ha);
        collisionToast = Toast.makeText(this, "Ha Ha!", Toast.LENGTH_SHORT);
        ShapeableImageView toastImage = new ShapeableImageView(this);
        Glide.with(this).load(R.drawable.nelson).into(toastImage);
        collisionToast.setView(toastImage);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        int interval = getResources().getInteger(R.integer.interval);
        int lives = getResources().getInteger(R.integer.lives);
        int lanes = getResources().getInteger(R.integer.lanes);
        for (int i = 0; i < lanes; i++) {
            obstacles.add(new ArrayList<>());
        }
        int obstacles_per_lane = lanes * 4; // 4 obstacles per lane per lane, fits for now

        gameInitializer = new GameFieldInitializer(this);

        gameManager = new GameManager(lives, lanes, getResources().getInteger(R.integer.interval), obstacles_per_lane);

        findViews();

        Glide
                .with(this)
                .load(getResources().getString(R.string.background_image_url))
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(img_background);

        gameInitializer.initGameField(game_area, lane_layouts, lanes, player_images, obstacles, collision_obstacles, lives_area, lives_images, lives);

        up_button.setOnClickListener(v -> movePlayer(-1));

        down_button.setOnClickListener(v -> movePlayer(1));

        startTimer(interval);
    }

    private void startTimer(int interval) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateUI());
            }
        }, 0, interval);

    }

    private void findViews() {
        up_button = findViewById(R.id.game_up_button);
        down_button = findViewById(R.id.game_down_button);
        img_background = findViewById(R.id.game_img_background);
        game_area = findViewById(R.id.game_area);
        lives_area = findViewById(R.id.game_lives_area);
        game_score = findViewById(R.id.game_score);
    }

    private void updateUI() {
        setPlayerAndObstaclesVisibilityAs(View.INVISIBLE);
        if(gameManager.updateGame(vibrator, collisionSound, collisionToast))
            collision_obstacles.get(gameManager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        refreshUI();

    }

    private void refreshUI() {
        setPlayerAndObstaclesVisibilityAs(View.VISIBLE);
        updateLives();
        game_score.setText(String.format(Locale.US, "%03d", gameManager.getScore()));
    }

    private void updateLives() {
        if (gameManager.getLives() == 0) {
            findViewById(R.id.game_over).setVisibility(View.VISIBLE);
        } else if (gameManager.getLives() < lives_images.size()) {
            lives_images.get(gameManager.getLives()).setVisibility(View.INVISIBLE);
        }
    }

    private void setPlayerAndObstaclesVisibilityAs(int visibility) {
        gameManager.getActiveObstacles().forEach(
                obstacle_location ->
                        obstacles
                                .get(obstacle_location.getLane())
                                .get(obstacle_location.getDistance())
                                .setVisibility(visibility));

        player_images
                .get(gameManager.getPlayerLocation())
                .setVisibility(visibility);
    }

    private void movePlayer(int direction) {
        if((direction < 0 && gameManager.getPlayerLocation() == 0)
        || (direction > 0 && gameManager.getPlayerLocation() == lane_layouts.size() - 1)) {
            return;
        }
        player_images.get(gameManager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        gameManager.movePlayer(direction);
        player_images.get(gameManager.getPlayerLocation()).setVisibility(View.VISIBLE);

        if (gameManager.checkCollision(vibrator, collisionSound, collisionToast))
            collision_obstacles.get(gameManager.getPlayerLocation()).setVisibility(View.INVISIBLE);
            refreshUI();
    }
}