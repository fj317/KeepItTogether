package com.example.keep_it_together;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.*;

public class GraphsPage extends AppCompatActivity {

    TabsAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    Client dbConnection;
    String[] choreData;
    String[] transactionData;
    PieChart pieChartChore;
    PieChart pieChartTransaction;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.graphPager);
        Spinner datePicker = findViewById(R.id.datePicker);
        date = "2021-02-03";

        String[] dates = new String[] {"Today","This week","All time"};

        datePicker.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, dates));

        adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChoresFragment(), "Chores");
        adapter.addFragment(new TransactionFragment(), "Transactions");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AsyncTaskRunnerTransaction runner = new AsyncTaskRunnerTransaction();
        runner.execute();
        AsyncTaskRunnerChore runner2 = new AsyncTaskRunnerChore();
        runner2.execute();

        datePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateChoreGraph();
                updateTransactionGraph();
                //Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
                int[] days = new int[] {31,28,31,30,31,30,31,31,30,31,30,31};
                if (i == 0){
                    date = String.valueOf(java.time.LocalDate.now());
                }
                if (i == 1){
                    date = String.valueOf(java.time.LocalDate.now());
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

                }
                if (i == 2){
                    date = "1700-0-0";
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(date), Toast.LENGTH_LONG).show();
                /*
                AsyncTaskRunnerTransaction runner = new AsyncTaskRunnerTransaction();
                runner.execute();
                AsyncTaskRunnerChore runner2 = new AsyncTaskRunnerChore();
                runner2.execute();

                 */
                updateChoreGraph();
                updateTransactionGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1){
                    updateTransactionGraph();

                }
                if(tab.getPosition() == 0){
                    updateChoreGraph();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


    }

    public void updateChoreGraph(){
        pieChartChore = findViewById(R.id.choresPieChart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        if (choreData == null){
            pieChartChore.setNoDataText("loading data ... ");
            return;
        }

        Map<String, Float> typeAmountMap = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();
        String[] c = new String[] {"#304567", "#309967", "#476567", "#890567","#a35567","#ff5f67","#3ca567", "#b30527"};
        for(int i = 0; i < choreData.length/2; i++){
            typeAmountMap.put(choreData[i*2],Float.parseFloat(choreData[i*2+1]));
            colors.add(Color.parseColor(c[i]));
        }

        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "user");
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        //pieData.setDrawValues(true);
        Legend l = pieChartChore.getLegend();
        l.setEnabled(false);
        pieChartChore.setData(pieData);
        pieChartChore.setDrawHoleEnabled(false);
        pieChartChore.invalidate();
    }

    public void updateTransactionGraph(){

        pieChartTransaction = findViewById(R.id.transactionsPieChart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        if (transactionData == null){
            pieChartTransaction.setNoDataText("loading data ... ");
            return;
        }
        Map<String, Float> typeAmountMap = new HashMap<>();
        ArrayList<Integer> colors = new ArrayList<>();
        String[] c = new String[] {"#304567", "#309967", "#476567", "#890567","#a35567","#ff5f67","#3ca567", "#b30527"};
        for(int i = 0; i < transactionData.length/2; i++){
            typeAmountMap.put(transactionData[i*2],Float.parseFloat(transactionData[i*2+1]));
            colors.add(Color.parseColor(c[i]));
        }

        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "user");
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        //pieData.setDrawValues(true);
        Legend l = pieChartTransaction.getLegend();
        l.setEnabled(false);
        pieChartTransaction.setData(pieData);
        pieChartTransaction.setDrawHoleEnabled(false);
        pieChartTransaction.invalidate();
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunnerTransaction extends AsyncTask<String, String, String> {
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
            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            String house_id = preferences.getString("houseID", "");
            //String[] users = dbConnection.select("SELECT user_id FROM HouseUsers WHERE house_id = " + house_id);
            //String[] transactions = dbConnection.select("SELECT price FROM Transactions WHERE date > '" + date.toString() + "' AND user_id = " + user);
            String[] response;
            String request = "SELECT name, SUM(price) FROM Users INNER JOIN Transactions ON Users.user_id = Transactions.user_id  WHERE date > '" + date.toString() + "' AND house_id = '" + house_id + "' GROUP BY name";
            try{
                response  = dbConnection.select(request);
                if (!response[0].isEmpty()){
                    transactionData = response;

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunnerChore extends AsyncTask<String, String, String> {
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
            String[] response;
            String request = "SELECT user_id, COUNT(user_id) FROM ChoreRecords WHERE date_completed > '" + date.toString() + "' GROUP BY user_id";
            try{
                response  = dbConnection.select(request);
                if (!response[0].isEmpty()){
                    String[] names = new String[response.length/2];
                    for(int i = 0; i< response.length/2; i++){
                        names[i] = dbConnection.select("SELECT name FROM Users WHERE user_id = " + response[i*2])[0];
                    }
                    for(int i = 0; i < response.length/2; i++){
                        response[i*2] = names[i];
                    }
                    choreData = response;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}

