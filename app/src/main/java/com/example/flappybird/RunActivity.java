package com.example.flappybird;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import java.lang.Math;
import java.util.List;
import android.os.Handler;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RunActivity extends AppCompatActivity {

    private FrameLayout container;  // Container for game elements
    private Handler pipeHandler = null;  // Handler for pipe spawning
    private Runnable pipeSpawner = null;  // Runnable for pipe spawning logic
    private Handler scoreHandler = null;  // Handler for score updating
    private Runnable scoreUpdater = null;  // Runnable for score updating logic
    String username;  // Username of the player
    int curr_score;  // Current score of the player
    Long highScore;  // High score of the player
    Boolean finished = false;  // Flag to check if the game is finished

    TextView score;  // TextView to display the score

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        // Retrieve the username from the Intent
        username = getIntent().getStringExtra("username");

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Set a test value in the Firebase database
        DatabaseReference myRef = database.getReference("user");
        myRef.setValue("test");

        // Get the reference to the user's high score in the database
        myRef = database.getReference();
        DatabaseReference readRef = myRef.child("users").child(username);

        // Add a listener to retrieve the user's high score
        readRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long value = snapshot.getValue(long.class);
                if (value != null){
                    highScore = value;  // Retrieve high score from database
                } else {
                    highScore = (long) -1;  // Set high score to -1 if not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                highScore = (long) -1;  // Handle database error
            }
        });

        // Initialize the UI components
        container = findViewById(R.id.container);
        score = new TextView(this);
        score.setText("score: 0");  // Initial score display
        score.setTextSize(32);  // Set the text size
        score.setTypeface(null, Typeface.BOLD);  // Set the text style
        score.setTextColor(Color.parseColor("#FFFFFF"));  // Set the text color
        score.setShadowLayer(1.5f, -1, 1, Color.BLACK);  // Set the text shadow

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        container.addView(score, layoutParams);  // Add score TextView to container

        // Create a Constants object
        Constants constants = new Constants(this);

        // Create and add the Bird view to the layout
        Bird bird = new Bird(this);
        container.addView(bird);

        // Start the animation loop for the bird
        bird.startAnimationLoop(this);

        // Initialize the pipe spawning handler and runnable
        pipeHandler = new Handler();
        pipeSpawner = new Runnable() {
            @Override
            public void run() {
                spawnPipe(bird);  // Spawn a new pipe
                pipeHandler.postDelayed(this, 2500);  // Delay of 2500 milliseconds (2.5 seconds)
            }
        };
        pipeHandler.post(pipeSpawner);  // Start pipe spawning

        // Initialize the score updating handler and runnable
        scoreHandler = new Handler();
        scoreUpdater = new Runnable() {
            @Override
            public void run() {
                View v;
                Pipe pipe;
                final int childCount = container.getChildCount();
                int curr_score1 = 0;
                for (int i = 0; i < childCount; i++) {
                    v = container.getChildAt(i);
                    if (v instanceof Pipe){
                        pipe = (Pipe) v;
                        curr_score1 += pipe.getDidPass();  // Update score if bird passed the pipe
                        if (Bird.x + Bird.birdSize >= pipe.getX() && Bird.x <= pipe.getX() + pipe.getPipeWidth() && (Bird.y + Bird.birdSize >= pipe.getBottomPipeY() || Bird.y <= pipe.getTopPipeY())){
                            finished = true;  // Check for collision
                        }
                    }
                }
                curr_score1 += Constants.pipes_gone;  // Add the number of pipes gone to score
                curr_score = curr_score1;
                updateScore(curr_score1);  // Update the score display
                if (finished){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    if (curr_score > highScore){
                        myRef.child("users").child(username).setValue(curr_score);  // Update high score in database if current score is higher
                    }

                    Intent endGameIntent = new Intent(RunActivity.this, EndActivity.class);
                    endGameIntent.putExtra("curr_score", curr_score);  // Pass current score to EndActivity
                    endGameIntent.putExtra("high_score", highScore);  // Pass high score to EndActivity
                    endGameIntent.putExtra("isNewHighScore", curr_score > highScore);  // Pass whether it's a new high score
                    startActivity(endGameIntent);  // Start EndActivity
                    System.exit(0);  // Exit the game
                }
                scoreHandler.postDelayed(this, 1);  // Update score every millisecond
            }
        };
        scoreHandler.post(scoreUpdater);  // Start score updating
    }

    // Update the score display
    private void updateScore(int newScore) {
        score.setText("score: " + newScore);  // Update score TextView
    }

    // Method to spawn a new pipe
    private void spawnPipe(Bird bird) {
        Pipe pipe = new Pipe(this, bird);
        container.addView(pipe);  // Add new pipe to container
        pipe.startAnimationLoop(bird);  // Start animation loop for the pipe
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the pipe spawning when the activity is destroyed
        pipeHandler.removeCallbacks(pipeSpawner);
        Constants.pipes_gone += 1;  // Increment pipes_gone count
    }
}
