package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class View extends AppCompatActivity {
    Button btCompletedTasks, btGraphs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        btCompletedTasks = findViewById(R.id.bt_completed);
        btGraphs = findViewById(R.id.bt_graphs);


        btCompletedTasks.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(View.this , com.example.keep_it_together.CompletedTasks.class));
            }
        });

        btGraphs.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(View.this , com.example.keep_it_together.Graphs.class));
            }
        });


    }
}