package com.project.roadsideassistant.utils;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.project.roadsideassistant.RoadAssistantApplication;

public class UIHelpers {

    public static void toast(String message) {
        Toast.makeText(RoadAssistantApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void snackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("OK", v -> {
        }).show();
    }
}
