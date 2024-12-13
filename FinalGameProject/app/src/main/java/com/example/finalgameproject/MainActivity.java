package com.example.finalgameproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button playButton = findViewById(R.id.playbutton);
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SequenceActivity.class);
            startActivity(intent);
        });

        Button highScoresButton = findViewById(R.id.viewScoresButton);
        highScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HighScoresActivity.class);
            startActivity(intent);
        });
    }
}