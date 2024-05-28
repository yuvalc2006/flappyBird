package com.example.flappybird;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class Constants {
    static int pipes_gone = 0; // Tracks the number of pipes the bird has passed
    static float gravity; // Represents the gravity force applied to the bird
    static float screenHeight; // Holds the screen height in pixels
    static float screenWidth; // Holds the screen width in pixels

    // Constructor to initialize screen dimensions and gravity based on the screen height
    public Constants(Context context) {
        Point screenSize = getScreenSize(context);
        screenHeight = screenSize.y; // Set the screen height
        screenWidth = screenSize.x; // Set the screen width
        gravity = screenHeight / 4000; // Calculate gravity based on screen height
    }

    // Retrieves the screen size as a Point object containing width and height
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); // Access the window manager
        Display display = windowManager.getDefaultDisplay(); // Get the default display
        Point screenSize = new Point(); // Create a Point object to store screen dimensions
        display.getSize(screenSize); // Populate screenSize with the display dimensions

        return screenSize; // Return the screen size
    }
}
