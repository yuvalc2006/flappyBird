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

        // Retrieve the score and high score information from the Intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("curr_score", 0);
        Long highScore = intent.getLongExtra("high_score", 0);
        boolean isNewHighScore = intent.getBooleanExtra("isNewHighScore", false);

        // Initialize the TextView and Button elements
        scoreTextView = findViewById(R.id.scoreTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);
        quitButton = findViewById(R.id.quitButton);

        // Set the score TextView to display the current score
        scoreTextView.setText("Score: " + score);

        // Check if the current score is a new high score
        if (isNewHighScore) {
            // Display a "New High Score!" message
            highScoreTextView.setText("New High Score!");

            // Create an animation set for the high score message
            AnimationSet animationSet = new AnimationSet(true);

            // Create a scale animation to enlarge the text
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    1f, 1.3f, 1f, 1.3f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleAnimation.setDuration(500);
            scaleAnimation.setRepeatMode(Animation.REVERSE);
            scaleAnimation.setRepeatCount(Animation.INFINITE);

            // Create an alpha animation to make the text flash
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.5f);
            alphaAnimation.setDuration(500);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            alphaAnimation.setRepeatCount(Animation.INFINITE);

            // Add both animations to the animation set
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);

            // Start the animations on the high score TextView
            highScoreTextView.startAnimation(animationSet);
        } else {
            // Display the high score if it is not a new high score
            highScoreTextView.setText("High Score: " + highScore);
        }

        // Set an OnClickListener on the quit button to exit the app when clicked
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0); // Exit the application
            }
        });
    }
}
