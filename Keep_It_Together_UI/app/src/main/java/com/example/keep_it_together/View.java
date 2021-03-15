package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class View extends AppCompatActivity {
    Button btCompletedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        btCompletedTasks = findViewById(R.id.bt_completed);

        btCompletedTasks.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new Intent(View.this , com.example.keep_it_together.CompletedTasks.class));
            }
        });


    }
}