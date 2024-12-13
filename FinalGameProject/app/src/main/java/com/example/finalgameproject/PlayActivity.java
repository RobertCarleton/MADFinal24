package com.example.finalgameproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float[] accelerometerValues;
    private List<String> sequence;
    private int currentIndex = 0;
    private int score;
    private boolean isProcessing = false;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_play);

        score = getIntent().getIntExtra("score", 0);
        sequence = getIntent().getStringArrayListExtra("sequence");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        scoreTextView = findViewById(R.id.scoreText);
        updateScoreDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (isProcessing) {
                return;
            }

            accelerometerValues = event.values;
            String direction = detectTilt();
            if (direction == null) {
                return; // Skip if no tilt
            }

            Log.d("PlayActivity", "Detected direction: " + direction);
            Log.d("PlayActivity", "Expected direction: " + sequence.get(currentIndex));

            if (direction.equals(sequence.get(currentIndex))) {
                currentIndex++;
                // If the player has completed the entire sequence
                if (currentIndex == sequence.size()) {
                    // Award points for completing the sequence
                    score += sequence.size();
                    updateScoreDisplay();  // Update score display

                    // Pass sequence to the next activity
                    Intent intent = new Intent(PlayActivity.this, SequenceActivity.class);
                    intent.putStringArrayListExtra("sequence", (ArrayList<String>) sequence); // Pass updated sequence
                    intent.putExtra("score", score); // Pass updated score
                    startActivity(intent);
                    finish(); // Finish this activity
                }
            } else {
                // If the player fails, go to the game over screen
                Intent intent = new Intent(PlayActivity.this, GameOverActivity.class);
                intent.putExtra("FINAL_SCORE", score); // Pass score to GameOverActivity
                startActivity(intent);
                finish(); // Ensure PlayActivity is removed
            }

            // Set the buffer to prevent rapid sensor events
            isProcessing = true;
            new android.os.Handler().postDelayed(() -> isProcessing = false, 500);
        }
    }

    private String detectTilt() {
        if (accelerometerValues[0] > 5) return "green";  // Tilt down
        if (accelerometerValues[0] < -5) return "purple";  // Tilt up
        if (accelerometerValues[1] > 5) return "blue"; // Tilt right
        if (accelerometerValues[1] < -5) return "red"; // Tilt left
        return null;
    }

    private void extendSequence(int length) {
        String[] colors = {"purple", "green", "red", "blue"};
        Random random = new Random();

        // Add length new colors
        for (int i = 0; i < length; i++) {
            sequence.add(colors[random.nextInt(colors.length)]); // Add random color to the sequence
        }
    }

    private void updateScoreDisplay() {
        scoreTextView.setText("Score: " + score);  // Update the TextView with the current score
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
