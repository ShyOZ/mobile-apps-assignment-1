package com.mobileapps.assignments.presentation;

import android.app.Application;

import com.paz.prefy_lib.Prefy;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Prefy.init(this, true);
    }
}
