package com.example.keep_it_together;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.time.LocalDate;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Graphs extends AppCompatActivity {
    int days;
    SeekBar seekBar;
    TextView textDays;
    Switch graphTypeSwitch;
    String houseID;
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
        houseID = preferences.getString("houseID", "");
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
            GraphsFunctions graphsFunctions = new GraphsFunctions();
            graphsFunctions.getClients(houseID);
            days = seekBar.getProgress();
            if (graphTypeSwitch.isChecked()) {
                drawGraph(graphsFunctions.transactionsGraphsData(days));
                // call transaction making graph code
            } else {
                drawGraph(graphsFunctions.choresGraphsData(days));
                // call chore making graph code
            }

        }
    };

    private void drawGraph(Map<String, Float> data) {
        List<SliceValue> pieData = new ArrayList<>();
        PieChartView pieChartView = findViewById(R.id.chart);
        for(String key : data.keySet()){
            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            int randomColor = Color.rgb(r,g,b);
            pieData.add(new SliceValue(data.get(key), randomColor).setLabel(key));
        }
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartView.setPieChartData(pieChartData);
    }

}