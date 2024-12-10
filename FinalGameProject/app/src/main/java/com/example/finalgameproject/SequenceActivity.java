package com.example.finalgameproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

public class SequenceActivity extends AppCompatActivity {
    private ArrayList<String> colorSequence;
    private TextView sequenceDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);

        sequenceDisplay = findViewById(R.id.sequence_display);
        colorSequence = new ArrayList<>();
        generateSequence(4); // Initial sequence size of 4
        displaySequence();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SequenceActivity.this, PlayActivity.class);
            intent.putStringArrayListExtra("colorSequence", colorSequence);
            startActivity(intent);
            finish();
        }, 3000); // Display sequence for 3 seconds
    }

    private void generateSequence(int size) {
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        for (int i = 0; i < size; i++) {
            colorSequence.add(colors[(int) (Math.random() * colors.length)]);
        }
    }

    private void displaySequence() {
        sequenceDisplay.setText(colorSequence.toString().replace(",", " ").replace("[", "").replace("]", ""));
    }
}