package com.example.keep_it_together;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.IOException;
import java.util.Arrays;


public class AccountLogin extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btSubmit, btRegister;
    ProgressBar progressBar;
    Client dbConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // get widgets from xml file
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btSubmit = findViewById(R.id.bt_submit);
        btRegister = findViewById(R.id.bt_register);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //AccountLogin.AsyncTaskRunner runner = new AccountLogin.AsyncTaskRunner();
                progressBar.setVisibility(View.VISIBLE);
                //runner.execute();
                startActivity(new Intent(AccountLogin.this , MainMenu.class));
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountLogin.this , Register.class));
            }
        });
    }

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
            String email = getText(etEmail);
            String password = getText(etPassword);
            // make request to database here

            // check email address exists in databse
            // if exists, check password matches the one stored
            // if both true then login successful
            String dbRequest = "SELECT user_id, email, password FROM users WHERE email = '" + email + "'";
            String[] dbResponse = new String[3];
            try {
                dbResponse = dbConnection.select(dbRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // check if email exists in database
            if (dbResponse[1].isEmpty()) {
                // if its not then error message
                Toast.makeText(getApplicationContext(), "Invalid email",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            // hash password to compare
            password = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // if email is valid, then check the passwords match
            if (!dbResponse[2].equals(password)) {
                // if they dont match then error message
                Toast.makeText(getApplicationContext(), "Invalid password",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            // get userID
            String userID = dbResponse[0];
            // put userID into preferences
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("userID", userID);
            edit.apply();

            // if both match then allow login
            startActivity(new Intent(AccountLogin.this , MainMenu.class));
        }
    }


    private String getText(EditText textBox) {
        String text = textBox.getText().toString();
        return text;
    }

}