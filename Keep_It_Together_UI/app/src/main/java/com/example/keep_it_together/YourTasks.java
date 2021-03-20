package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;
import java.util.ArrayList;

public class YourTasks extends AppCompatActivity {
    Client dbConnection = null;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_tasks);
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable buttonDrawableLayout = getDrawable(R.drawable.menu_button);

        ArrayList<Button> buttons = new ArrayList<Button>();
        YourTasks.AsyncTaskRunner runner = null;
        
        try {
            runner = new YourTasks.AsyncTaskRunner();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runner.execute();



        for(int i = 0; i <= 10; i++) {
            Button button = new Button(this);
            buttons.add(button);
            button.setBackground(buttonDrawableLayout);
            //button.setTextColor(0xffffff);
            button.setTextSize(25);
            button.setText("Task " + i);
            params.setMargins(0, 0, 0, 25);
            button.setLayoutParams(params);
            buttonLayout.addView(button);
        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String house_Id = preferences.getString("houseID", "");
        String user_Id = preferences.getString("userID", "");



        private AsyncTaskRunner() throws IOException {}

        @Override
        protected String doInBackground(String... params) {
            try {
                dbConnection = new Client("86.9.93.210", 58934);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        String[] chores = dbConnection.select("SELECT chore_id FROM chore_users WHERE user_id = ;" + user_Id);
        String[] transactions = dbConnection.select("SELECT transactions_id FROM transations WHERE house_id = ;" + house_Id + "AND" + "user_id = " + user_Id);

    }

}