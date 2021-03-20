package com.example.keep_it_together;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        et_add_Name = findViewById(R.id.et_add_name);
        et_add_Description = findViewById(R.id.et_add_description);
        et_add_repeat = findViewById(R.id.et_add_repeat);
        et_add_cost = findViewById(R.id.et_add_cost);
        btSubmit = findViewById(R.id.bt_submit);
        aSwitch = (android.widget.Switch) findViewById(R.id.chore_bill_switch);
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
                String description = et_add_Description.getText().toString();
                SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                String userID = preferences.getString("userID", "");
                String houseID = preferences.getString("houseID", "");
                // choose if transaction or chore


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void chore(String desc, String houseId, boolean complete, String userId) throws IOException {
        LocalDate date = LocalDate.now();
        Client db = new Client("86.9.93.210", 58934);
        String choreId = "";
        String[] checkArr = db.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'");
        if (checkArr[0].isEmpty()) {
            if (complete) {
                System.out.println("in 1");
                String chores = "INSERT INTO Chores (description, house_id, last_completed) VALUES (";
                chores += "'" + desc + "', '" + houseId + "', '" + date.toString() + "')";
                db.modify(chores);
                choreId = db.select("SELECT chore_id FROM Chores WHERE description = '" + desc + "'")[0];
                String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
                choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
                db.modify(choreRecords);
                Assign.chore(choreId, userId);
            }
            else {
                System.out.println("in 2");
                String chores = "INSERT INTO Chores (description, house_id, last_completed) VALUES (";
                chores += "'" + desc + "', '" + houseId + "', 'null')";
                System.out.println(chores);
                db.modify(chores);
                Assign.chore(choreId, userId);
            }
        }
        else {
            choreId = checkArr[0];
            if (complete) {
                System.out.println("in 3");
                String chores = "UPDATE Chores SET last_completed = '";
                chores += date.toString() + "' WHERE chore_id = " + choreId;
                db.modify(chores);
                String choreRecords = "INSERT INTO ChoreRecords (user_id, chore_id, date_completed) VALUES (";
                choreRecords += userId + ", " + choreId + ", '" + date.toString() + "')";
                db.modify(choreRecords);
                Assign.chore(choreId, userId);
            }
            else {
                System.out.println("in 4");
                Assign.chore(choreId, userId);
            }
        }
    }

    private static void bill(String desc, String houseId, String price, String userId, String name) throws IOException {
        LocalDate date = LocalDate.now();
        Client db = new Client("86.9.93.210", 58934);
        String prodId = "";
        String[] checkArr = db.select("SELECT product_id FROM Products WHERE name = '" + name + "'");
        if (checkArr[0].isEmpty()) {
            System.out.println("empty");
            String products = "INSERT INTO Products (name, description) VALUES ('";
            products += name + "', '" + desc + "')";
            db.modify(products);
            prodId = db.select("SELECT product_id FROM Products WHERE name = '" + name + "'")[0];
            String trans = "INSERT INTO Transactions (user_id, house_id, date, product_id, price) VALUES (";
            trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + prodId + ", " + price + ")";
            db.modify(trans);
        }
        else {
            prodId = checkArr[0];
            String trans = "INSERT INTO Transactions (user_id, house_id, date, product_id, price) VALUES (";
            trans += userId + ", '" + houseId + "', '" + date.toString() + "', " + prodId + ", " + price + ")";
            db.modify(trans);
        }
    }
}