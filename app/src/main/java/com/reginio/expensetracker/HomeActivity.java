package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HomeActivity extends AppCompatActivity implements OnEditRecordSpnrSelect {
    //Instantiations
  
    //Date Picker Object
    Button dateChangerBtn;
    TextView incomeText, expenseText, balanceText;
    Double totalIncome;
    Double totalExpense;
    Double totalBalance;

    //Pie Chart Object
    PieChart pieChart;

    //Settings Button
    ImageButton settingsBtn;

    //Add Record Button
    ImageButton addRecordBtn;

    //List of Records
    ListView lv;
    String date;
    ArrayList<String> recordIds;
    RecordAdapter recordAdapter;

    //Others
    String nameToGreet;
    EntryFormatter ef;
    String LOG_TAG = "Debugging";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Formatter
        ef = new EntryFormatter();

        //Set Text from value
        dateChangerBtn = findViewById(R.id.dateChange_btn);
        incomeText = findViewById(R.id.income_txt);
        expenseText = findViewById(R.id.expenses_txt);
        balanceText = findViewById(R.id.balance_txt);

        //Date Changer Button
        dateChangerBtn.setText(getTodaysDate());

        //Settings Page
        settingsBtn = findViewById(R.id.ibSettings);
        settingsBtn.setOnClickListener(view -> {
            Intent toSettings = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(toSettings);
        });

        dateChangerBtn = findViewById(R.id.dateChange_btn);
        dateChangerBtn.setText(getTodaysDate());
        dateChangerBtn.setOnClickListener(view -> {
            Intent toCheckRec = new Intent(HomeActivity.this, CheckRecordActivity.class);
            startActivity(toCheckRec);
        });

        //Add Record Page
        addRecordBtn = findViewById(R.id.ibAddRecord);
        addRecordBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddRecordActivity.class);
            startActivity(intent);
        });

        //List of Records
        lv = findViewById(R.id.lvHomeRecords);

        // retrieve list of records
        getList();

        //Pie Chart Implementation
        pieChart = findViewById(R.id.piechart);

        //Greeting
        // check for saved username
        readSettings();
      
        // greet user
        Toast.makeText(getApplicationContext(),
                "Hello, " + nameToGreet,
                Toast.LENGTH_LONG).show();
        
        //Dark/Light Mode
        sp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        Boolean isDark = sp.getBoolean("night", false);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //FOR PIE CHART ================================================================================
    private void setData() {

        pieChart.clearChart();

        pieChart.addPieSlice(
                new PieModel(
                        "Income",
                        //Integer.parseInt(incomeText.getText().toString()),
                        totalIncome.floatValue(),
                        Color.parseColor("#65BCBF")
                )
        );
        pieChart.addPieSlice(
                new PieModel(
                        "Income",
                        //Integer.parseInt(expenseText.getText().toString()),
                        totalExpense.floatValue(),
                        Color.parseColor("#F8777D")
                )
        );

        //To animate pie chart
        pieChart.startAnimation();
    }

    //FOR DATE PICKER ==============================================================================
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // used later to retrieve records
        date = year + "-" + month + "-" + day;
        Log.d(LOG_TAG, "date to retrieve records: " + date);

        // add 1 to month since its range of values is 0-11
        month = month + 1;

        return makeDateString(day,month,year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1 : return "JAN";
            case 2 : return "FEB";
            case 3 : return "MAR";
            case 4 : return "APR";
            case 5 : return "MAY";
            case 6 : return "JUN";
            case 7 : return "JUL";
            case 8 : return "AUG";
            case 9 : return "SEP";
            case 10 : return "OCT";
            case 11 : return "NOV";
            case 12 : return "DEC";
            default : return "JAN";
        }
    }
    // FOR LIST OF RECORDS =========================================================================
    // retrieve modify value from RecordAdapter
    @Override
    public void onItemSelectedListener(String modify, int id) {
        Log.d(LOG_TAG, "MainActivity ==========================");
        Log.d(LOG_TAG, "modify in MainActivity: " + modify);
        Log.d(LOG_TAG, "adapter position: " + id);

        if (!modify.equals("")) {
            modifyRecord(modify, id);
        }
    }

    // get list of records
    private void getList() {
        DBHandler db = new DBHandler(this);
        recordIds = new ArrayList<>();
        ArrayList<HashMap<String,String>> recordsList = db.getRecordsbyDate(date);

        //Initalize totalIncome, totalExpense, totalBalance
        totalIncome = 0.00;
        totalExpense = 0.00;
        totalBalance = 0.00;

        int count = 0;
        for (Map<String,String> map : recordsList) {
            recordIds.add(map.get("id"));
            Log.d(LOG_TAG, "Stored id " + map.get("id") + " at index " + count);

            String type = map.get("type");
            Double amount = Double.parseDouble(map.get("amount"));
            if (type.equals("Expense")) {
                totalExpense += amount;
                Log.d(LOG_TAG, "added expense: " + amount);
            } else if (type.equals("Income")) {
                totalIncome += amount;
                Log.d(LOG_TAG, "added income: " + amount);
            }
            totalBalance = totalIncome - totalExpense;
            count++;
        }
        Log.d(LOG_TAG, "recordIds after for loop: " + recordIds);

        recordAdapter = new RecordAdapter(HomeActivity.this, recordsList, this);
        lv.setAdapter(recordAdapter);

        db.close();
    }

    // edit/delete the record
    private void modifyRecord(String modify, int id) {
        String recordId = recordIds.get(id);
        Log.d(LOG_TAG, "recordId: " + recordId);

        if (modify.equals("Edit")) {
            // send the recordId to the edit record page for editing
            Log.d(LOG_TAG, "Record to be edited");

            Intent i = new Intent(HomeActivity.this, EditRecordActivity.class);
            i.putExtra("id", recordId);
            startActivity(i);
        } else if (modify.equals("Delete")) {
            // delete the record from the database
            Log.d(LOG_TAG, "Record deleted");

            DBHandler db = new DBHandler(this);
            db.deleteRecord(Integer.parseInt(recordId));

            recordIds.clear();
            getList();
            ((BaseAdapter) recordAdapter).notifyDataSetChanged();

            //add data to piechart
            setData();

            //Set Text from value
            incomeText.setText(ef.formatCurrAmount(ef.formatAmountValue(String.valueOf(totalIncome))));
            expenseText.setText(ef.formatCurrAmount(ef.formatAmountValue(String.valueOf(totalExpense))));
            balanceText.setText(ef.formatCurrAmount(ef.formatAmountValue(String.valueOf(totalBalance))));
        }
    }

    // update the displayed list of records
    @Override
    protected void onResume() {
        super.onResume();
        recordIds.clear();
        getList();
        ((BaseAdapter) recordAdapter).notifyDataSetChanged();

        //add data to piechart
        setData();

        //Set Text from value
        incomeText.setText(ef.formatCurrAmount(ef.formatAmountValue(String.valueOf(totalIncome))));
        expenseText.setText(ef.formatCurrAmount(ef.formatAmountValue(String.valueOf(totalExpense))));
        balanceText.setText(ef.formatCurrAmount(ef.formatAmountValue(String.valueOf(totalBalance))));
    }

    // FOR GREETING NAME ===========================================================================
    public void readSettings() {
        // check if file exists
        if (getBaseContext().getFileStreamPath("ExpenseTracker_Settings.txt").exists()) {
            Log.d(LOG_TAG, "Settings file exists");

            try {
                FileInputStream fis = openFileInput("ExpenseTracker_Settings.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                // get name
                String name = br.readLine();
                if (name.equals("")) {
                    nameToGreet = "user";
                } else {
                    nameToGreet = name;
                }


                Log.d(LOG_TAG, "nameToRead: " + nameToGreet);

                br.close();
                isr.close();
                fis.close();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception: " + e);
            }
        } else {
            nameToGreet = "user";
            Log.d(LOG_TAG, "Settings file does NOT exist");
        }

        Log.d(LOG_TAG, "nameToGreet: " + nameToGreet);
    }
}

/*
How to update values in RecyclerView on onResume(): https://stackoverflow.com/questions/62984025/how-can-i-refresh-recylverview-item-when-i-press-back
notifyDataSetChanged() not showing up: https://stackoverflow.com/questions/32261572/notifydatasetchanged-not-showing-up-for-custom-list-adapter
*/