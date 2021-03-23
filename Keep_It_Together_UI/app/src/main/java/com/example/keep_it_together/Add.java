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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class Add extends AppCompatActivity {
    EditText et_add_Name, et_add_Description , et_add_repeat , et_add_cost;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch aSwitch;
    Button btSubmit;
    Spinner nameSpinner;
    Client dbConnection = null;
    String userID, houseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et_add_Name = findViewById(R.id.et_add_name);
        et_add_Description = findViewById(R.id.et_add_description);
        et_add_cost = findViewById(R.id.et_add_cost);
        btSubmit = findViewById(R.id.bt_submit);
        aSwitch = findViewById(R.id.chore_bill_switch);
        nameSpinner = findViewById(R.id.name_spinner);
        et_add_cost.setVisibility(View.INVISIBLE);
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        userID = preferences.getString("userID", "");
        houseID = preferences.getString("houseID", "");

        String dbRequest = "SELECT name FROM Users INNER JOIN HouseUsers ON Users.user_id = HouseUsers.user_id WHERE house_id = '" + houseID + "'";
        try {
            dbConnection = new Client("86.9.93.210", 58934);
            String[] dbResponse = dbConnection.select(dbRequest);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, dbResponse);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            nameSpinner.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }


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

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            String description = et_add_Description.getText().toString();
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            String userID = preferences.getString("userID", "");
            String houseID = preferences.getString("houseID", "");

<<<<<<< HEAD

=======
>>>>>>> 8099bc274d02f5aae865f94318ef66ef8de55b4c
            String price = et_add_cost.getText().toString();
            String name = et_add_Name.getText().toString();
            String user = nameSpinner.getSelectedItem().toString();
            try {
                String dbRequest = "SELECT user_id FROM Users WHERE name = '" + user + "'";
                String[] dbResponse = dbConnection.select(dbRequest);
                user = dbResponse[0];
            } catch (IOException e) {
                e.printStackTrace();
            }

            // choose if transaction or chore
            if (aSwitch.isChecked()) {
                // if checked then is transaction
                try {
                    bill(description, houseID, price, user, name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // if not checked then is chore
                try {
                    chore(description, houseID, user, name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void chore(String desc, String houseId, String userId, String name) throws IOException {
        boolean success = false;
        String choreId = "";
        // check if chore already exists
        String[] checkArr = dbConnection.select("SELECT chore_id FROM Chores WHERE name = '" + name + "'");
        if (checkArr[0].isEmpty()) {
            // if doesn't exist then add to database
                String chores = "INSERT INTO Chores (description, house_id, last_completed, name, active) VALUES (";
                chores += "'" + desc + "', '" + houseId + "', 'null', '" + name + "', 1)";
                success = dbConnection.modify(chores);
        } else {
            // if does exist then set chore to active
            choreId = checkArr[0];
            String chores = "UPDATE Chores SET active = 1 WHERE chore_id = " + choreId;
            dbConnection.modify(chores);
        }
        boolean assignSuccess = Assign.chore(choreId, userId);
        if (success && assignSuccess) {
            // if assigned success and chore added to database then
            // give message
            Toast.makeText(getApplicationContext(), "Chore successfully added",Toast.LENGTH_SHORT).show();
            // go back to main menu
            startActivity(new Intent(Add.this , MainMenu.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bill(String desc, String houseId, String price, String userId, String name) throws IOException {
        LocalDate date = LocalDate.now();
        String trans = "INSERT INTO Transactions (user_id, house_id, date, price, transaction_name, description) VALUES (";
        trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + price + ", '" + name + "', '" + desc + "')";
        dbConnection.modify(trans);
    }
}