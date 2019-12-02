package com.example.mp5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener(v -> { // Add Image Saving section to this
            // Store Image in filesystem
            startActivity(new Intent(this, LaunchActivity.class));
            finish();
        });
        Button restart = findViewById(R.id.restartButton);
        restart.setOnClickListener(v -> {
            startActivity(new Intent(this, LaunchActivity.class));
            finish();
        });
    }
}
