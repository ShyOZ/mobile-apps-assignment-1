package com.mobileapps.assignment1.presentation;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.mobileapps.assignment1.R;

import java.util.ArrayList;

public class GameHelper {

    Context context;

    int startingMargin;
    float playerImageRatio;
    float playerLifeRatio;
    final float dpi_scale;

    public GameHelper(Context context) {
        this.context = context;


        //each car wil go from this margin (in dp) to 0
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        startingMargin = (int) (dm.heightPixels / dm.density);

        dpi_scale = context.getResources().getDisplayMetrics().density;

        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.img_bart_player, dimensions);
        playerImageRatio = (float) dimensions.outWidth / dimensions.outHeight;
        BitmapFactory.decodeResource(context.getResources(), R.drawable.img_bart_life, dimensions);
        playerLifeRatio = (float) dimensions.outWidth / dimensions.outHeight;
    }

    ConstraintLayout createLane(int index) {
        ConstraintLayout laneCL = new ConstraintLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        laneCL.setId(index);
        laneCL.setLayoutParams(layoutParams);
        return laneCL;
    }

    ShapeableImageView createLaneSeparator() {
        ShapeableImageView separatorSIV = new ShapeableImageView(context);

        int height = (int) (R.integer.separator_size * dpi_scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height);
        separatorSIV.setLayoutParams(layoutParams);
        return separatorSIV;
    }

    void createLanes(LinearLayout game_area, ArrayList<ConstraintLayout> lane_layouts, int lanes) {

        //first lane
        ConstraintLayout lane = createLane(1);
        game_area.addView(lane);
        lane_layouts.add(lane);

        // separator required with all of the rest
        for (int i = 1; i < lanes; i++) {
            game_area.addView(createLaneSeparator());
            lane = createLane(i+1);
            game_area.addView(lane);
            lane_layouts.add(lane);
        }
    }

    ShapeableImageView createPlayerImage() {
        ShapeableImageView playerSIV = new ShapeableImageView(context);
        Glide
                .with(context)
                .load(R.drawable.img_bart_player)
                .into(playerSIV);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams.dimensionRatio = String.valueOf(playerImageRatio);
        playerSIV.setLayoutParams(layoutParams);

        return playerSIV;
    }

    void createPlayers(ArrayList<ConstraintLayout> lane_layouts, ArrayList<ShapeableImageView> player_images, int lanes){
        for (int i = 0; i < lanes; i++) {
            ShapeableImageView current_player = createPlayerImage();
            player_images.add(current_player);
            current_player.setVisibility(View.INVISIBLE);
            lane_layouts.get(i).addView(current_player);
            ConstraintSet set = new ConstraintSet();
            set.clone(lane_layouts.get(i));
            set.connect(current_player.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
            set.connect(current_player.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
            set.connect(current_player.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
            set.applyTo(lane_layouts.get(i));
        }
    }

    ShapeableImageView createLife() {
        ShapeableImageView lifeSIV = new ShapeableImageView(context);
        Glide
                .with(context)
                .load(R.drawable.img_bart_life)
                .into(lifeSIV);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        layoutParams.dimensionRatio = String.valueOf(playerLifeRatio);
        lifeSIV.setLayoutParams(layoutParams);

        return lifeSIV;
    }

    //TODO
    void createLives(ConstraintLayout lives_area, ArrayList<ShapeableImageView> player_lives, int lives) {
        // first life

        // separator required with all of the rest
        int sep_width = (int) (R.integer.separator_size * dpi_scale + 0.5f);

        for (int i = 1; i < lives; i++) {
            Space s = new Space(context);

            s.setLayoutParams(new LinearLayout.LayoutParams(sep_width, LinearLayout.LayoutParams.MATCH_PARENT));

            lives_area.addView(s);
        }


    }

    void initGameField(LinearLayout game_area, ArrayList<ConstraintLayout> lane_layouts, int lanes, ArrayList<ShapeableImageView> player_images, ConstraintLayout lives_area, ArrayList<ShapeableImageView> player_lives, int lives) {

        createLanes(game_area, lane_layouts, lanes);

        createPlayers(lane_layouts, player_images, lanes);

        //createLives(lives_area, player_lives, lives);
    }
}
