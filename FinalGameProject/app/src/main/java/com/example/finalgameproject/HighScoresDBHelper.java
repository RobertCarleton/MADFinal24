package com.example.finalgameproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighScoresDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HighScores.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "high_scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    public HighScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SCORE + " INTEGER);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertHighScore(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SCORE, score);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getTopFiveScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        /*return db.query(
                TABLE_NAME,
                new String[]{COLUMN_NAME, COLUMN_SCORE},
                null, null, null, null,
                COLUMN_SCORE + " DESC",
                "5"
        );*/
        return db.rawQuery(
                "SELECT id AS _id, name, score FROM high_scores ORDER BY score DESC LIMIT 5",
                null
        );
    }

    public void clearScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    // Check if the given score qualifies as a high score (top 5)
    public boolean isHighScore(int score) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_SCORE},
                null, null, null, null,
                COLUMN_SCORE + " DESC",
                "5"
        );

        if (cursor.moveToLast()) { // Move to the lowest score in the top 5
            int lowestHighScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
            cursor.close();
            if (cursor.getCount() < 5 || score > lowestHighScore) {
                return true; // Always qualifies if less than 5 scores are stored
            }

        }

        cursor.close();
        return true; // No scores in the DB yet, so it's a high score
    }

    // Insert a score only if it qualifies as a high score
    public void insertScore(String name, int score) {
        if (isHighScore(score)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_SCORE, score);
            db.insert(TABLE_NAME, null, values);

            // Remove scores beyond the top 5
            Cursor cursor = db.query(
                    TABLE_NAME,
                    new String[]{COLUMN_ID},
                    null, null, null, null,
                    COLUMN_SCORE + " DESC",
                    "5, -1" // Skip the top 5
            );

            if (cursor.moveToNext()) { // If there are extra scores
                int idToRemove = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(idToRemove)});
            }

            cursor.close();
            db.close();
        }
    }


}
