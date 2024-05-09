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
    static Paint birdPaint;
    static float x;
    static float y;
    static float birdSize;
    static float jumpHeight;
    static int score;
    static float vy;
    static Handler birdHandler;
    static Runnable birdRunnable;

    public Bird(RunActivity context) {
        super(context);

        birdPaint = new Paint();
        birdPaint.setColor(Color.BLUE);
        x = Constants.screenWidth / 8;
        birdSize = Constants.screenWidth / 10;
        y = Constants.screenHeight / 14;
        jumpHeight = Constants.screenHeight / 8;
        score = 0;
        vy = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the bird (rectangle) on the canvas
        canvas.drawRect((float) x, (float) y, (float) (x + birdSize), (float) (y + birdSize), birdPaint);

        // Request redraw (animation)
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            jump();
            return true;
        }
        return super.onTouchEvent(event);
    }

    // Method to handle the bird's jump action
    private void jump() {
        vy = (float) -Math.sqrt(2 * Constants.gravity * jumpHeight); // Simulate a jump (move rectangle upward)
    }

    // Start the animation loop using a Runnable
    public void startAnimationLoop(RunActivity context) {
        birdHandler = new Handler();
        birdRunnable = new Runnable() {
            @Override
            public void run() {
                vy += Constants.gravity;
                y += vy;
                if (y >= Constants.screenHeight){
                    context.finished = true;
                }
                if (y <= 0){
                    vy = 0;
                }
                invalidate(); // Trigger redraw
                postDelayed(this, 1); // Delay for smoother animation (adjust as needed)
            }
        };
        birdHandler.post(birdRunnable);
    }
}