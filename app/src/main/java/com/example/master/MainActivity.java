package com.example.master;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button speak;
    TextView tv;
    ListView options;
    ArrayList<String> results;
    Chronometer chronometer;
    boolean checck;

    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speak = (Button) findViewById(R.id.bSpeak);
        options = (ListView) findViewById(R.id.lvOptions);
        tv = (TextView) findViewById(R.id.time);
        chronometer = (Chronometer) findViewById(R.id.chron);

        speak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
                start();
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,15);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say");
                startActivityForResult(i,1010);

            }
        });
        if (savedInstanceState != null) {
            results = savedInstanceState.getStringArrayList("results");


            if (results != null) {
                options.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, results));
            }

        }
    }

    protected void start(){
        if(!checck){
            chronometer.start();
            checck = true;
        }
    }

    protected void stop(){
        if(checck){
            chronometer.stop();
            checck = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results.contains("close"))
            {
                finish();
            }
            options.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, (results)));
        }
        stop();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("results", results);
    }
}