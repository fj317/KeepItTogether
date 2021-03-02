package com.example.keepittogether_login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Switch chore_bill_switch = (Switch) findViewById(R.id.chore_bill_switch);
        chore_bill_switch.setTextOn("Chore");
        chore_bill_switch.setTextOff("Bill");

    }


}