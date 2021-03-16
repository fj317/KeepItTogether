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
    private String joinCode = "";

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
                builder.setTitle("Enter house join code:");

                // Set up the input
                final EditText input = new EditText(UserNoHouse.this);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joinCode = input.getText().toString();
                        try {
                            // call function which works out which houseID from join code
                            String houseId = getHouseId(joinCode);
                            // get userID for the current user
                            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                            String userID = preferences.getString("userID", "");
                            // call join house function
                            // CALL FUNCTION HERE
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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

    private String getHouseId(String joinCode) throws IOException {
        Client dbConnection = new Client("86.9.93.210", 58934);
        String[] dbResponse = dbConnection.select("SELECT house_id FROM house WHERE house_code = " + joinCode);
        if (dbResponse[0].isEmpty()) {
            return null;
        }
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("houseId", dbResponse[0]);
        edit.apply();
        return dbResponse[0];
    }

}