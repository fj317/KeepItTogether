package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Add extends AppCompatActivity {
    EditText et_add_Name, et_add_Description , et_add_repeat , et_add_cost;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        et_add_Name = findViewById(R.id.et_add_name);
        et_add_Description = findViewById(R.id.et_add_description);
        et_add_repeat = findViewById(R.id.et_add_repeat);
        et_add_cost = findViewById(R.id.et_add_cost);
        aSwitch = (android.widget.Switch) findViewById(R.id.chore_bill_switch);
        et_add_cost.setVisibility(View.INVISIBLE);


        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    et_add_cost.setVisibility(View.VISIBLE);
                }else{
                    et_add_cost.setVisibility(View.INVISIBLE);
                }
            }
        });
    }




}