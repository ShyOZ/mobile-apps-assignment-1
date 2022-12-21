package com.mobileapps.assignments.data;

import com.google.android.gms.maps.model.LatLng;

public class Score {
    private String playerName = "";
    private int score = 0;
    private LatLng location = new LatLng(0, 0);

    public Score() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public Score setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Score setScore(int score) {
        this.score = score;
        return this;
    }

    public LatLng getLocation() {
        return location;
    }

    public Score setLocation(LatLng location) {
        this.location = location;
        return this;
    }
}
