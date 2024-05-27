package com.example.flappybird;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import java.lang.Math;
import java.util.List;
import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private Button buttonStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editUsername);
        buttonStartGame = findViewById(R.id.startButton);

        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();

                if (!username.isEmpty()) {
                    // Start the game activity and pass the username
                    Intent intent = new Intent(MainActivity.this, RunActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    editTextUsername.setError("Please enter a username");
                }
            }
        });
    }
}
