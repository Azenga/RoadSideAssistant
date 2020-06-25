package com.project.roadsideassistant;

import android.app.Application;

public class RoadAssistantApplication extends Application {

    private static RoadAssistantApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static RoadAssistantApplication getInstance() {
        return instance;
    }
}
