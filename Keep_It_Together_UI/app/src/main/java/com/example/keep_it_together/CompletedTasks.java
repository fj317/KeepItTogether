package com.example.keep_it_together;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;

public class CompletedTasks extends AppCompatActivity {
    Client dbConnection = null;
    RecyclerView recyclerView;
    RecyclerAdapterCompletedTasks adapter;
    String[] allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        Spinner spinner = findViewById(R.id.timeFrameSspinner);

        String[] classes = new String[] {"Today", "7 days", "All"};
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, classes);
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(allData != null){
                    int[] days = new int[] {31,28,31,30,31,30,31,31,30,31,30,31};
                    ArrayList<TaskListData> temp = new ArrayList<TaskListData>();
                    @SuppressLint({"NewApi", "LocalSuppress"}) String date = String.valueOf(java.time.LocalDate.now());
                    if(i == 0){
                        for(int j = 0; j < allData.length/3; j++) {
                            if (allData[j * 3 + 1].equals(date)) {
                                temp.add(new TaskListData(allData[j * 3], allData[j * 3 + 1], allData[j * 3 + 2]));
                            }
                        }
                        TaskListData[] data = new TaskListData[temp.size()];
                        for(int k = 0; k < temp.size(); k++){
                            data[k] = temp.get(k);
                        }
                        adapter = new RecyclerAdapterCompletedTasks(data);
                        recyclerView.setAdapter(adapter);
                    }
                    else if(i == 1){
                        int datePart1 = Integer.parseInt(date.substring(0,4));
                        int datePart2 = Integer.parseInt(date.substring(5,7));
                        int datePart3 = Integer.parseInt(date.substring(8));
                        if(datePart3 > 7){
                            datePart3 = datePart3 - 7;
                        }
                        else{
                            if(!(datePart2 == 1)){
                                datePart2 = datePart2 - 1;
                            }
                            else{
                                datePart2 = 12;
                                datePart1 = datePart1 - 1;
                            }
                            datePart3 = days[datePart2];
                        }
                        String strDatePart2 = String.valueOf(datePart2);
                        if(datePart2 < 10){
                            strDatePart2 = "0" + datePart2;
                        }
                        String strDatePart3 = String.valueOf(datePart3);
                        if(datePart3 < 10){
                            strDatePart3 = "0" + datePart3;
                        }
                        date = datePart1 + "-" + strDatePart2 + "-" + strDatePart3;

                        for(int j = 0; j < allData.length/3; j++) {
                            if (checkDate(allData[j * 3 + 1],date)) {
                                temp.add(new TaskListData(allData[j * 3], allData[j * 3 + 1], allData[j * 3 + 2]));
                            }
                        }
                        TaskListData[] data = new TaskListData[temp.size()];
                        for(int k = 0; k < temp.size(); k++){
                            data[k] = temp.get(k);
                        }
                        adapter = new RecyclerAdapterCompletedTasks(data);
                        recyclerView.setAdapter(adapter);

                    }
                    else{
                        for(int j = 0; j < allData.length/3; j++) {
                            temp.add(new TaskListData(allData[j * 3], allData[j * 3 + 1], allData[j * 3 + 2]));
                        }
                        TaskListData[] data = new TaskListData[temp.size()];
                        for(int k = 0; k < temp.size(); k++){
                            data[k] = temp.get(k);
                        }
                        adapter = new RecyclerAdapterCompletedTasks(data);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    private boolean checkDate(String s1, String s2){
        int s1d1 = Integer.parseInt(s1.substring(0,4));
        int s1d2 = Integer.parseInt(s1.substring(5,7));
        int s1d3 = Integer.parseInt(s1.substring(8));

        int s2d1 = Integer.parseInt(s2.substring(0,4));
        int s2d2 = Integer.parseInt(s2.substring(5,7));
        int s2d3 = Integer.parseInt(s2.substring(8));
        if (s1d1 > s2d1){
            return true;
        }
        else if(s1d2 > s2d2){
            return true;
        }
        else return s1d3 > s2d3;

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
            String request = "SELECT name, date_completed, description FROM ChoreRecords INNER JOIN Chores ON ChoreRecords.chore_id = Chores.chore_id INNER JOIN Users ON ChoreRecords.user_id = Users.user_id";
            String[] response;
            try{
                response  = dbConnection.select(request);
                if (!response[0].isEmpty()){
                    allData = response;
                    TaskListData[] data = new TaskListData[response.length/3];
                    for(int i = 0; i < response.length/3; i++){
                        data[i] = new TaskListData(response[3*i], response[3*i+1], response[3*i+2]);
                    }
                    recyclerView = (RecyclerView) findViewById(R.id.completedTasksRecycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new RecyclerAdapterCompletedTasks(data);
                    recyclerView.setAdapter(adapter);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
