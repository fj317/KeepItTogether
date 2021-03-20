package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class CreateHouse extends AppCompatActivity {
    Button btSubmit;
    EditText etAddress, etPostCode, etNumberResidents;
    String houseID;
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
                try {
                    create(address, postCode, Integer.parseInt(numberResidents));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // once successfully created, add houseID to preferences since user is now in a house
                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("houseID", houseID);
                edit.apply();
                startActivity(new Intent(CreateHouse.this , com.example.keep_it_together.YourHouse.class));
            }
        });
    }

    private void create(String address, String postcode, int numberOfResidents) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        String[] find = dbConnection.select("SELECT house_id FROM House WHERE address = '" + address + "' AND postcode = '" + postcode + "'");
        if (find[0].isEmpty()) {
            houseID = randomIdGenerator();
            boolean completed = dbConnection.modify("INSERT INTO House (house_id, address, postcode, number_of_residents) VALUES ('" + houseID + "', '" + address + "', '" + postcode + "', " + numberOfResidents + ")");
            if (completed) {
                Toast.makeText(getApplicationContext(), "House successfully created",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failure creating house. Please try again",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "House already exists",Toast.LENGTH_SHORT).show();
        }
    }

    private String randomIdGenerator() throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        Random rand  = new Random();
        int length = 10;
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(chars[rand.nextInt(36)]);
        }
        String randomID = builder.toString();
        String[] checkUniqueness = dbConnection.select("SELECT house_id FROM House WHERE house_id = '" + randomID + "'");
        if (!checkUniqueness[0].isEmpty()) {
            randomIdGenerator();
        }
        return randomID;
    }

}