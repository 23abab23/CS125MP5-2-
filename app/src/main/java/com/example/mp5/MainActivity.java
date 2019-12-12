package com.example.mp5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import io.uuddlrlrba.closepixelate.Pixelate;
import io.uuddlrlrba.closepixelate.PixelateLayer;

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
        setTitle("Fry Fry Fry"); // Sets action bar title
        //Save Button
        Button save = findViewById(R.id.saveButton); // Grabs Save button
        save.setOnClickListener(v -> { // Creates a handler
            try { // Try catch to encompass the save function
                saveImage(); // Calls save function
            } catch (IOException e) { // Handles IO errors that can be generated
                e.printStackTrace();
            }
            startActivity(new Intent(this, LaunchActivity.class));
            finish(); // Once save completes the activity is closed and the user is sent back to home
        });
        // Restart Button
        Button restart = findViewById(R.id.restartButton); // Grabs Restart button
        restart.setOnClickListener(v -> { // Creates handler
            startActivity(new Intent(this, LaunchActivity.class));
            finish(); // If clicked, user is sent back to launch screen to choose new image
        });
        // Random Effect Button
        Button randomButton = findViewById(R.id.randomButton); //Grabs random effect button
        randomButton.setOnClickListener(v -> { // Creates handler
            randomize(); // Calls randomize upon buttonpress
        });
        Intent intent = getIntent(); // Gets intent used to start activity
        String filePath = intent.getStringExtra("path"); // Extracts the filepath as String
        ImageView userImg = findViewById(R.id.imageView2); // Creates a reference for the blank imageview
        loadImage(filePath, userImg); // Loads image into the imageView
    }

    /**
     * Called to load the user selected image into the imageView for the first time
     * @param filePath String filepath of the user selected image
     * @param userImg Reference to the imageView in which the image is placed
     */
    public void loadImage(String filePath, ImageView userImg) {
        Bitmap userBtmp = BitmapFactory.decodeFile(filePath); // Uses passed file path to generate a bitmap
        // Stores bitmap in a class reference for future use, also scales it to a smaller size
        modImg = Bitmap.createScaledBitmap(userBtmp, 411, 544, false);
        userImg.setImageBitmap(modImg); // Sets imageView
    }

    /**
     * Called upon button press of the randomize button, randomly fries image using helper functions
     */
    public void randomize() {
        Random rand = new Random(); // Creates a new random generator
        int intF = rand.nextInt(100) + 1; // Creates a 1-100 random int
        double doubleF = rand.nextDouble()*100; // Creates a 1-100 random double
        addContrast(doubleF); // Calls contrast modifer, uses random factor
        addSaturation(intF); // Calls saturation modifier, uses random factor
        addTint(); // Calls tint adder, constant tint
        addGrain(intF/10); // Calls grain added, uses scaled random
        ImageView userImg = findViewById(R.id.imageView2); // Creates a reference for the blank imageview
        userImg.setImageBitmap(modImg); // Updates image
    }

    /**
     * Adds a reddish/orangish tint to the photo using a Paint object
     */
    public void addTint() {
        Paint tint = new Paint(); // Creates paint object
        tint.setColorFilter(new PorterDuffColorFilter(0x66FF4000, PorterDuff.Mode.SRC_IN)); // Creates a orangish filter
        Canvas canvas = new Canvas(modImg); // Creates a canvas to apply the tint with
        canvas.drawBitmap(modImg, 0, 0, tint); // Directly applies tint to class image with canvas
    }
    /**
     * Uses the android-close-pixelate library to apply grain to the photo
     * @param cGrain Integer value used to determine the amount of grain
     */
    public void addGrain(int cGrain) {
        int grain = (modImg.getHeight())/(cGrain*9); // Function scales grain size with image size
        Bitmap grained = Pixelate.fromBitmap( // Uses the fromBitmap constructor to construct a Bitmap
                modImg, // Reference original image
                new PixelateLayer.Builder(PixelateLayer.Shape.Square) // Pixelation layer
                        .setResolution(grain) // Sets the amount of grain
                        .build()); // Constructs the image
        modImg = grained; // Sets the class image to the altered product
    }
    /**
     * Adds a certain amount of contrast to the bitmap image
     * Logic was taken from the thread below
     * https://stackoverflow.com/questions/12891520/how-to-programmatically-change-contrast-of-a-bitmap-in-android
     * @param factor Double representing the relative contrast to add
     */
    public void addContrast(double factor) {
        double cVal = 50 * (factor/100); // Assigns cVal, scales with factor
        int width = modImg.getWidth(); // Fetches image dimensions
        int height = modImg.getHeight();
        Bitmap fImg = Bitmap.createBitmap(width, height, modImg.getConfig()); // Creates an output image
        int pixel; // Creates empty references to hold pixel data as contrast is modified
        int G, R, B, A;
        double contrast = Math.pow((100 + cVal) / 100, 2); // Calculates contrast from input cVal
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) { // Loops through the bitmap to alter every pixel
                pixel = modImg.getPixel(x, y); // Gets pixel color
                A = Color.alpha(pixel); // Apply filter contrast for every channel R, G, B
                // Red pixel modifications
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }
                // Green pixel modifications
                G = Color.green(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }
                // Blue pixel modifications
                B = Color.blue(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }
                // Set new pixel colors to output bitmap
                fImg.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        modImg = fImg; // Set class bitmap to output bitmap
    }

    /**
     * Adds saturation to the image
     * https://stackoverflow.com/questions/12711376/how-to-adjust-hue-saturation-of-a-bitmap provided logic
     * @param factor Random scaling factor used to compute all dependent saturation values
     */
    public void addSaturation(int factor) {
        double saturationDelta = 0.1 * (factor/100); // Value controls saturation, scales with factor
        float hue = 1/2 * (factor/100); // Value controls hue, scales with factor
        double valueDelta = 0.1 * (factor/100); // Value controls delta, scales with factor
        float[] hsv = new float[3]; // Creates an array to hold the hue, saturation, value data
        hsv[0] = hue; // Assigns values to hsv
        hsv[1] += saturationDelta;
        hsv[2] += valueDelta;
        for (int x = 0; x < modImg.getWidth(); x++) {
            for (int y = 0; y < modImg.getHeight(); y++) { // Loops through each pixel
                int color = modImg.getPixel(x, y); // Grabs ARGB data
                Color.colorToHSV(color, hsv); // Populates an object with color data and hsv
                int newColor = Color.HSVToColor(Color.alpha(color), hsv); //Applies color shift
                modImg.setPixel(x, y, newColor); // Modifies class image with color shift
            }
        }
    }

    /**
     * Called to save the image that is displayed on the user's phone (modImg)
     * https://stackoverflow.com/questions/649154/save-bitmap-to-location provided code logic
     * @throws IOException Throws this exception in case of a bad filetype for save
     */
    public void saveImage() throws IOException {
        File f = Environment.getExternalStorageDirectory(); // Opens the phone filesystem
        File dir = new File(f.getAbsolutePath() + "/image/"); // Creates a folder in filesystem
        dir.mkdir();
        File file = new File(dir, System.currentTimeMillis() +".jpg"); // Creates save file as jpg
        FileOutputStream out = new FileOutputStream(file); // Creates file to save to /image/ uses system clock for filename
        modImg.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compressess the bitmap to save space
        Toast.makeText(getApplicationContext(), "Image Save to photo",Toast.LENGTH_SHORT).show();
        out.flush(); // Shows the user a toast and flushes/closes the filestream
        out.close();
    }
}
