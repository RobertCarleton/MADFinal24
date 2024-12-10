package com.example.finalgameproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity {
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        HighScoresDBHelper dbHelper = new HighScoresDBHelper(this);
        database = dbHelper.getReadableDatabase();

        TextView highScoresText = findViewById(R.id.high_scores_list);
        highScoresText.setText(getHighScores());
    }

    private String getHighScores() {
        StringBuilder highScores = new StringBuilder();
        Cursor cursor = database.query(
                HighScoresDBHelper.TABLE_NAME,
                null, null, null, null, null,
                HighScoresDBHelper.COLUMN_SCORE + " DESC",
                "5"
        );

        int rank = 1;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(HighScoresDBHelper.COLUMN_NAME));
            int score = cursor.getInt(cursor.getColumnIndex(HighScoresDBHelper.COLUMN_SCORE));
            highScores.append(rank).append(". ").append(name).append(" - ").append(score).append("\n");
            rank++;
        }
        cursor.close();
        return highScores.toString();
    }
}