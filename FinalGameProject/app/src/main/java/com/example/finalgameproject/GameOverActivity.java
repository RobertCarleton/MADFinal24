package com.example.finalgameproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        int finalScore = getIntent().getIntExtra("finalScore", 0);
        TextView scoreText = findViewById(R.id.final_score);
        scoreText.setText("Your Score: " + finalScore);

        Button highScoresButton = findViewById(R.id.view_high_scores);
        highScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, HighScoresActivity.class);
            startActivity(intent);
            finish();
        });

        Button restartButton = findViewById(R.id.restart_game);
        restartButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}