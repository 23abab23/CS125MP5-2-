package com.example.mp5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    /**
     * Unique gallery request code thats created when the user accesses the image gallery
     */
    public static final int GALLERY_REQUEST_CODE = 209;

    /**
     * Function thats called when activity is started, sets up UI and calls requisite functions
     * @param savedInstanceState saved state from the previously terminated instance of this activity (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Button loadImage = findViewById(R.id.loadButton); // Loads in the load image button
        loadImage.setOnClickListener(v -> { // Creates a handler for the button
            getImage(); // Calls getImage() if the button is clicked
        });
    }

    /**
     * Creates and populates an intent, then starts activity to let user access an image from gallery
     */
    public void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK); // Creates intent for picking an image
        intent.setType("image/*"); // Sets the type of file to be picked
        String[] mime = {"image/jpeg", "image/png"}; // Creates an extra to pick only jpeg and png
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mime); // Populates extra with image types
        startActivityForResult(intent,GALLERY_REQUEST_CODE); // Starts activity
    }

    /**
     * Overrides the defaule onActivityResult, when image is selected, the URL is placed in an intent
     * as an extra and the MainActivity is launched from within
     * @param requestCode Unique request code for gallery access, 209 if user accesses the gallery
     * @param resultCode Code returned if gallery access was successful, only care about OK code
     * @param data Image data contained in an intent, returned by gallery activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) // Only runs if user selects an image
            switch (requestCode) {
                case GALLERY_REQUEST_CODE: // For the case where the user accesses the gallery
                    Uri selectedImage = data.getData(); // Gets selected image URI
                    // Gets the file path of the selected image
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Populates an intent to start the main activity, image path loaded as an exra
                    Intent intent = new Intent(this, MainActivity.class); // Creates an empty GameActivity intent
                    intent.putExtra("game", imgDecodableString); // Game ID is stored as an extra
                    startActivity(intent); // Activity is started with intent
                    finish(); // Finishes current activity
            }
    }
}
