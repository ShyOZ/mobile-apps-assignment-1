package com.mobileapps.assignment1.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.mobileapps.assignment1.R;

import java.util.ArrayList;
import java.util.Timer;

public class GameActivity extends AppCompatActivity {
    private ExtendedFloatingActionButton up_button;
    private ExtendedFloatingActionButton down_button;
    private ShapeableImageView img_background;
    private LinearLayout game_area;
    private ArrayList<ConstraintLayout> lane_layouts;
    private ArrayList<ShapeableImageView> player_images;

    private ConstraintLayout lives_area;
    private ArrayList<ShapeableImageView> player_lives;

    GameHelper gameHelper;

    private Timer timer;
    private final int lives = getResources().getInteger(R.integer.lives);
    private final int lanes = getResources().getInteger(R.integer.lanes);
    private int pos = lanes / 2;

    private final int interval = getResources().getInteger(R.integer.interval);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameHelper = new GameHelper(this);

        findViews();

        Glide
                .with(this)
                .load(getResources().getString(R.string.background_image_url))
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(img_background);

        gameHelper.initGameField(game_area,lane_layouts,lanes, player_images, lives_area, player_lives, lives);

        //TODO
        up_button.setOnClickListener(v -> {});

        //TODO
        down_button.setOnClickListener(v -> {});
    }

    private void findViews() {
        up_button = findViewById(R.id.game_up_button);
        down_button = findViewById(R.id.game_down_button);
        img_background = findViewById(R.id.game_img_background);
        game_area = findViewById(R.id.game_area);

        lane_layouts = new ArrayList<>();
        player_images = new ArrayList<>();
        player_lives = new ArrayList<>();
    }

    //TODO: delete, probably irrelevant
    private boolean checkViewIntersection(View v1, View v2) {
        int[] v1_pos = new int[2];
        int[] v2_pos = new int[2];
        v1.getLocationOnScreen(v1_pos);
        v2.getLocationOnScreen(v2_pos);
        Rect rect_v1 = new Rect(v1_pos[0], v1_pos[1], v1_pos[0] + v1.getMeasuredWidth(), v1_pos[0] + v1.getMeasuredHeight());
        Rect rect_v2 = new Rect(v2_pos[0], v2_pos[1], v2_pos[0] + v2.getMeasuredWidth(), v2_pos[0] + v2.getMeasuredWidth());
        return rect_v1.intersect(rect_v2);
    }

    //TODO
    private void updateUI(){

    }

    //TODO
    private void clicked(int dir){

    }
}