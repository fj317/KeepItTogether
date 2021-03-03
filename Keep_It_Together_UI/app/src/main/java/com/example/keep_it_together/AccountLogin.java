package com.example.keep_it_together;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountLogin extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // get widgets from xml file
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btSubmit = findViewById(R.id.bt_submit);


        btSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // make request to database here
                // also need to run validation on input to stop SQL injection / other security stuff
                if (etEmail.getText().toString().equals("ADMIN") && etPassword.getText().toString().equals("ADMIN")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                        AccountLogin.this
                    );
                    // Moving to Menu page
                    startActivity(new Intent(AccountLogin.this , MainMenu.class));
                    Toast.makeText(getApplicationContext(), "Login successful",Toast.LENGTH_SHORT).show();
                } else {
                    // text pop up to say invalid
                    Toast.makeText(getApplicationContext(), "INVALID CREDENTIALS",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}