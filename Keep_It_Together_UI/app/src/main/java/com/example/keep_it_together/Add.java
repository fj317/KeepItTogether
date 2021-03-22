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
        et_add_repeat = findViewById(R.id.et_add_repeat);
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
                    chore(description, houseID, false, userID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void chore(String desc, String houseId, boolean complete, String userId) throws IOException {
        LocalDate date = LocalDate.now();
        String choreId = "";
        String[] checkArr = dbConnection.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'");
        if (checkArr[0].isEmpty()) {
            if (complete) {
                System.out.println("in 1");
                String chores = "INSERT INTO Chores (description, house_id, last_completed) VALUES (";
                chores += "'" + desc + "', '" + houseId + "', '" + date.toString() + "')";
                dbConnection.modify(chores);
                choreId = dbConnection.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'")[0];
                String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
                choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
                dbConnection.modify(choreRecords);
                Assign.chore(choreId, userId);
            }
            else {
                System.out.println("in 2");
                String chores = "INSERT INTO Chores (description, house_id, last_completed) VALUES (";
                chores += "'" + desc + "', '" + houseId + "', 'null')";
                System.out.println(chores);
                dbConnection.modify(chores);
                Assign.chore(choreId, userId);
            }
        }
        else {
            choreId = checkArr[0];
            if (complete) {
                System.out.println("in 3");
                String chores = "UPDATE Chores SET last_completed = '";
                chores += date.toString() + "' WHERE chore_id = " + choreId;
                dbConnection.modify(chores);
                String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
                choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
                dbConnection.modify(choreRecords);
                Assign.chore(choreId, userId);
            }
            else {
                System.out.println("in 4");
                Assign.chore(choreId, userId);
            }
        }
    }

    private void bill(String desc, String houseId, String price, String userId, String name) throws IOException {
        LocalDate date = LocalDate.now();
        String prodId = "";
        String[] checkArr = dbConnection.select("SELECT product_id FROM Products WHERE name = '" + name + "'");
        if (checkArr[0].isEmpty()) {
            System.out.println("empty");
            String products = "INSERT INTO Products (name, description) VALUES ('";
            products += name + "', '" + desc + "')";
            dbConnection.modify(products);
            prodId = dbConnection.select("SELECT product_id FROM Products WHERE name = '" + name + "'")[0];
            String trans = "INSERT INTO Transactions (user_id, house_id, date, product_id, price) VALUES (";
            trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + prodId + ", " + price + ")";
            dbConnection.modify(trans);
        }
        else {
            prodId = checkArr[0];
            String trans = "INSERT INTO Transactions (user_id, house_id, date, product_id, price) VALUES (";
            trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + prodId + ", " + price + ")";
            dbConnection.modify(trans);
        }
    }
}