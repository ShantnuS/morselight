package me.shantnu.morselight;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Global variables
    private int timeUnit = 300;

    //UI elements
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void oneUnitSleep(){
        try{
            Thread.sleep(timeUnit);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void threeUnitSleep(){
        int threeTimeUnit = (timeUnit * 3);

        try{
            Thread.sleep(threeTimeUnit);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sevenUnitSleep(){
        int sevenTimeUnit = (timeUnit * 7);

        try{
            Thread.sleep(sevenTimeUnit);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dot(){
        //The duration of a dot should be equal to 1 time unit
        Camera cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();

        oneUnitSleep();

        cam.stopPreview();
        cam.release();

        oneUnitSleep();

    }

    public void dash(){
        //The duration of a dash is equivalent to 3 time units.

        Camera cam = Camera.open();
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();

        threeUnitSleep();

        cam.stopPreview();
        cam.release();

        oneUnitSleep();

    }

    public void sosTest(View view){
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

    public void parseText(View view){

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);

        String textToParse = editText.getText().toString();
        ArrayList<String> textArray = new ArrayList<String>(Arrays.asList(textToParse.split("")));

        for (String i : textArray){
            morseIt(i);
        }

        textView.setText("Done!");
    }

    public void morseIt(String letter){
        letter = letter.toUpperCase();

        switch (letter){
            case "A": dot(); dash(); threeUnitSleep(); break;
            case "B": dash(); dot(); dot(); dot(); threeUnitSleep(); break;
            case "C": dash(); dot(); dash(); dot(); threeUnitSleep(); break;
            case "D": dash(); dot(); dot(); threeUnitSleep(); break;
            case "E": dot(); threeUnitSleep(); break;
            case "F": dot(); dot(); dash(); dot(); threeUnitSleep(); break;
            case "G": dash(); dash(); dot(); threeUnitSleep(); break;
            case "H": dot(); dot(); dot(); dot(); threeUnitSleep(); break;
            case "I": dot(); dot(); threeUnitSleep(); break;
            case "J": dot(); dash(); dash(); dash(); threeUnitSleep(); break;
            case "K": dash(); dot(); dash(); threeUnitSleep(); break;
            case "L": dot(); dash(); dot(); dot(); threeUnitSleep(); break;
            case "M": dash(); dash(); threeUnitSleep(); break;
            case "N": dash(); dot(); threeUnitSleep(); break;
            case "O": dash(); dash(); dash(); threeUnitSleep(); break;
            case "P": dot(); dash(); dash(); dot(); threeUnitSleep(); break;
            case "Q": dash(); dash(); dot(); dash(); threeUnitSleep(); break;
            case "R": dot(); dash(); dot(); threeUnitSleep(); break;
            case "S": dot(); dot(); dot(); threeUnitSleep(); break;
            case "T": dash(); threeUnitSleep(); break;
            case "U": dot(); dot(); dash(); threeUnitSleep(); break;
            case "V": dot(); dot(); dot(); dash(); threeUnitSleep();  break;
            case "W": dot(); dash(); dash(); threeUnitSleep(); break;
            case "X": dash(); dot(); dot(); dash(); threeUnitSleep(); break;
            case "Y": dash(); dot(); dash(); dash(); threeUnitSleep(); break;
            case "Z": dash(); dash(); dot(); dot(); threeUnitSleep(); break;
            case " ": sevenUnitSleep(); break;
            case "1": dot(); dash(); dash(); dash(); dash(); threeUnitSleep(); break;
            case "2": dot(); dot(); dash(); dash(); dash(); threeUnitSleep(); break;
            case "3": dot(); dot(); dot(); dash(); dash(); threeUnitSleep(); break;
            case "4": dot(); dot(); dot(); dot(); dash(); threeUnitSleep(); break;
            case "5": dot(); dot(); dot(); dot(); dot(); threeUnitSleep(); break;
            case "6": dash(); dot(); dot(); dot(); dot(); threeUnitSleep(); break;
            case "7": dash(); dash(); dot(); dot(); dot(); threeUnitSleep(); break;
            case "8": dash(); dash(); dash(); dot(); dot(); threeUnitSleep(); break;
            case "9": dash(); dash(); dash(); dash(); dot(); threeUnitSleep(); break;
            case "0": dash(); dash(); dash(); dash(); dash(); threeUnitSleep(); break;
        }
    }
}
