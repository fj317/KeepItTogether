package com.example.keep_it_together;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountLogin extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);
        // get widgets from xml file
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btSubmit = findViewById(R.id.bt_submit);

        btSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // make request to database here
                // also need to run validation on input to stop SQL injection / other security stuff
                if (etUsername.getText().toString().equals("ADMIN") && etPassword.getText().toString().equals("ADMIN")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                        AccountLogin.this
                    );
                    // alert box for user
                    builder.setIcon(R.drawable.ic_tick);
                    builder.setTitle("Login Successful.");
                    builder.setMessage("Do some stuff now.");
                    builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            startActivity(new Intent(AccountLogin.this , MainMenu.class));
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                    // Moving to Menu page

                } else {
                    // text pop up to say invalid
                    Toast.makeText(getApplicationContext(), "INVALID CREDENTIALS",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}