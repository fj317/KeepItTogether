package com.example.keep_it_together;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class Settings extends AppCompatActivity {

    Client dbConnection = null;
    TextView addressText;
    TextView postCodeText;
    TextView houseNumberText;
    String house_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addressText = (TextView)findViewById(R.id.addressBox);
        postCodeText = (TextView)findViewById(R.id.postCodeBox);
        houseNumberText = (TextView)findViewById(R.id.housePeopleBox);
        Button updateButton = findViewById(R.id.updateButton);

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        house_id = preferences.getString("houseID", "");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();

        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AsyncTaskRunnerUpdate runner = new AsyncTaskRunnerUpdate();
                runner.execute();
                startActivity(new Intent(Settings.this , com.example.keep_it_together.YourHouse.class));
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
            //String house_id = "u8k1s4lusy";
            String request = "SELECT address, postcode, number_of_residents FROM House WHERE house_id = '" +  house_id + "'";
            String[] response;
            try {
                response  = dbConnection.select(request);
                if (!response[0].isEmpty()){
                    addressText.setText(response[0]);
                    postCodeText.setText(response[1]);
                    houseNumberText.setText(response[2]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunnerUpdate extends AsyncTask<String, String, String> {
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
            //String house_id = "u8k1s4lusy";
            String request = "UPDATE House SET address = '" + addressText.getText() + "', postcode = '" + postCodeText.getText() + "', number_of_residents = " + houseNumberText.getText() + " WHERE house_id = '" +  house_id + "'";
            dbConnection.modify(request);
        }
    }
}
