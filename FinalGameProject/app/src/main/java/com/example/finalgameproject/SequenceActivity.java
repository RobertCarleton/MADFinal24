package com.example.finalgameproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SequenceActivity extends AppCompatActivity {
    private String[] colors = {"purple", "green", "red", "blue"};
    private ImageView purpleC, greenC, redC, blueC;
    private List<String> currentSequence = new ArrayList<>();
    private int sequenceLength = 4;
    private int currentIndex;
    private Handler Handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        purpleC = findViewById(R.id.purple_circle);
        greenC = findViewById(R.id.green_circle);
        redC = findViewById(R.id.red_circle);
        blueC = findViewById(R.id.blue_circle);

        ArrayList<String> recievedSequence = getIntent().getStringArrayListExtra("sequence");

        //Generation
        if(recievedSequence != null && !recievedSequence.isEmpty()) {
            currentSequence = recievedSequence;
            extendSequence(2);
        }
        else{
            generateSequence(sequenceLength); // Initial sequence size of 4

        }
        displaySequence();

        Handler.postDelayed(() -> {
            Intent intent = new Intent(SequenceActivity.this, PlayActivity.class);
            intent.putExtra("score", getIntent().getIntExtra("score", 0));
            intent.putStringArrayListExtra("sequence", new ArrayList<>(currentSequence));
            startActivity(intent);
            finish();
        }, (currentSequence.size() + 1) * 1000);
    }

    private void generateSequence(int length) {
        Random random = new Random();
        currentSequence.clear();
        for (int i = 0; i < length; i++){
            currentSequence.add(colors[random.nextInt(colors.length)]);
        }
    }

    private void extendSequence(int length)
    {
        Random random = new Random();
        for (int i = 0; i < length; i++)
        {
            currentSequence.add(colors[random.nextInt(colors.length)]);
        }
    }

    private void displaySequence() {
        for (int i = 0; i < currentSequence.size(); i++) {
            int finalI = i;
            Handler.postDelayed(() -> {
                highlightButton(currentSequence.get(finalI)); // Highlight current color
            }, i * 1000); // Delay for each color based on its index (1 second per color)

            // Reset the colors after 1 second
            Handler.postDelayed(() -> {
                resetButtonColors();
            }, (i + 1) * 1000); // Reset after the color is displayed
        }
    }


    private void highlightButton(String color)
    {
        resetButtonColors();
        switch(color){
            case "purple":
                purpleC.setAlpha(0.5f);
            break;
            case "green":
                greenC.setAlpha(0.5f);
                break;
            case "red":
                redC.setAlpha(0.5f);
                break;
            case "blue":
                blueC.setAlpha(0.5f);
                break;
        }
    }

    private void resetButtonColors(){
        purpleC.setAlpha(1.0f);
        greenC.setAlpha(1.0f);
        redC.setAlpha(1.0f);
        blueC.setAlpha(1.0f);
    }
}