package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;

public class AddRecordActivity extends AppCompatActivity {

    TextView addExpIncTv, addCatSpnrTv, addDateTv;
    Spinner addExpIncSpnr, addCatSpnr;
    Switch addSpecifCatSw;
    DatePickerDialog dpd;
    Button addDateBtn;

    String LOG_TAG = "Debugging";

    String entryType = "";
    String category = "";
    Boolean isIncome = false;
    Boolean isSpecifCategory = false;
    int addYear;
    int addMonth;
    int addDay;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        addExpIncTv = findViewById(R.id.tvAddExpInc);
        addCatSpnrTv = findViewById(R.id.tvAddCatSpnr);
        addDateTv = findViewById(R.id.tvAddDate);

        addExpIncSpnr = findViewById(R.id.spnrAddExpInc);
        addCatSpnr = findViewById(R.id.spnrAddCat);

        addSpecifCatSw = findViewById(R.id.swAddSpecifCat);
        addDateBtn = findViewById(R.id.btnAddDate);

        // populate the dropdown list for expenses/income
        getExIncSpinner();

        // date picker
        initDatePicker();
        addDateBtn.setText(getDateToday());

        // get selected item in expense/income spinner
        addExpIncSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                entryType = addExpIncSpnr.getSelectedItem().toString();
                addExpIncTv.setText(entryType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                entryType = "";
            }
        });

        // get value of switch
        // if enabled, enable category spinner
        addSpecifCatSw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addCatSpnr.setEnabled(true);

                // set opacity to 100%
                addCatSpnrTv.setAlpha(1f);
                addCatSpnr.setAlpha(1f);
            } else {
                addCatSpnr.setEnabled(false);

                // set opacity to 35%
                addCatSpnrTv.setAlpha(0.35f);
                addCatSpnr.setAlpha(0.35f);
            }

            isSpecifCategory = isChecked;
            getCatSpinner();
        });

        // get selected item in category spinner
        addCatSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                entryType = addCatSpnr.getSelectedItem().toString();
                addCatSpnrTv.setText(entryType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                entryType = "";
            }
        });

        // set to dark/light mode
        sp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        Boolean isDark = sp.getBoolean("night", false);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void getExIncSpinner() {
        // create a list of items for each spinner
        String[] itemsExpInc = new String[]{"Expense", "Income"};

        // create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner,
                itemsExpInc
        );

        // set the spinner adapter to the previously created one
        addExpIncSpnr.setAdapter(adapter);
    }

    private void getCatSpinner() {
        String[] itemsCat;

        if (isIncome) {
            itemsCat = new String[]{
                    "Salary",
                    "Savings",
                    "Commissions",
                    "Pocket Money"
            };
        } else {
            itemsCat = new String[]{
                    "Food",
                    "Drinks",
                    "Groceries",
                    "Rental",
                    "Commute",
                    "Mobile Account",
                    "Entertainment"
            };
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner,
                itemsCat
        );

        addCatSpnr.setAdapter(adapter);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dsl = (datePicker, year, month, day) -> {
            month += 1;
            String date = getDateString(year, month, day);
            addDateTv.setText(date);

            addYear = year;
            addMonth = month;
            addDay = day;

            Log.d(LOG_TAG, "addYear: " + addYear);
            Log.d(LOG_TAG, "addMonth: " + addMonth);
            Log.d(LOG_TAG, "addDay: " + addDay);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(this, dsl, year, month, day);
    }

    private String getDateString(int year, int month, int day) {
        return formatMonth(month) + " " + day + ", " + year;
    }

    private String formatMonth(int month) {
        String[] monthList = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        return monthList[month-1];
    }

    private String getDateToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return getDateString(year, month, day);
    }

    public void openDatePicker(View view) {
        dpd.show();
    }
}

/*
Reference
Dropdown list: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
Customized spinner: https://www.youtube.com/watch?v=N8GfosWTt44
Set opacity for TextView: https://stackoverflow.com/questions/38434108/set-opacity-for-textview
Popup date picker: https://www.youtube.com/watch?v=qCoidM98zNk
 */