package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

public class YourHouse extends AppCompatActivity {
    Button btLeaveHouse, btSettings;
    TextView txt_houseMates, txt_houseJoinCode;
    Client dbConnection = null;
    String houseID, userID;
    boolean initialised = false;
    YourHouse.AsyncTaskRunner runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_house);
        btSettings = findViewById(R.id.bt_settings);
        btLeaveHouse = findViewById(R.id.bt_leave);
        txt_houseMates = findViewById(R.id.txt_users);
        txt_houseJoinCode = findViewById(R.id.txt_houseJoinCode);
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        houseID = preferences.getString("houseID", "");
        userID = preferences.getString("userID", "");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        runner = new YourHouse.AsyncTaskRunner();
        runner.execute();

        btLeaveHouse.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on leave button press
                runner = new YourHouse.AsyncTaskRunner();
                runner.execute();
            }
        });

        btSettings.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on settings click
                initialised = false;
                startActivity(new Intent(YourHouse.this , Settings.class));
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                dbConnection = new Client("86.9.93.210", 58934);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!initialised) {
                // if not initialised yet then set up page
                txt_houseJoinCode.setText(houseID);
                String dbRequest = "SELECT name FROM Users WHERE user_id = (SELECT user_id FROM HouseUsers WHERE house_id = '" + houseID + "')";
                try {
                    StringBuilder users = new StringBuilder();
                    String[] dbResponse = dbConnection.select(dbRequest);
                    for (String name: dbResponse) {
                        users.append(name).append("\n");
                    }
                    txt_houseMates.setText(users.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                initialised = true;
            } else {
                // otherwise leave button is pressed so do leave house stuff
                try {
                    leave(houseID, Integer.parseInt(userID));
                    initialised = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void leave(String houseID, int userID) throws IOException {
        dbConnection.modify("UPDATE House SET number_of_residents = number_of_residents - 1 WHERE house_id = '" + houseID + "'");
        boolean success = dbConnection.modify("DELETE FROM HouseUsers WHERE user_id = " + userID);
        if (success) {
            // if successfully delete user tell them
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            preferences.edit().remove("houseID").apply();
            Toast.makeText(getApplicationContext(), "Successfully left your house",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(YourHouse.this , MainMenu.class));
        } else {
            // if not succesfully tell them
            Toast.makeText(getApplicationContext(), "Error leaving your house. Please try again.",Toast.LENGTH_SHORT).show();
        }
    }
}