package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class UserNoHouse extends AppCompatActivity {
    Button btCreateHouse, btJoinHouse;
    EditText joinCodeInput;
    Client dbConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_no_house);
        btCreateHouse = findViewById(R.id.bt_create_house);
        btJoinHouse = findViewById(R.id.bt_join_house);

        // reference https://stackoverflow.com/questions/10903754/input-text-dialog-android
        btJoinHouse.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserNoHouse.this);
                builder.setTitle("Enter house code:");

                // Set up the input
                joinCodeInput = new EditText(UserNoHouse.this);
                builder.setView(joinCodeInput);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        UserNoHouse.AsyncTaskRunner runner = new UserNoHouse.AsyncTaskRunner();
                        runner.execute();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        btCreateHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserNoHouse.this , com.example.keep_it_together.CreateHouse.class));
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
            String houseID = joinCodeInput.getText().toString();
            // get userID for the current user
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            String userID = preferences.getString("userID", "");
            // join house function
            try {
                join(houseID, Integer.parseInt(userID));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // once successfully joined house put house code in preferences
            storeHouseID(houseID);
        }
    }


    private void storeHouseID(String houseId) {
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("houseID", houseId);
        edit.apply();
    }

    private void join(String houseID, int userID) throws IOException {
        String[] find = dbConnection.select("SELECT house_id FROM House WHERE house_id = '" + houseID + "'");
        if (find[0].isEmpty()) {
            Toast.makeText(getApplicationContext(), "This household does not exist",Toast.LENGTH_SHORT).show();
        }
        else {
            dbConnection.modify("UPDATE House SET number_of_residents = number_of_residents + 1 WHERE house_id = '" + houseID + "'");
            dbConnection.modify("INSERT INTO HouseUsers (user_id, house_id) VALUES (" + userID + ", '" + houseID + "')");
            Toast.makeText(getApplicationContext(), "Successfully join your house",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserNoHouse.this , YourHouse.class));
        }
    }

}