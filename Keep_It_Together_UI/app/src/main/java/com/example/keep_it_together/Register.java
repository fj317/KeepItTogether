package com.example.keep_it_together;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class Register extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etRepeatPassword;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // get widgets from xml file
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etRepeatPassword = findViewById(R.id.et_Re_password);
        btSubmit = findViewById(R.id.bt_submit);
        String emailPattern = "[w.-]+@[w.-]+.w{2,4}\b";
        final Pattern emailRegex = Pattern.compile(emailPattern);
        String passwordPattern = "^(?=.*d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        final Pattern passwordRegex = Pattern.compile(passwordPattern);

        btSubmit.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get text box text
                String name = getText(etName);
                String email = getText(etEmail);
                String password = getText(etPassword);
                String repeatPassword = getText(etRepeatPassword);

                // if passwords dont match
                if (password != repeatPassword) {
                    // send error message here
                    // if text boxs are empty/null
                } else if (name == null || email == null || password == null || repeatPassword == null) {
                    // send error message here
                }
                // check email is valid
                Matcher matcher = emailRegex.matcher(email);
                if (!(matcher.matches())) {
                    // error message here
                }
                // check password is valid (at least 8 characters, must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number, can contain special character)
                matcher = passwordRegex.matcher(password);
                if (!(matcher.matches())) {
                    // error message here
                }
                // all validations check done so now add valid credentials to database
                Client dbConnection = null;
                try {
                    dbConnection = new Client("86.9.93.210", 58934);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dbConnection.modify("INSERT INTO Users (email, password, name) VALUES (" + email + "," + password + ", " + name + ")");
                // go back to login page now registering is done
                startActivity(new Intent(Register.this , com.example.keep_it_together.AccountLogin.class));
            }
        });
    }

    private String getText(EditText textBox) {
        return textBox.getText().toString();
    }

}