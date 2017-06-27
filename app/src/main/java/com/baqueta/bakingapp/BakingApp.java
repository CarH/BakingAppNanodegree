package com.baqueta.bakingapp;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;

/**
 * Created by CarH on 04/05/2017.
 */

public class BakingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Log.v("BakingApp", "Setting debug");
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
    }
}
