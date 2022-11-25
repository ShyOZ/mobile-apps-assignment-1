package com.mobileapps.assignment1.presentation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.mobileapps.assignment1.R;

import java.util.ArrayList;

public class GameFieldInitializer {

    private final Context context;
    private final Resources resources;

    private final int separator_size;

    private int obstacles_per_lane;

    private final float player_image_ratio;
    private final float player_life_ratio;

    public GameFieldInitializer(Context context) {
        this.context = context;
        resources = context.getResources();

        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, R.drawable.img_bart_player, dimensions);
        player_image_ratio = (float) dimensions.outWidth / dimensions.outHeight;

        BitmapFactory.decodeResource(resources, R.drawable.img_bart_life, dimensions);
        player_life_ratio = (float) dimensions.outWidth / dimensions.outHeight;

        separator_size = (int) resources.getDimension(R.dimen.separator_size);
    }

    private ConstraintLayout createLane(int index) {
        ConstraintLayout laneCL = new ConstraintLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1);
        laneCL.setId(index);
        laneCL.setLayoutParams(layoutParams);
        return laneCL;
    }

    private ShapeableImageView createLaneSeparator() {
        ShapeableImageView separatorSIV = new ShapeableImageView(context);
        Glide
                .with(context)
                .load(R.drawable.dotted)
                .into(separatorSIV);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                separator_size);
        separatorSIV.setLayoutParams(layoutParams);
        return separatorSIV;
    }

    private void createLanes(LinearLayout game_area, ArrayList<ConstraintLayout> lane_layouts, int lanes) {

        //first lane
        ConstraintLayout lane = createLane(1);
        game_area.addView(lane);
        lane_layouts.add(lane);

        // separator required with all of the rest
        for (int i = 1; i < lanes; i++) {
            game_area.addView(createLaneSeparator());
            lane = createLane(i + 1);
            game_area.addView(lane);
            lane_layouts.add(lane);
        }
    }

    private ShapeableImageView createPlayerImage() {
        ShapeableImageView playerSIV = new ShapeableImageView(context);
        Glide
                .with(context)
                .load(R.drawable.img_bart_player)
                .into(playerSIV);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams.dimensionRatio = String.valueOf(player_image_ratio);
        playerSIV.setLayoutParams(layoutParams);

        return playerSIV;
    }

    private void createPlayers(ArrayList<ConstraintLayout> lane_layouts, ArrayList<ShapeableImageView> player_images, int lanes) {
        for (int i = 0; i < lanes; i++) {
            ShapeableImageView current_player = createPlayerImage();
            current_player.setId(View.generateViewId());
            player_images.add(current_player);
            current_player.setVisibility(View.INVISIBLE);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) current_player.getLayoutParams();
            layoutParams.topToTop = lane_layouts.get(i).getId();
            layoutParams.bottomToBottom = lane_layouts.get(i).getId();
            layoutParams.startToStart = lane_layouts.get(i).getId();

            lane_layouts.get(i).addView(current_player);
        }
    }

    private ShapeableImageView createLife() {
        ShapeableImageView lifeSIV = new ShapeableImageView(context);
        Glide
                .with(context)
                .load(R.drawable.img_bart_life)
                .into(lifeSIV);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        layoutParams.dimensionRatio = String.valueOf(player_life_ratio);
        lifeSIV.setLayoutParams(layoutParams);

        return lifeSIV;
    }

    private void createLives(ConstraintLayout lives_area, ArrayList<ShapeableImageView> player_lives, int lives) {
        // first life
        ShapeableImageView life = createLife();
        life.setId(View.generateViewId());
        lives_area.addView(life);
        player_lives.add(life);

        // Right align the health
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) life.getLayoutParams();
        layoutParams.endToEnd = lives_area.getId();
        layoutParams.topToTop = lives_area.getId();
        layoutParams.bottomToBottom = lives_area.getId();

        for (int i = 1; i < lives; i++) {

            life = createLife();
            life.setId(View.generateViewId());
            ((ConstraintLayout.LayoutParams) life.getLayoutParams()).setMarginEnd(separator_size);
            lives_area.addView(life);
            player_lives.add(life);

            layoutParams = (ConstraintLayout.LayoutParams) life.getLayoutParams();
            layoutParams.endToStart = player_lives.get(i - 1).getId();
            layoutParams.topToTop = lives_area.getId();
            layoutParams.bottomToBottom = lives_area.getId();
        }


    }

    private ShapeableImageView createObstacle() {
        ShapeableImageView obstacleSIV = new ShapeableImageView(context);
        Glide
                .with(context)
                .load(R.drawable.img_obstacle_duff)
                .into(obstacleSIV);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams.dimensionRatio = "1";
        obstacleSIV.setLayoutParams(layoutParams);

        return obstacleSIV;
    }

    private void createObstacles(ArrayList<ConstraintLayout> lane_layouts, ArrayList<ArrayList<ShapeableImageView>> obstacles, ArrayList<ShapeableImageView> collision_obstacles) {
//        // measuring how many obstacles fit on the screen
        int screen_width = resources.getDisplayMetrics().widthPixels;
        int screen_height = resources.getDisplayMetrics().heightPixels;
        int lane_width = (int) (screen_width - context.getResources().getDimension(R.dimen.width_to_reduce));
        int lane_height = (int) ((screen_height - context.getResources().getDimension(R.dimen.height_to_reduce) - (lane_layouts.size() - 1) * separator_size) / lane_layouts.size());
        obstacles_per_lane = lane_width / lane_height;

        ShapeableImageView obstacle;
        ConstraintLayout.LayoutParams layoutParams;

        for (int i = 0; i < lane_layouts.size(); i++) {
            ConstraintLayout lane = lane_layouts.get(i);
            obstacle = createObstacle();
            obstacle.setId(View.generateViewId());
            obstacle.setVisibility(View.INVISIBLE);
            lane.addView(obstacle);
            obstacles.get(i).add(obstacle);
            collision_obstacles.add(obstacle);

            layoutParams = (ConstraintLayout.LayoutParams) obstacle.getLayoutParams();
            layoutParams.topToTop = lane.getId();
            layoutParams.bottomToBottom = lane.getId();
            layoutParams.endToEnd = lane.getChildAt(0).getId();

            for (int j = 1; j <= obstacles_per_lane; j++) {
                obstacle = createObstacle();
                obstacle.setId(View.generateViewId());
                obstacle.setRotation(90 * j);
                obstacle.setVisibility(View.INVISIBLE);
                lane.addView(obstacle);
                obstacles.get(i).add(obstacle);

                layoutParams = (ConstraintLayout.LayoutParams) obstacle.getLayoutParams();
                layoutParams.topToTop = lane.getId();
                layoutParams.bottomToBottom = lane.getId();
                layoutParams.startToEnd = lane.getChildAt(j).getId();
            }
        }
    }

    public void initGameField(LinearLayout game_area, ArrayList<ConstraintLayout> lane_layouts, int lanes, ArrayList<ShapeableImageView> player_images, ArrayList<ArrayList<ShapeableImageView>> obstacles, ArrayList<ShapeableImageView> collision_obstacles, ConstraintLayout lives_area, ArrayList<ShapeableImageView> player_lives, int lives) {

        createLanes(game_area, lane_layouts, lanes);

        createPlayers(lane_layouts, player_images, lanes);
        player_images.get(lanes / 2).setVisibility(View.VISIBLE);

        createLives(lives_area, player_lives, lives);

        createObstacles(lane_layouts, obstacles, collision_obstacles);

        Log.d("game status in initializer", "obstacles per lane: " + getObstaclesPerLane());
    }

    public int getObstaclesPerLane() {
        return obstacles_per_lane;
    }
}
