package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Graphs extends AppCompatActivity {
    int days;
    SeekBar seekBar;
    TextView textDays;
    // reference https://stackoverflow.com/questions/8629535/implementing-a-slider-seekbar-in-android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        // set a change listener on the SeekBar
        seekBar = findViewById(R.id.seekBarDays);
        textDays = findViewById(R.id.textDays);
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
            // call graph making bit here
        }
    };
}