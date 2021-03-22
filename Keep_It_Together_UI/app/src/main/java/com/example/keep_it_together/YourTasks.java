package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class YourTasks extends AppCompatActivity {
    Client dbConnection = null;
    String[] chores,transactions,tasks, task_names;
    int numOfTasks;
    boolean transaction = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_tasks);
        YourTasks.AsyncTaskRunner runner = null;
        try {
            runner = new YourTasks.AsyncTaskRunner();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runner.execute();
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

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
        protected void onPostExecute(String result) {
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            String house_Id = preferences.getString("houseID", "");
            String user_Id = preferences.getString("userID", "");
            try {
                // getting tasks
                chores = dbConnection.select("SELECT chore_id FROM chore_users WHERE user_id = ;" + user_Id);
                transactions = dbConnection.select("SELECT transactions_id FROM transations WHERE house_id = ;" + house_Id + "AND" + "user_id = " + user_Id);
                // combining the 2 arrays
                tasks = Arrays.copyOf(chores , chores.length + transactions.length);
                System.arraycopy(transactions , 0 , tasks , chores.length , transactions.length);

                // getting names of tasks
                //TODO
                // add names columns to chores and transaction so they can be displayed.

            } catch (IOException e) {
                e.printStackTrace();
            }
            

            numOfTasks = tasks.length;
            LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable buttonDrawableLayout = getDrawable(R.drawable.menu_button);

            ArrayList<Button> buttons = new ArrayList<Button>();
            for(int i = 0; i <= numOfTasks+1; i++) {
                Button button = new Button(YourTasks.this);
                buttons.add(button);
                button.setBackground(buttonDrawableLayout);
                //button.setTextColor(0xffffff);
                button.setTextSize(25);
                // names of tasks here
                button.setText("Task " + i);
                params.setMargins(0, 0, 0, 25);
                button.setLayoutParams(params);
                buttonLayout.addView(button);
                if(i==chores.length)transaction=true;

                int finalI = i;
                button.setOnClickListener(new android.view.View.OnClickListener()  {

                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        if(!transaction) {
                            bundle.putString("chore", tasks[finalI]);
                        }else{
                            bundle.putString("transaction", tasks[finalI]);
                        }
                        Intent intent = new Intent(YourTasks.this , Edit.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }


    }

}