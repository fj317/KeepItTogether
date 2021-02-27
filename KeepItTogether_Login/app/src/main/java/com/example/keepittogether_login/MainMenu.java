package com.example.keepittogether_login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {
    Button btYour_tasks, btView, btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // get widgets from xml file
        btView = findViewById(R.id.bt_view);
        btAdd = findViewById(R.id.bt_add);
        btYour_tasks = findViewById(R.id.bt_your_tasks);

    }
}