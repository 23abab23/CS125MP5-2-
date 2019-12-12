package com.example.mp5;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    /** Unique gallery request code thats created when the user accesses the image gallery */
    public static final int GALLERY_REQUEST_CODE = 209;

    /** Unique permission request identifier for the app, read perms */
    public static final int STORAGE_REQUEST_CODE = 314;

    /**
     * Function thats called when activity is started, sets up UI and calls requisite functions
     * @param savedInstanceState saved state from the previously terminated instance of this activity (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setTitle("Geoff's Frying Fiesta"); // Sets action bar title
        ImageView splash = findViewById(R.id.splash); // Calls splash imageView
        String uri = "@drawable/splashart"; // Grabs file uri
        int imgR = getResources().getIdentifier(uri, null, getPackageName()); // Gets int resource
        Drawable res = getResources().getDrawable(imgR); // Uses int to create a drawable
        splash.setImageDrawable(res); // Sets splash art with drawable
        Button loadImage = findViewById(R.id.loadButton); // Loads in the load image button
        loadImage.setOnClickListener(v -> { // Creates a handler for the button
            getImage(); // Calls getImage() if the button is clicked
        });
        checkPerms(); // Calls checkPerms to check permissions
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
                    intent.putExtra("path", imgDecodableString); // Filepath is stored as an extra
                    startActivity(intent); // Activity is started with intent
                    finish(); // Finishes current activity
            }
    }

    /**
     * Overrides the default permission request handler
     * @param requestCode Request specific integer code
     * @param permissions Array of requested permissions
     * @param grantResults Return array of which perms were granted and denied
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 // Checks if any results were granted
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Does nothing if perms are granted
                } else {
                    finish(); // Closes app if perms are denied, perms are required for functionality
                }
                return;
            }
        }
    }

    /**
     * Checks for read/write permissions when called, prompts user if perms aren't given
     */
    public void checkPerms() {
        List<String> permsNeeded = new ArrayList<>(); // Initializes an empty array to hold requested perms
        // Checks if the app has read external storage permissions
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE); // If no perms, then request is added
        }
        // Checks if the app has write external storage permissions
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE); // If no perms, then request is added
        }
        if (permsNeeded.size() != 0) { // If perms are needed then the request is called
            requestPermissions(permsNeeded.toArray(new String[permsNeeded.size()]),
                    STORAGE_REQUEST_CODE); // Uses a result activity to call for permissions
        }
    }
}
