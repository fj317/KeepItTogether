package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HouseTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_tasks);

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String house_Id = preferences.getString("houseID", "");
        String[] chores,transactions,tasks;
        int numOfTasks;
        try {
            Client dbConnection = new Client("86.9.93.210", 58934);
            chores = dbConnection.select("SELECT chore_id, name FROM Chores WHERE house_id = '" + house_Id + "'");
            transactions = dbConnection.select("SELECT transaction_id, transaction_name FROM Transactions WHERE house_id = '" + house_Id + "'");
            if (chores[0].isEmpty()) {
                chores = new String[0];
            }
            if (transactions[0].isEmpty()) {
                transactions = new String[0];
            }
            // combining the 2 arrays
            tasks = Arrays.copyOf(chores , chores.length + transactions.length);
            System.arraycopy(transactions , 0 , tasks , chores.length , transactions.length);

            numOfTasks = tasks.length;
            LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.houseTasksLayout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable buttonDrawableLayout = getDrawable(R.drawable.menu_button);

            for(int i = 0; i < numOfTasks; i+=2) {
                Button button = new Button(HouseTasks.this);
                button.setBackground(buttonDrawableLayout);
                button.setTextSize(25);
                // names of tasks here
                button.setText(tasks[i+1]);
                params.setMargins(0, 0, 0, 25);
                button.setLayoutParams(params);
                buttonLayout.addView(button);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}