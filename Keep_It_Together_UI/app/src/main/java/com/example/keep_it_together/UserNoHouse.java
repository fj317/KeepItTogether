package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class UserNoHouse extends AppCompatActivity {
    Button btCreateHouse, btJoinHouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_no_house);
        btCreateHouse = findViewById(R.id.bt_create_house);
        btJoinHouse = findViewById(R.id.bt_join_house);

        // reference https://stackoverflow.com/questions/10903754/input-text-dialog-android
        btJoinHouse.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserNoHouse.this);
                builder.setTitle("Enter house code:");

                // Set up the input
                final EditText input = new EditText(UserNoHouse.this);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String houseID = input.getText().toString();
                        // get userID for the current user
                        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                        String userID = preferences.getString("userID", "");
                        // call join house function
                        // CALL FUNCTION HERE
                        // once successfully joined house put house code in preferences
                        storeHouseID(houseID);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


        btCreateHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserNoHouse.this , com.example.keep_it_together.CreateHouse.class));
            }
        });

    }

    private void storeHouseID(String houseId) {
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("houseId", houseId);
        edit.apply();
    }

}