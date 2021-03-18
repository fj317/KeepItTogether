package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateHouse extends AppCompatActivity {
    Button btSubmit;
    EditText etAddress, etPostCode, etNumberResidents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_house);
        btSubmit = findViewById(R.id.bt_submit);
        etAddress = findViewById(R.id.et_address);
        etPostCode = findViewById(R.id.et_postcode);
        etNumberResidents = findViewById(R.id.et_number_people);

        btSubmit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = etAddress.getText().toString();
                String postCode = etPostCode.getText().toString();
                String numberResidents = etNumberResidents.getText().toString();
                // call create function here

                // once successfully created, add houseID to preferences since user is now in a house


            }
        });
    }
}