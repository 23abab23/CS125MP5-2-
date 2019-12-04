package com.example.mp5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    /** Bitmap used to hold an image such that it can be modified by other activities */
    public static Bitmap modImg;

    /**
     * Called when activity starts, loads image and 3 modification buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Save Button
        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener(v -> { // Add Image Saving section to this
            // Store Image in filesystem
            startActivity(new Intent(this, LaunchActivity.class));
            finish();
        });
        // Restart Button
        Button restart = findViewById(R.id.restartButton); // Grabs Restart button
        restart.setOnClickListener(v -> { // Creates handler
            startActivity(new Intent(this, LaunchActivity.class));
            finish(); // If clicked, user is sent back to launch screen to choose new image
        });
        // Random Effect Button
        Button randomButton = findViewById(R.id.randomButton);
        randomButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LaunchActivity.class));
            finish();
            randomize();
        });
        Intent intent = getIntent(); // Gets intent used to start activity
        String filePath = intent.getStringExtra("path"); // Extracts the filepath as String
        ImageView userImg = findViewById(R.id.imageView2); // Creates a reference for the blank imageview
        loadImage(filePath, userImg);
    }

    /**
     * Called to load the user selected image into the imageView for the first time
     * @param filePath String filepath of the user selected image
     * @param userImg Reference to the imageView in which the image is placed
     */
    public void loadImage(String filePath, ImageView userImg) {
        Bitmap userBtmp = BitmapFactory.decodeFile(filePath);
        modImg = userBtmp;
        userImg.setImageBitmap(userBtmp);

    }
    public void randomize() {
        return; // fix this later
    }
}
