package com.app.remindd.activities;

import android.app.Application;

/**
 * Created by rishav on 5/12/16.
 */

public class MyApplication extends Application {

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}