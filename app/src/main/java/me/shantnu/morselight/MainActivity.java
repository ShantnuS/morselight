package me.shantnu.morselight;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/*
* MorseLight
* Version 1.2
* Created by Shantnu Singh on 11/07/2017
*/

public class MainActivity extends AppCompatActivity {

    //Global variables
    private int timeUnit = 300;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkFlashAvailable();
        requestPermission();
    } //Do this on create...

    public void requestPermission(){
        System.err.println("Requesting camera permission");
        Context context = getApplicationContext();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        }
    } //Request camera access permission

    public void checkFlashAvailable(){
        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Compatiblilty Error!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
        }
        else{
            System.err.println("Flash is available!");
        }
    } //Check if device has a flashlight

    public void oneUnitSleep(){
        try{
            Thread.sleep(timeUnit);
        }
        catch (Exception e){
            e.printStackTrace();
            displayToast("Could not sleep!");
        }
    } //Sleep for one time unit

    public void threeUnitSleep(){
        //a three unit sleep is needed after each letter.
        int threeTimeUnit = (timeUnit * 3);

        try{
            Thread.sleep(threeTimeUnit);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    } //Sleep for 3 time units

    public void sevenUnitSleep(){
        //A seven unit sleep is needed after each word. I.e. a space = 7 unit sleep.
        int sevenTimeUnit = (timeUnit * 7);

        try{
            Thread.sleep(sevenTimeUnit);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    } //Sleep for 7 time units

    public void dot(){
        //The duration of a dot should be equal to 1 time unit
        Camera cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();

        oneUnitSleep();
        addMorseCode(".");

        //cam.stopPreview();
        cam.release();

        oneUnitSleep();

    } //Turn on/off flashlight for DOT

    public void dash(){
        //The duration of a dash is equivalent to 3 time units.
        Camera cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();

        addMorseCode("-");
        threeUnitSleep();

        //cam.stopPreview();
        cam.release();

        oneUnitSleep();

    } //Turn on/off flashlight for DASH

    public void buttonAction(View view){
        /* This had to be run on a separate thread because of the skipped frames caused by running
        * on the main thread, which was a problem since the text view didn't update.
        */
        requestPermission();
        hideKeyboard(view);

        new Thread() {
            public void run() {
                if(!isRunning) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                        displayToast("Camera permission was not granted!");
                    }
                    else{
                        isRunning = true;
                        parseText();
                    }
                }
                else{
                    displayToast("Already Running!");
                }
            }
        }.start();
    } //Called when button is pressed

    public void parseText() {

        String textToParse = getEditTextString();
        ArrayList<String> textArray = new ArrayList<>(Arrays.asList(textToParse.split("")));

        clearMorseCode();

        for (String letter : textArray) {
            setTextViewText(letter);
            morseIt(letter);
        }

        setTextViewText("Done!");
        isRunning = false;
    } //Pass text from the field to morseIt one at a time

    public void hideKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    } //Hide the soft-keyboard

    public void displayToast(final String message){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, message, duration);
                toast.show();
            }
        });
    } //Display a toast message on screen

    public String getEditTextString(){
        //returns the text from the text field "editText"...
        EditText editText = (EditText) findViewById(R.id.editText);
        return editText.getText().toString();
    } //Get the string from the text-field

    public void setTextViewText(final String text) {
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                System.err.println("Setting textView text: " + text);
                TextView textview = (TextView) findViewById(R.id.textView);
                textview.setText(text);
            }
        });
    } //Set the view on the bottom text view

    public void addMorseCode(final String code) {
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                System.err.println("Adding to textview: " + code);
                TextView textview = (TextView) findViewById(R.id.textView2);
                textview.append(code);
            }
        });
    } //Append text on the morse code text view

    public void clearMorseCode(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                System.err.println("Clearing morse textview");
                TextView textview = (TextView) findViewById(R.id.textView2);
                textview.setText("");
            }
        });
    } //Clear the morse code text view

    public void morseIt(String letter) {

        letter = letter.toUpperCase();
        System.err.println("Flashing the letter: " + letter);

        switch (letter) {
            case "A":
                dot();
                dash();
                threeUnitSleep();
                break;
            case "B":
                dash();
                dot();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "C":
                dash();
                dot();
                dash();
                dot();
                threeUnitSleep();
                break;
            case "D":
                dash();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "E":
                dot();
                threeUnitSleep();
                break;
            case "F":
                dot();
                dot();
                dash();
                dot();
                threeUnitSleep();
                break;
            case "G":
                dash();
                dash();
                dot();
                threeUnitSleep();
                break;
            case "H":
                dot();
                dot();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "I":
                dot();
                dot();
                threeUnitSleep();
                break;
            case "J":
                dot();
                dash();
                dash();
                dash();
                threeUnitSleep();
                break;
            case "K":
                dash();
                dot();
                dash();
                threeUnitSleep();
                break;
            case "L":
                dot();
                dash();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "M":
                dash();
                dash();
                threeUnitSleep();
                break;
            case "N":
                dash();
                dot();
                threeUnitSleep();
                break;
            case "O":
                dash();
                dash();
                dash();
                threeUnitSleep();
                break;
            case "P":
                dot();
                dash();
                dash();
                dot();
                threeUnitSleep();
                break;
            case "Q":
                dash();
                dash();
                dot();
                dash();
                threeUnitSleep();
                break;
            case "R":
                dot();
                dash();
                dot();
                threeUnitSleep();
                break;
            case "S":
                dot();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "T":
                dash();
                threeUnitSleep();
                break;
            case "U":
                dot();
                dot();
                dash();
                threeUnitSleep();
                break;
            case "V":
                dot();
                dot();
                dot();
                dash();
                threeUnitSleep();
                break;
            case "W":
                dot();
                dash();
                dash();
                threeUnitSleep();
                break;
            case "X":
                dash();
                dot();
                dot();
                dash();
                threeUnitSleep();
                break;
            case "Y":
                dash();
                dot();
                dash();
                dash();
                threeUnitSleep();
                break;
            case "Z":
                dash();
                dash();
                dot();
                dot();
                threeUnitSleep();
                break;
            case " ":
                addMorseCode("/");
                sevenUnitSleep();
                break;
            case "1":
                dot();
                dash();
                dash();
                dash();
                dash();
                threeUnitSleep();
                break;
            case "2":
                dot();
                dot();
                dash();
                dash();
                dash();
                threeUnitSleep();
                break;
            case "3":
                dot();
                dot();
                dot();
                dash();
                dash();
                threeUnitSleep();
                break;
            case "4":
                dot();
                dot();
                dot();
                dot();
                dash();
                threeUnitSleep();
                break;
            case "5":
                dot();
                dot();
                dot();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "6":
                dash();
                dot();
                dot();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "7":
                dash();
                dash();
                dot();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "8":
                dash();
                dash();
                dash();
                dot();
                dot();
                threeUnitSleep();
                break;
            case "9":
                dash();
                dash();
                dash();
                dash();
                dot();
                threeUnitSleep();
                break;
            case "0":
                dash();
                dash();
                dash();
                dash();
                dash();
                threeUnitSleep();
                break;
        }
        addMorseCode(" ");
    } //Call sequence of DOT/DASH depending on the letter

}

// ********** LEGACY CODE **********

    /*
    //Might be useful later...
    public void setTimeUnit(int timeUnit){
        this.timeUnit = timeUnit;
    }
    */

    /*
    public void sosTest(View view){

        //This was a test of the dot and dash methods being called consecutively
        //It is not very efficient as it runs on the main thread, and therefore
        //can cause the device to lag and skip frames. Hence, the parseView method
        //was called from within a separate thread.


        dot();
        dot();
        dot();

        threeUnitSleep();

        dash();
        dash();
        dash();

        threeUnitSleep();

        dot();
        dot();
        dot();

    }
    */
