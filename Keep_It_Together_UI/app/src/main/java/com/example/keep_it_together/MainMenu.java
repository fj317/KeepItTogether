package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        //SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        //btYourHouse.setText(preferences.getString("userID", ""));

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
                startActivity(new Intent(MainMenu.this , com.example.keep_it_together.UserNoHouse.class));
            }
        });
    }
}