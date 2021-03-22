package com.example.keep_it_together;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class Add extends AppCompatActivity {
    EditText et_add_Name, et_add_Description , et_add_repeat , et_add_cost;
    Switch aSwitch;
    Button btSubmit;
    Client dbConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et_add_Name = findViewById(R.id.et_add_name);
        et_add_Description = findViewById(R.id.et_add_description);
        et_add_cost = findViewById(R.id.et_add_cost);
        btSubmit = findViewById(R.id.bt_submit);
        aSwitch = findViewById(R.id.chore_bill_switch);
        et_add_cost.setVisibility(View.INVISIBLE);


        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    et_add_cost.setVisibility(View.VISIBLE);
                }else{
                    et_add_cost.setVisibility(View.INVISIBLE);
                }
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on submit button press
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Add.AsyncTaskRunner runner = new Add.AsyncTaskRunner();
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
            String description = et_add_Description.getText().toString();
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            String userID = preferences.getString("userID", "");
            String houseID = preferences.getString("houseID", "");
            String price = et_add_cost.getText().toString();
            String name = et_add_Name.getText().toString();
            // choose if transaction or chore
            if (aSwitch.isChecked()) {
                // if checked then is transaction
                try {
                    bill(description, houseID, price, userID, name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // if not checked then is chore
                try {
                    chore(description, houseID, false, userID, name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    private static void chore(String desc, String houseId, boolean complete, String userId, String name) throws IOException {
        LocalDate date = LocalDate.now();
        Client db = new Client("86.9.93.210", 58934);
        String choreId = "";
        String[] checkArr = db.select("SELECT chore_id FROM Chores WHERE name = '" + name + "'");
        if (checkArr[0].isEmpty()) {
            if (complete) {
                String chores = "INSERT INTO Chores (description, house_id, last_completed, name, active) VALUES (";
                chores += "'" + desc + "', '" + houseId + "', '" + date.toString() + "', '" + name + "', + 0)";
                db.modify(chores);
                choreId = db.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'")[0];
                String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
                choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
                db.modify(choreRecords);
                Assign.chore(choreId, userId);
            }
            else {
                String chores = "INSERT INTO Chores (description, house_id, last_completed, name, active) VALUES (";
                chores += "'" + desc + "', '" + houseId + "', 'null', '" + name + "', 1)";
                db.modify(chores);
                Assign.chore(choreId, userId);
            }
        }
        else {
            choreId = checkArr[0];
            if (complete) {
                String chores = "UPDATE Chores SET last_completed = '";
                chores += date.toString() + "', active = 0 WHERE chore_id = " + choreId;
                db.modify(chores);
                String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
                choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
                db.modify(choreRecords);
                Assign.chore(choreId, userId);
            }
            else {
                String chores = "UPDATE Chores SET active = 1 WHERE chore_id = " + choreId;
                db.modify(chores);
                Assign.chore(choreId, userId);
            }
        }
    }

    private static void bill(String desc, String houseId, String price, String userId, String name) throws IOException {
        LocalDate date = LocalDate.now();
        Client db = new Client("86.9.93.210", 58934);
        String trans = "INSERT INTO Transactions (user_id, house_id, date, price, name, description) VALUES (";
        trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + price + ", '" + name + "', '" + desc + "')";
        db.modify(trans);
    }
}