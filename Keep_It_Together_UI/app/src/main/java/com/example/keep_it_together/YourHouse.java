package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

public class YourHouse extends AppCompatActivity {
    Button btLeaveHouse;
    TextView txt_houseMates, txt_houseJoinCode;
    Client dbConnection = null;
    boolean initialised = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_house);
        btLeaveHouse = findViewById(R.id.bt_leave);
        txt_houseMates = findViewById(R.id.txt_users);
        txt_houseJoinCode = findViewById(R.id.txt_houseJoinCode);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        YourHouse.AsyncTaskRunner runner = new YourHouse.AsyncTaskRunner();
        runner.execute();

        btLeaveHouse.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on submit button press
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                YourHouse.AsyncTaskRunner runner = new YourHouse.AsyncTaskRunner();
                runner.execute();
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
                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                String houseID = preferences.getString("houseID", "");
                txt_houseJoinCode.setText(houseID);
                //String dbRequest = "SELECT user_id FROM HouseUsers WHERE house_id = '" + houseID + "'";
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
            }
        }
    }
}