package com.mobileapps.assignments.presentation.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
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
import com.mobileapps.assignments.presentation.GameFieldInitializer;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class GameActivity extends AppCompatActivity {
    private GameManager gameManager;

    private ExtendedFloatingActionButton startButton;

    private ExtendedFloatingActionButton upButton;
    private ExtendedFloatingActionButton downButton;

    private ShapeableImageView imgBackground;

    private LinearLayout gameArea;
    private final ArrayList<ConstraintLayout> laneLayouts = new ArrayList<>();
    private final ArrayList<ShapeableImageView> playerImages = new ArrayList<>();

    private final ArrayList<ArrayList<ShapeableImageView>> obstacles = new ArrayList<>();
    private final ArrayList<ArrayList<ObstacleType>> obstacleTypes = new ArrayList<>();
    private final ArrayList<ShapeableImageView> collisionObstacles = new ArrayList<>();

    private TextView gameScore;

    private ConstraintLayout livesArea;
    private final ArrayList<ShapeableImageView> livesImages = new ArrayList<>();

    private MediaPlayer collisionSound;
    private Toast collisionToast;
    private Vibrator vibrator;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        collisionSound = MediaPlayer.create(this, R.raw.nelson_ha_ha);

        collisionToast = Toasty.normal(
                this,
                "Ha Ha!",
                ResourcesCompat.getDrawable(getResources(), R.drawable.vec_nelson, getTheme()));

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        int interval = getResources().getInteger(R.integer.interval);
        int lives = getResources().getInteger(R.integer.lives);
        int lanes = getResources().getInteger(R.integer.lanes);

        findViews();

        Glide
                .with(this)
                .load(getResources().getString(R.string.background_image_url))
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgBackground);

        GameFieldInitializer gameInitializer = new GameFieldInitializer(this);
        gameInitializer.initGameField(gameArea, laneLayouts, lanes, playerImages, obstacles, collisionObstacles, obstacleTypes, livesArea, livesImages, lives);


        gameManager = new GameManager(
                lives,
                lanes,
                getResources().getInteger(R.integer.ticks_per_obstacle),
                gameInitializer.getObstaclesPerLane(),
                getResources().getInteger(R.integer.mediumSpawn));

        upButton.setOnClickListener(v -> movePlayer(-1));
        downButton.setOnClickListener(v -> movePlayer(1));

        startButton.setOnClickListener(v -> {
            v.setVisibility(View.GONE);
            gameManager.startGame();
            startTimer(interval);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("GameActivity", "onStop");
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (gameManager.isRunning()) {
            startTimer(getResources().getInteger(R.integer.interval));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
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
        startButton = findViewById(R.id.game_start_button);
        upButton = findViewById(R.id.game_up_button);
        downButton = findViewById(R.id.game_down_button);
        imgBackground = findViewById(R.id.game_img_background);
        gameArea = findViewById(R.id.game_area);
        livesArea = findViewById(R.id.game_lives_area);
        gameScore = findViewById(R.id.game_score);
    }

    private void updateUI() {
        setPlayerAndObstaclesVisibilityAs(View.INVISIBLE);
        if (gameManager.updateGame(vibrator, collisionSound, collisionToast))
            collisionObstacles.get(gameManager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        refreshUI();

    }

    private void refreshUI() {
        setPlayerAndObstaclesVisibilityAs(View.VISIBLE);
        updateLives();
        gameScore.setText(String.format(Locale.US, "%03d", gameManager.getScore()));
    }

    private void updateLives() {
        if (gameManager.getLives() < livesImages.size())
            livesImages.get(gameManager.getLives()).setVisibility(View.INVISIBLE);

        if (gameManager.getLives() == 0)
            findViewById(R.id.game_over).setVisibility(View.VISIBLE);
    }

    private void setPlayerAndObstaclesVisibilityAs(int visibility) {
        gameManager.getActiveObstacles().forEach(
                obstacle -> {
                    ShapeableImageView obstacleSIV = obstacles.get(obstacle.getLane()).get(obstacle.getDistance());
                    ObstacleType obstacleType = obstacleTypes.get(obstacle.getLane()).get(obstacle.getDistance());

                    if (obstacleType != obstacle.getType()) {
                        Glide.with(this)
                                .load(obstacle.getType().getResourceId())
                                .centerCrop()
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(obstacleSIV);
                        obstacleTypes.get(obstacle.getLane()).set(obstacle.getDistance(), obstacle.getType());
                    }

                    obstacleSIV
                            .setVisibility(visibility);
                });

        playerImages
                .get(gameManager.getPlayerLocation())
                .setVisibility(visibility);
    }

    private void movePlayer(int direction) {
        if ((direction < 0 && gameManager.getPlayerLocation() == 0)
                || (direction > 0 && gameManager.getPlayerLocation() == laneLayouts.size() - 1)) {
            return;
        }
        playerImages.get(gameManager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        gameManager.movePlayer(direction);
        playerImages.get(gameManager.getPlayerLocation()).setVisibility(View.VISIBLE);

        if (gameManager.checkCollision(vibrator, collisionSound, collisionToast))
            collisionObstacles.get(gameManager.getPlayerLocation()).setVisibility(View.INVISIBLE);
        refreshUI();
    }
}