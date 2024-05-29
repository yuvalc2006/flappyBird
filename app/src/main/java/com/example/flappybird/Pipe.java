package com.example.flappybird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.util.Random;
import android.os.Handler;

public class Pipe extends View {
    private static Paint pipePaint;
    private final float pipeWidth = Constants.screenWidth / 8;
    private float x;
    private float vx = -5;
    private float bottomPipeY;
    private float topPipeY;
    private float gapLength;
    private float gapStart;
    private int didPass = 0;
    private Handler pipeHandler;
    private Runnable pipeRunnable;

    // Constructor for Pipe, initializing position, size, and color
    public Pipe(Context context, Bird bird) {
        super(context);
        x = Constants.screenWidth;
        pipePaint = new Paint();
        pipePaint.setColor(Color.GREEN);
        Random random = new Random();

        // Calculate gap length and position
        gapLength = random.nextFloat() * Bird.birdSize * 5 + Bird.birdSize * 4; // Adjust gap size
        gapStart = random.nextFloat() * (Constants.screenHeight - gapLength);

        bottomPipeY = gapStart + gapLength;
        topPipeY = gapStart;
    }

    // Get the paint used for the pipes
    public static Paint getPipePaint() {
        return pipePaint;
    }

    // Set the paint used for the pipes
    public static void setPipePaint(Paint pipePaint) {
        Pipe.pipePaint = pipePaint;
    }

    // Get the width of the pipe
    public float getPipeWidth() {
        return pipeWidth;
    }

    // Override the getX method to return the current x position of the pipe
    @Override
    public float getX() {
        return x;
    }

    // Override the setX method to set the current x position of the pipe
    @Override
    public void setX(float x) {
        this.x = x;
    }

    // Get the velocity of the pipe
    public float getVx() {
        return vx;
    }

    // Set the velocity of the pipe
    public void setVx(float vx) {
        this.vx = vx;
    }

    // Get the y position of the bottom pipe
    public float getBottomPipeY() {
        return bottomPipeY;
    }

    // Set the y position of the bottom pipe
    public void setBottomPipeY(float bottomPipeY) {
        this.bottomPipeY = bottomPipeY;
    }

    // Get the y position of the top pipe
    public float getTopPipeY() {
        return topPipeY;
    }

    // Set the y position of the top pipe
    public void setTopPipeY(float topPipeY) {
        this.topPipeY = topPipeY;
    }

    // Get the length of the gap between the pipes
    public float getGapLength() {
        return gapLength;
    }

    // Set the length of the gap between the pipes
    public void setGapLength(float gapLength) {
        this.gapLength = gapLength;
    }

    // Get the starting position of the gap between the pipes
    public float getGapStart() {
        return gapStart;
    }

    // Set the starting position of the gap between the pipes
    public void setGapStart(float gapStart) {
        this.gapStart = gapStart;
    }

    // Get whether the bird has passed the pipe
    public int getDidPass() {
        return didPass;
    }

    // Set whether the bird has passed the pipe
    public void setDidPass(int didPass) {
        this.didPass = didPass;
    }

    // Override the onDraw method to draw the pipes on the canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw bottom pipe
        canvas.drawRect(x, bottomPipeY, x + pipeWidth, Constants.screenHeight, pipePaint);
        // Draw top pipe
        canvas.drawRect(x, 0, x + pipeWidth, topPipeY, pipePaint);
    }

    // Start the animation loop for moving the pipes
    public void startAnimationLoop(Bird bird) {
        pipeHandler = new Handler();
        pipeRunnable = new Runnable() {
            @Override
            public void run() {
                x += vx; // Move the pipe left
                // Check if bird has passed the pipe
                if (didPass == 0 && bird.getX() >= x){
                    didPass = 1; // Mark as passed
                }
                invalidate(); // Trigger redraw
                postDelayed(this, 1); // Delay for smoother animation (adjust as needed)
            }
        };
        pipeHandler.post(pipeRunnable);
    }
}
