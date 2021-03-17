package com.example.keep_it_together;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.time.LocalDate;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Graphs extends AppCompatActivity {
    int days;
    SeekBar seekBar;
    TextView textDays;
    Switch graphTypeSwitch;
    // reference https://stackoverflow.com/questions/8629535/implementing-a-slider-seekbar-in-android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        // set a change listener on the SeekBar
        seekBar = findViewById(R.id.seekBarDays);
        textDays = findViewById(R.id.textDays);
        graphTypeSwitch = findViewById(R.id.chore_transaction_switch);
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String houseID = preferences.getString("houseID", "");

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            textDays.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            days = seekBar.getProgress();
            if (graphTypeSwitch.isChecked()) {
                // call transaction making graph code
            } else {
                // call chore making graph code
            }

        }
    };

    private void drawGraph(Dictionary data) {

    }

}