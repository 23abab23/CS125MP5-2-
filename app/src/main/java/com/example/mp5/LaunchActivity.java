package com.example.mp5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Button loadImage = findViewById(R.id.loadButton);
        loadImage.setOnClickListener(v -> { // Add Image Loading section to this
            // Store Image in intent
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
