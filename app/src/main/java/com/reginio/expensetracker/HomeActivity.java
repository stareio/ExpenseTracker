package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.*;
import android.app.DatePickerDialog.*;
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
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private TextView incomeText, expenseText;
    String totalIncome = "12000";
    String totalExpense = "200";

    //Pie Chart Object
    PieChart pieChart;

    //Settings Button
    ImageButton settingsBtn;

    //List of Records
    ListView lv;
    ArrayList<String> recordIds;
    RecordAdapter recordAdapter;
    String currency;

    //Others
    String LOG_TAG = "Debugging";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Find ID
        dateButton = findViewById(R.id.datePicker_btn);
        incomeText = findViewById(R.id.income_txt);
        expenseText = findViewById(R.id.expenses_txt);

        //Date Picker Button
        initDatePicker();
        dateButton.setText(getTodaysDate());

        //Pie Chart Implementation
        pieChart = findViewById(R.id.piechart);

        //add data to piechart
        setData();

        //Set Text from value
        incomeText.setText(totalIncome);
        expenseText.setText(totalExpense);

        //Settings Page
        settingsBtn = findViewById(R.id.ibSettings);
        settingsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        //List of Records
        lv = findViewById(R.id.lvHomeRecords);

        getList();  // retrieve list of records

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

        pieChart.addPieSlice(
                new PieModel(
                        "Income",
                        //Integer.parseInt(incomeText.getText().toString()),
                        Integer.parseInt(totalIncome),
                        Color.parseColor("#856214")
                )
        );
        pieChart.addPieSlice(
                new PieModel(
                        "Income",
                        //Integer.parseInt(expenseText.getText().toString()),
                        Integer.parseInt(totalExpense),
                        Color.parseColor("#213933")
                )
        );

        //To animate pie chart
        pieChart.startAnimation();
    }

    private void getData() {

        // for loop
    }

    //FOR DATE PICKER ==============================================================================
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void initDatePicker() {
            DatePickerDialog.OnDateSetListener dateSetListener = new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    dateButton.setText(date);
                }
            };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

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

    public void openDatePicker(View view) {
        datePickerDialog.show();
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
        ArrayList<HashMap<String,String>> recordsList = db.getRecords();

        int count = 0;
        for (Map<String,String> map : recordsList) {
            recordIds.add(map.get("id"));
            Log.d(LOG_TAG, "Stored id " + map.get("id") + " at index " + count);
            count++;
        }
        Log.d(LOG_TAG, "recordIds after for loop:" + recordIds);

        // get currency set in app
        getCurrency();

        recordAdapter = new RecordAdapter(HomeActivity.this, recordsList,
                currency, this);
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
        }
    }

    // update the displayed list of records
    @Override
    protected void onResume() {
        super.onResume();
        recordIds.clear();
        getList();
        ((BaseAdapter) recordAdapter).notifyDataSetChanged();
    }

    // get selected currency of user
    private void getCurrency() {
        // check if settings file exists
        if (getBaseContext().getFileStreamPath("ExpenseTracker_Settings.txt").exists()) {
            try {
                FileInputStream fis = openFileInput("ExpenseTracker_Settings.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                // only retrieve stored currency value
                br.readLine();
                String currToRead = br.readLine();
                Log.d(LOG_TAG, "currToRead: " + currToRead);

                br.close();
                isr.close();
                fis.close();

                // update the displayed currency
                currency = currToRead;
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception: " + e);
            }
        } else {
            currency = "PHP";
        }
    }
}