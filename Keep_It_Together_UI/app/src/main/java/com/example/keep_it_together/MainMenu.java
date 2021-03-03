package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    Button btYour_tasks, btView, btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // get widgets from xml file
        btView = findViewById(R.id.bt_view);
        btAdd = findViewById(R.id.bt_add);
        btYour_tasks = findViewById(R.id.bt_your_tasks);

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
    }
}