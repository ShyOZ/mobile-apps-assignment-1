package com.mobileapps.assignments.presentation;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.mobileapps.assignments.R;
import com.mobileapps.assignments.data.ObstacleType;
import com.mobileapps.assignments.logic.GameManager;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class GameActivity extends AppCompatActivity {
    private GameManager game_manager;

    private ExtendedFloatingActionButton start_button;

    private ExtendedFloatingActionButton up_button;
    private ExtendedFloatingActionButton down_button;

    private ShapeableImageView img_background;

    private LinearLayout game_area;
    private final ArrayList<ConstraintLayout> lane_layouts = new ArrayList<>();
    private final ArrayList<ShapeableImageView> player_images = new ArrayList<>();

    private final ArrayList<ArrayList<ShapeableImageView>> obstacles = new ArrayList<>();
    private final ArrayList<ShapeableImageView> collision_obstacles = new ArrayList<>();
    private final ArrayList<ArrayList<ObstacleType>> obstacleTypes = new ArrayList<>();

    private TextView game_score;

    private ConstraintLayout lives_area;
    private final ArrayList<ShapeableImageView> lives_images = new ArrayList<>();

    private MediaPlayer collision_sound;
    private Toast collision_toast;
    private Vibrator vibrator;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        collision_sound = MediaPlayer.create(this, R.raw.nelson_ha_ha);

        collision_toast = Toasty.normal(this, "Ha Ha!", ResourcesCompat.getDrawable(getResources(), R.drawable.vec_nelson, getTheme()));

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        int interval = getResources().getInteger(R.integer.interval);
        int lives = getResources().getInteger(R.integer.lives);
        int lanes = getResources().getInteger(R.integer.lanes);
        for (int i = 0; i < lanes; i++)
            obstacles.add(new ArrayList<>());

        findViews();

        Glide.with(this).load(getResources().getString(R.string.background_image_url)).centerCrop().placeholder(R.drawable.ic_launcher_background).into(img_background);

        GameFieldInitializer game_initializer = new GameFieldInitializer(this);
        game_initializer.initGameField(game_area, lane_layouts, lanes, player_images, obstacles, collision_obstacles, obstacleTypes, lives_area, lives_images, lives);


        game_manager = new GameManager(lives, lanes, getResources().getInteger(R.integer.ticks_per_obstacle), game_initializer.getObstaclesPerLane(), getResources().getInteger(R.integer.mediumSpawn));

        up_button.setOnClickListener(v -> movePlayer(-1));

        down_button.setOnClickListener(v -> movePlayer(1));

        start_button.setOnClickListener(v -> {
            v.setVisibility(View.GONE);
            startTimer(interval);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startTimer(getResources().getInteger(R.integer.interval));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void startTimer(int interval) {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateUI());
            }
        }, 0, interval);

    }

    private void findViews() {
        start_button = findViewById(R.id.game_start_button);
        up_button = findViewById(R.id.game_up_button);
        down_button = findViewById(R.id.game_down_button);
        img_background = findViewById(R.id.game_img_background);
        game_area = findViewById(R.id.game_area);
        lives_area = findViewById(R.id.game_lives_area);
        game_score = findViewById(R.id.game_score);
    }

    private void updateUI() {
        setPlayerAndObstaclesVisibilityAs(View.INVISIBLE);
        if (game_manager.updateGame(vibrator, collision_sound, collision_toast))
            collision_obstacles.get(game_manager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        refreshUI();

    }

    private void refreshUI() {
        setPlayerAndObstaclesVisibilityAs(View.VISIBLE);
        updateLives();
        game_score.setText(String.format(Locale.US, "%03d", game_manager.getScore()));
    }

    private void updateLives() {
        if (game_manager.getLives() < lives_images.size())
            lives_images.get(game_manager.getLives()).setVisibility(View.INVISIBLE);

        if (game_manager.getLives() == 0) findViewById(R.id.game_over).setVisibility(View.VISIBLE);
    }

    private void setPlayerAndObstaclesVisibilityAs(int visibility) {
        game_manager.getActiveObstacles().forEach(obstacle_location -> obstacles.get(obstacle_location.getLane()).get(obstacle_location.getDistance()).setVisibility(visibility));

        player_images.get(game_manager.getPlayerLocation()).setVisibility(visibility);
    }

    private void movePlayer(int direction) {
        if ((direction < 0 && game_manager.getPlayerLocation() == 0) || (direction > 0 && game_manager.getPlayerLocation() == lane_layouts.size() - 1)) {
            return;
        }
        player_images.get(game_manager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        game_manager.movePlayer(direction);
        player_images.get(game_manager.getPlayerLocation()).setVisibility(View.VISIBLE);

        if (game_manager.checkCollision(vibrator, collision_sound, collision_toast))
            collision_obstacles.get(game_manager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        refreshUI();
    }
}