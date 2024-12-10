package com.example.finalgameproject;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ArrayList<String> colorSequence;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        colorSequence = getIntent().getStringArrayListExtra("colorSequence");
        currentIndex = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        String detectedDirection = "";
        if (x > 5) detectedDirection = "West";
        else if (x < -5) detectedDirection = "East";
        else if (y > 5) detectedDirection = "North";
        else if (y < -5) detectedDirection = "South";

        if (!detectedDirection.isEmpty()) {
            checkSequence(detectedDirection);
        }
    }

    private void checkSequence(String direction) {
        if (direction.equals(colorSequence.get(currentIndex))) {
            currentIndex++;
            if (currentIndex == colorSequence.size()) {
                Toast.makeText(this, "Round Complete!", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(PlayActivity.this, SequenceActivity.class);
                intent.putExtra("nextSequenceSize", colorSequence.size() + 2);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PlayActivity.this, GameOverActivity.class);
            intent.putExtra("finalScore", colorSequence.size() - 4);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}