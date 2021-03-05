package com.example.keep_it_together;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.content.Intent;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class Register extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etRepeatPassword;
    Button btSubmit;
    Client dbConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // get widgets from xml file
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etRepeatPassword = findViewById(R.id.et_Re_password);
        btSubmit = findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
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
            try {
                System.out.println(Arrays.toString(dbConnection.select("SELECT user_id FROM users")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String name = getText(etName);
            String email = getText(etEmail);
            String password = getText(etPassword);
            String repeatPassword = getText(etRepeatPassword);
            String emailPattern = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";
            final Pattern emailRegex = Pattern.compile(emailPattern);
            String passwordPattern = "^(?=.*d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
            final Pattern passwordRegex = Pattern.compile(passwordPattern);

            // if text boxes are empty/null
            if (name.equals("") || email.equals("") || password.equals("") || repeatPassword.equals("")) {
                // send error message
                Toast.makeText(getApplicationContext(), "No data in text-box field.",Toast.LENGTH_SHORT).show();
                return;
            }
            // if passwords dont match
            if (!password.equals(repeatPassword)) {
                // send error message
                Toast.makeText(getApplicationContext(), "Passwords don't match!",Toast.LENGTH_SHORT).show();
                return;
            }
            // check email is valid
            Matcher matcher = emailRegex.matcher(email);
            if (!(matcher.matches())) {
                // error message
                Toast.makeText(getApplicationContext(), "Email is not valid",Toast.LENGTH_SHORT).show();
                return;
            }
            // check password is valid (at least 8 characters, must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number, can contain special character)
            matcher = passwordRegex.matcher(password);
            if (!(matcher.matches())) {
                // error message
                Toast.makeText(getApplicationContext(), "Password is not valid. It must be: 8 characters long, and contain at least 1 uppercase, 1 lowercase and 1 number.",Toast.LENGTH_LONG).show();
                return;
            }
            String dbRequest = "SELECT email FROM users WHERE email = '" + email + "'";
            String[] dbResponse = new String[1];
            try {
                dbResponse = dbConnection.select(dbRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // check if email is already in database
            if (!dbResponse[0].isEmpty()) {
                // error message
                Toast.makeText(getApplicationContext(), "Email already in use.",Toast.LENGTH_LONG).show();
                return;
            }

            // get hash of password
            password = BCrypt.hashpw(password, BCrypt.gensalt(12));

            // all validations check done so now add valid credentials to database
            Boolean database = dbConnection.modify("INSERT INTO Users (email, password, name) VALUES ('" + email + "', '" + password + "', '" + name + "')");
            if (database) {
                Toast.makeText(getApplicationContext(), "Register successful!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Register unsuccessful, please try again.",Toast.LENGTH_SHORT).show();
            }
            // go back to login page now registering is done
            startActivity(new Intent(Register.this , AccountLogin.class));
        }

    }

    private String getText(EditText textBox) {
        String text = textBox.getText().toString();
        return text;
    }

}