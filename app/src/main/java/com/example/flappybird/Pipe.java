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

    public static Paint getPipePaint() {
        return pipePaint;
    }

    public static void setPipePaint(Paint pipePaint) {
        Pipe.pipePaint = pipePaint;
    }

    public float getPipeWidth() {
        return pipeWidth;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getBottomPipeY() {
        return bottomPipeY;
    }

    public void setBottomPipeY(float bottomPipeY) {
        this.bottomPipeY = bottomPipeY;
    }

    public float getTopPipeY() {
        return topPipeY;
    }

    public void setTopPipeY(float topPipeY) {
        this.topPipeY = topPipeY;
    }

    public float getGapLength() {
        return gapLength;
    }

    public void setGapLength(float gapLength) {
        this.gapLength = gapLength;
    }

    public float getGapStart() {
        return gapStart;
    }

    public void setGapStart(float gapStart) {
        this.gapStart = gapStart;
    }

    public int getDidPass() {
        return didPass;
    }

    public void setDidPass(int didPass) {
        this.didPass = didPass;
    }

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // Draw bottom pipe
        canvas.drawRect((float) x, (float) bottomPipeY, (float) (x + pipeWidth), (float) Constants.screenHeight, pipePaint);
        // Draw top pipe
        canvas.drawRect((float) x, 0, (float) (x + pipeWidth), (float) topPipeY, pipePaint);
    }

    public void startAnimationLoop(Bird bird) {
        pipeHandler = new Handler();
        pipeRunnable = new Runnable() {
            @Override
            public void run() {
                x += vx;
                if (didPass == 0 && bird.getX() >= x + pipeWidth){
                    didPass = 1;
                }
                invalidate(); // Trigger redraw
                postDelayed(this, 1); // Delay for smoother animation (adjust as needed)
            }
        };
        pipeHandler.post(pipeRunnable);
    }
}
