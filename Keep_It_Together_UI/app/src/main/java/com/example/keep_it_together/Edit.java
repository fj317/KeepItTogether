package com.example.keep_it_together;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class Edit extends AppCompatActivity {
    Client dbConnection = null;
    String task_id , new_desription , new_cost , new_name , new_assigned_name , name_for_spinner;
    String[] description_text , cost_text, name_text, assigned_name;
    String task = "chore";
    String Task = "Chore";
    Button save_button;
    EditText description , cost , name;
    String userID, houseID;
    Spinner nameSpinner;
    CheckBox check_box;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Edit.AsyncTaskRunner runner = null;
        try {
            runner = new Edit.AsyncTaskRunner();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        userID = preferences.getString("userID", "");
        houseID = preferences.getString("houseID", "");

        nameSpinner = findViewById(R.id.name_spinner);

        // get the spinner names
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
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private AsyncTaskRunner() throws IOException {}

        @Override
        protected String doInBackground(String... params) {
            try {
                dbConnection = new Client("86.9.93.210", 58934);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            // see if the task is a transaction  or chore
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if(bundle.getString("chore") != null){
                    task_id = bundle.getString("chore");
                }else if((bundle.getString("transaction") != null)){
                    task_id = bundle.getString("transaction");
                    task = "transaction";
                    Task = "Transaction";
                }else{
                    System.out.println("error");
                }

                check_box = findViewById(R.id.checkBox2);
                if(task.equals("transaction")){
                    check_box.setVisibility(View.INVISIBLE);
                }

            }

            try {
                // getting previous task description
                description_text = dbConnection.select("SELECT description FROM " + Task + "s" + " WHERE " + task + "_id = " + task_id);
                description = findViewById(R.id.et_edit_description);
                // setting description to what it was before
                description.setText(description_text[0]);


                cost = findViewById(R.id.et_edit_cost);
                if(task.equals("transaction")){
                    cost_text = dbConnection.select("SELECT price FROM transactions WHERE transaction_id = " + task_id);
                    cost.setVisibility(android.view.View.VISIBLE);
                    cost.setText(cost_text[0]);
                } else {
                    cost.setVisibility(View.INVISIBLE);
                }

                name_text = dbConnection.select("SELECT name FROM " + Task + "s" + " WHERE " + task + "_id = " + task_id);
                name = findViewById(R.id.et_edit_name);
                // setting description to what it was before
                name.setText(name_text[0]);


            } catch (IOException e) {
                e.printStackTrace();
            }


            save_button = findViewById(R.id.bt_edit_save);
            save_button.setOnClickListener(new android.view.View.OnClickListener()  {
                @Override
                public void onClick(View v) {
                    new_desription = getText(findViewById(R.id.et_edit_description));
                    new_cost = getText(findViewById(R.id.et_edit_cost));
                    new_name = getText(findViewById(R.id.et_edit_name));

                    if(!new_desription.equals(description_text[0])){
                        dbConnection.modify("UPDATE " + Task + "s SET " + "description = '" + new_desription + "' WHERE " + task + "_id = " + task_id);
                    }
                    if (task.equals("transaction")) {
                        if(!new_cost.equals(cost_text[0])){
                            dbConnection.modify("UPDATE " + Task + "s SET " + "cost = '" + new_cost + "' WHERE " + task + "_id = " + task_id);
                        }
                    }
                    if(!new_name.equals(name_text[0])){
                        dbConnection.modify("UPDATE " + Task + "s SET " + "name = '" + new_name + "' WHERE " + task + "_id = " + task_id);
                    }
                    if(check_box.isChecked() && task.equals("chore")){
                        dbConnection.modify("UPDATE chores SET completed = '1' WHERE chore_id = " + task_id);
                    }

                    try {
                        if(task.equals("transaction")){
                            assigned_name = dbConnection.select("SELECT user_id FROM transactions WHERE transaction_id = '" + task_id + "'");
                        }else if(task.equals("chore")){
                            assigned_name = dbConnection.select("SELECT user_id FROM ChoreUsers WHERE chore_id = '" + task_id + "'");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    new_assigned_name = nameSpinner.getSelectedItem().toString();
                    if(!assigned_name[0].equals(new_assigned_name)){
                        try {
                            assigned_name = dbConnection.select("SELECT user_id FROM Users WHERE name = " + new_assigned_name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(task.equals("chore")){
                            dbConnection.modify("UPDATE ChoreUsers SET user_id = '" + assigned_name[0] + "' WHERE chore_id = " + task_id);
                        }else if(task.equals("transaction")){
                            dbConnection.modify("UPDATE Transactions SET user_id = '" + assigned_name[0] + "' WHERE transaction_id = " + task_id);
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Edited successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Edit.this , MainMenu.class));
                }
            });
        }
        private String getText(EditText textBox) {
            String text = textBox.getText().toString();
            return text;
        }


    }
}