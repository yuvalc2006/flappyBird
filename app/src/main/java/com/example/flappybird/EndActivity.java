package com.example.flappybird;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView highScoreTextView;
    private Button quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        // Get the passed score and high score status from Intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("curr_score", 0);
        Long highScore = intent.getLongExtra("high_score", 0);
        boolean isNewHighScore = intent.getBooleanExtra("isNewHighScore", false);

        // Initialize views
        scoreTextView = findViewById(R.id.scoreTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);
        quitButton = findViewById(R.id.quitButton);

        // Set the displayed score
        scoreTextView.setText("Score: " + score);

        // Display high score message if it's a new high score
        if (isNewHighScore) {
            highScoreTextView.setText("New High Score!");

            // Add animation if it's a new high score
            AnimationSet animationSet = new AnimationSet(true);

            // Scale animation to make the text bigger
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.3f, 1f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(500);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setRepeatCount(Animation.INFINITE);

            // Alpha animation to make the text flash
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.5f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            alphaAnimation.setRepeatCount(Animation.INFINITE);

            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);

            highScoreTextView.startAnimation(animationSet);
        } else {
            highScoreTextView.setText("High Score: " + highScore);
        }

        // Handle quit button click
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0); // Close the activity and return to previous screen or exit the app
            }
        });
    }
}
