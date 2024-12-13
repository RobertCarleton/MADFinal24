package com.example.finalgameproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class HighScoresActivity extends Activity {

    private HighScoresDBHelper dbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        dbHelper = new HighScoresDBHelper(this);
        listView = findViewById(R.id.list_high_scores);

        Button buttonBackToMainMenu = findViewById(R.id.button_back_to_main_menu);
        buttonBackToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainMenu();
            }
        });

        displayHighScores();
    }

    private void displayHighScores() {
        Cursor cursor = dbHelper.getTopFiveScores();

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No high scores available", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fromColumns = {"name", "score"};
        int[] toViews = {R.id.text_name, R.id.text_score};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.high_score_item,
                cursor,
                fromColumns,
                toViews,
                0
        );

        listView.setAdapter(adapter);
    }

    private void returnToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears all other activities
        startActivity(intent);
        finish(); // Ends the current activity
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
