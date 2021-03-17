package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainMenu extends AppCompatActivity {
    Button btYourTasks, btView, btAdd, btYourHouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // get widgets from xml file
        btView = findViewById(R.id.bt_view);
        btAdd = findViewById(R.id.bt_add);
        btYourTasks = findViewById(R.id.bt_your_tasks);
        btYourHouse = findViewById(R.id.bt_your_house);

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
                // work out which page to send user to (userNoHouse or yourHouse)
                try {
                    if (userInHouse()) {
                        startActivity(new Intent(MainMenu.this , com.example.keep_it_together.YourHouse.class));
                    } else {
                        startActivity(new Intent(MainMenu.this , com.example.keep_it_together.UserNoHouse.class));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(MainMenu.this , com.example.keep_it_together.UserNoHouse.class));
            }
        });

    }

    private boolean userInHouse() throws IOException {
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String userID = preferences.getString("userID", "");
        Client dbConnection = new Client("86.9.93.210", 58934);
        String dbRequest = "SELECT user_id, house_id FROM HouseUsers WHERE user_id = '" + userID + "'";
        String[] dbResponse = dbConnection.select(dbRequest);
        // if empty then no record exists so they arent part of any houses so return false
        // otherwise return true
        return !dbResponse[0].isEmpty();
    }
}