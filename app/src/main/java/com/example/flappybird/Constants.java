package com.example.flappybird;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class Constants {
    static int pipes_gone = 0;
    static float gravity;
    static float screenHeight;
    static float screenWidth;
    static int scoreFontSize;

    public Constants(Context context) {
        Point screenSize = getScreenSize(context);
        screenHeight = screenSize.y;
        screenWidth = screenSize.x;
        scoreFontSize = screenSize.y / 10;
        gravity = screenHeight / 4000;
    }

    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize); // This method populates screenSize with display dimensions

        return screenSize;
    }
}