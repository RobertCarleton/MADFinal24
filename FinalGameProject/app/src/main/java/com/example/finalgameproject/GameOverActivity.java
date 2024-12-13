package com.example.finalgameproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity {

    private HighScoresDBHelper dbHelper;
    private int finalScore;
    private boolean isHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        dbHelper = new HighScoresDBHelper(this);

        // Retrieve final score from intent
        finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);

        TextView textFinalScore = findViewById(R.id.text_final_score);
        textFinalScore.setText("Final Score: " + finalScore);

        Button buttonSaveScore = findViewById(R.id.button_save_score);
        Button buttonViewHighScores = findViewById(R.id.button_view_high_scores);
        Button buttonRestart = findViewById(R.id.button_restart);

        // Check if the score qualifies as a high score
        isHighScore = dbHelper.isHighScore(finalScore);

        if (!isHighScore) {
            buttonSaveScore.setEnabled(false);
        }

        buttonSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHighScore();
            }
        });

        buttonViewHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHighScores();
            }
        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }

    private void saveHighScore() {
        EditText editName = findViewById(R.id.edit_player_name);
        String playerName = editName.getText().toString();

        if (playerName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.insertScore(playerName, finalScore);
        Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show();
    }

    private void viewHighScores() {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    private void restartGame() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}

