package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class Graphs extends AppCompatActivity {
    int day, month, year, hour, minute;
    Button dateSelector;

    // https://www.truiton.com/2015/04/android-date-range-picker/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        dateSelector = findViewById(R.id.bt_date_selector);

        dateSelector.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
               // DatePickerDialog datePickerDialog = new DatePickerDialog(Graphs.this, Graphs.this, year, month, day);
                //datePickerDialog.show();
            }
        });

    }
}