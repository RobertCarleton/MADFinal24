package com.example.finalgameproject;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HighScoresActivity extends Activity {

    private HighScoresDBHelper dbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        dbHelper = new HighScoresDBHelper(this);
        listView = findViewById(R.id.list_high_scores);

        displayHighScores();
    }

    private void displayHighScores() {
        Cursor cursor = dbHelper.getTopFiveScores();

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

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}