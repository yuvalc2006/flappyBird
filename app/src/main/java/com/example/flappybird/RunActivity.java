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


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RunActivity extends AppCompatActivity {

    private FrameLayout container;
    private Handler pipeHandler = null;
    private Runnable pipeSpawner = null;
    private Handler scoreHandler = null;
    private Runnable scoreUpdater = null;
    String username;
    int curr_score;
    Long highScore;
    Boolean finished = false;

    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        username = getIntent().getStringExtra("username");

        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");
        myRef.setValue("test");

        myRef = database.getReference();
        DatabaseReference readRef = myRef.child("users").child(username);
        readRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long value = snapshot.getValue(long.class);
                if (value != null){
                    highScore = value;
                }
                else{
                    highScore = (long) -1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                highScore = (long) -1;
            }
        });
        container = findViewById(R.id.container);
        score = new TextView(this);
        score.setText("score: 0");
        score.setTextSize(32); // Set text size
        score.setTypeface(null, Typeface.BOLD);
        score.setTextColor(Color.parseColor("#FF5722"));
        score.setShadowLayer(1.5f, -1, 1, Color.BLACK);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        container.addView(score, layoutParams);

        Constants constants = new Constants(this);

        // Create a custom view (Bird) and add it to the layout
        Bird bird = new Bird(this);
        container.addView(bird);

        // Set up an animation loop for the bird
        bird.startAnimationLoop(this);

        pipeHandler = new Handler();
        pipeSpawner = new Runnable() {
            @Override
            public void run() {
                spawnPipe(bird);
                pipeHandler.postDelayed(this, 2500); // 3000 milliseconds (3 seconds) delay
            }
        };
        pipeHandler.post(pipeSpawner);
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
                        curr_score1 += pipe.getDidPass();
                        if (Bird.x + Bird.birdSize >= pipe.getX() && Bird.x <= pipe.getX() + pipe.getPipeWidth() && (Bird.y + Bird.birdSize >= pipe.getBottomPipeY() || Bird.y <= pipe.getTopPipeY())){
                            finished = true;
                        }
                    }
                }
                curr_score1 += Constants.pipes_gone;
                curr_score = curr_score1;
//                score.setText("score: " + String.valueOf(curr_score1));
                updateScore(curr_score1);
                if (finished){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    if (curr_score > highScore){
                        myRef.child("users").child(username).setValue(curr_score);
                    }

                    Intent endGameIntent = new Intent(RunActivity.this, EndActivity.class);
                    endGameIntent.putExtra("curr_score", curr_score);
                    endGameIntent.putExtra("high_score", highScore);
                    endGameIntent.putExtra("isNewHighScore", curr_score > highScore);
                    startActivity(endGameIntent);
                    System.exit(0);
                }
                pipeHandler.postDelayed(this, 1); // 3000 milliseconds (3 seconds) delay
            }
        };
        // Start spawning pipes
        scoreHandler.post(scoreUpdater);
    }

    private void updateScore(int newScore) {
        score.setText("score: " + newScore);
    }


    // Method to spawn a new pipe
    private void spawnPipe(Bird bird) {
        Pipe pipe = new Pipe(this, bird);
        container.addView(pipe);
        pipe.startAnimationLoop(bird);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the pipe spawning when the activity is destroyed
        pipeHandler.removeCallbacks(pipeSpawner);
        Constants.pipes_gone += 1;
    }
    public void endGame(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.setValue("hi");
        System.exit(0);
    }
}
