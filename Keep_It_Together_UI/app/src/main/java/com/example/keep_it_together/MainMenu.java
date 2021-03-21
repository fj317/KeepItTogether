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

import java.io.IOException;

public class MainMenu extends AppCompatActivity {
    Button btYourTasks, btView, btAdd, btYourHouse;
    boolean userInHouse;
    Client dbConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // get widgets from xml file
        btView = findViewById(R.id.bt_view);
        btAdd = findViewById(R.id.bt_add);
        btYourTasks = findViewById(R.id.bt_your_tasks);
        btYourHouse = findViewById(R.id.bt_your_house);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MainMenu.AsyncTaskRunner runner = new MainMenu.AsyncTaskRunner();
        runner.execute();

        btYourTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this , com.example.keep_it_together.YourTasks.class));
            }
        });

        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this , com.example.keep_it_together.View.class));
                }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this , com.example.keep_it_together.Add.class));
            }
        });

        btYourHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // work out if user is in a house or not
                if (userInHouse) {
                    startActivity(new Intent(MainMenu.this , com.example.keep_it_together.YourHouse.class));
                } else {
                    startActivity(new Intent(MainMenu.this , com.example.keep_it_together.UserNoHouse.class));
                }
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
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            String userID = preferences.getString("userID", "");
            String dbRequest = "SELECT user_id, house_id FROM HouseUsers WHERE user_id = '" + userID + "'";
            String[] dbResponse = new String[0];
            try {
                dbResponse = dbConnection.select(dbRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (dbResponse[0].isEmpty()) {
                // if empty then not in a house
                userInHouse = false;
                // view button
                btView.setAlpha(.5f);
                btView.setClickable(false);
                // add button
                btAdd.setAlpha(.5f);
                btAdd.setClickable(false);
                // your tasks
                btYourTasks.setAlpha(.5f);
                btYourTasks.setClickable(false);
            } else {
                // if not empty then they are in a house
                userInHouse = true;
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("houseID", dbResponse[1]);
                edit.apply();
            }
        }
    }
}