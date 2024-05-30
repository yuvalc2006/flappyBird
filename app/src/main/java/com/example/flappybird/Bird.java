package com.example.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Bird extends View {
    static Paint birdPaint; // Paint object to define bird's color and style
    static float x; // Bird's x-coordinate
    static float y; // Bird's y-coordinate
    static float birdSize; // Size of the bird
    static float jumpHeight; // Height the bird jumps
    static float vy; // Bird's vertical velocity
    static Handler birdHandler; // Handler for managing animation
    static Runnable birdRunnable; // Runnable for animation loop

    public Bird(RunActivity context) {
        super(context);

        birdPaint = new Paint();
        birdPaint.setColor(Color.parseColor("#40E0D0")); // Set bird color
        x = Constants.screenWidth / 8; // Initial x position
        birdSize = Constants.screenWidth / 10; // Bird size
        y = Constants.screenHeight / 14; // Initial y position
        jumpHeight = Constants.screenHeight / 8; // Jump height
        vy = 0; // Initial vertical velocity
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the bird as a rectangle on the canvas
        canvas.drawRect(x, y, x + birdSize, y + birdSize, birdPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            jump(); // Handle jump on touch down event
            return true;
        }
        return super.onTouchEvent(event);
    }

    // Method to handle the bird's jump action
    private void jump() {
        vy = (float) -Math.sqrt(2 * Constants.gravity * jumpHeight); // Calculate upward velocity for jump
    }

    // Start the animation loop using a Runnable
    public void startAnimationLoop(RunActivity context) {
        birdHandler = new Handler();
        birdRunnable = new Runnable() {
            @Override
            public void run() {
                vy += Constants.gravity; // Apply gravity to vertical velocity
                y += vy; // Update bird's y position

                // Check if bird hits the bottom of the screen
                if (y >= Constants.screenHeight){
                    context.finished = true; // End the game if bird falls to the bottom
                }

                // Prevent bird from moving above the screen
                if (y <= 0){
                    vy = 0;
                }

                invalidate(); // Trigger redraw for animation
                postDelayed(this, 1); // Delay for smoother animation (adjust as needed)
            }
        };
        birdHandler.post(birdRunnable); // Start the animation loop
    }
    public void stopAnimationLoop(){
        birdHandler.removeCallbacks(birdRunnable);
    }
}
