package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditRecordActivity extends AppCompatActivity {

    TextView editExpIncTv, editCatSpnrTv, editDateTv, editSpecifCatTv, editAmtCurrTv;
    EditText editNameEt, editAmtEt;
    Spinner editExpIncSpnr, editCatSpnr;
    Switch editSpecifCatSw;
    Button editDateBtn, editRecordBtn;

    String LOG_TAG = "Debugging";

    int recordId;
    String entryType = "";
    String category = "";
    Boolean isIncome = false;
    Boolean isSpecifCategory = false;
    int editYear;
    int editMonth;
    int editDay;

    SharedPreferences sp;
    EntryFormatter ef;
    EntryValidator ev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        editNameEt = findViewById(R.id.etEditName);
        editAmtEt = findViewById(R.id.etEditAmt);

        editExpIncTv = findViewById(R.id.tvEditExpInc);
        editCatSpnrTv = findViewById(R.id.tvEditCatSpnr);
        editDateTv = findViewById(R.id.tvEditDate);
        editSpecifCatTv = findViewById(R.id.tvEditSpecifCatSw);
        editAmtCurrTv = findViewById(R.id.tvEditAmtCurr);

        editExpIncSpnr = findViewById(R.id.spnrEditExpInc);
        editCatSpnr = findViewById(R.id.spnrEditCat);

        editSpecifCatSw = findViewById(R.id.swEditSpecifCat);
        editDateBtn = findViewById(R.id.btnEditDate);

        editRecordBtn = findViewById(R.id.btnEditRecord);

        ef = new EntryFormatter();
        ev = new EntryValidator();

        // populate the dropdown list for expenses/income
        getExIncSpinner();

        // set date to old date
        // set everything!!

        // set currency
        getCurrency();

        // get selected item in expense/income spinner
        editExpIncSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                entryType = editExpIncSpnr.getSelectedItem().toString();
                Log.d(LOG_TAG, "entryType: " + entryType);
                editExpIncTv.setText(entryType);

                if (entryType.equals("Income")) {
                    isIncome = true;
                } else {
                    isIncome = false;
                }

                Log.d(LOG_TAG, "isIncome: " + isIncome);

                // populate category spinner
                // if expense is selected, items are car, food, drinks, etc.
                // if income is selected, items are commissions, salary, pocket money, etc.
                getCatSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                entryType = "";
            }
        });

        // get value of switch
        // if enabled, enable and populate category spinner
        editSpecifCatSw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editCatSpnr.setEnabled(true);

                // set opacity to 100%
                editCatSpnrTv.setAlpha(1f);
                editCatSpnr.setAlpha(1f);
            } else {
                editCatSpnr.setEnabled(false);

                // set opacity to 35%
                editCatSpnrTv.setAlpha(0.35f);
                editCatSpnr.setAlpha(0.35f);
            }

            isSpecifCategory = isChecked;
            Log.d(LOG_TAG, "isSpecifCategory: " + isSpecifCategory);
        });

        // get selected item in category spinner
        editCatSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                category = editCatSpnr.getSelectedItem().toString();
                editCatSpnrTv.setText(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                entryType = "";
            }
        });

        // date picker
        editDateBtn.setOnClickListener(view -> {
            final Calendar cal = Calendar.getInstance();

            editYear = cal.get(Calendar.YEAR);
            editMonth = cal.get(Calendar.MONTH);
            editDay = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(
                    EditRecordActivity.this,
                    (dp, year, month, day) -> {
                        Log.d(LOG_TAG, "year: " + year);
                        Log.d(LOG_TAG, "month: " + month + " + 1 :)");
                        Log.d(LOG_TAG, "day: " + day);

                        // update TextView for date
                        editDateTv.setText(ef.formatDate(year, month, day));
                    },
                    // pass selected date in date picker
                    editYear, editMonth, editDay
            );

            // show date picker dialog
            dpd.show();
        });

        // submit inputs
        editRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NTS: retrieve record id first

                String name = editNameEt.getText().toString();
                // reformat amount to only have 2 decimal places or none
                String amount = ef.formatAmount(editAmtEt.getText().toString());
                String date = editYear + "-" + editMonth + "-" + editDay;

                Log.d(LOG_TAG, "=== Values NOT stored in database yet ===========");
                Log.d(LOG_TAG, "entryType: " + entryType);
                Log.d(LOG_TAG, "name: " + name);
                Log.d(LOG_TAG, "category: " + category);
                Log.d(LOG_TAG, "date: " + date);
                Log.d(LOG_TAG, "amount: " + amount);

                // Check if entry is valid
                boolean checkName = ev.checkName(name);
                boolean checkAmt = ev.checkAmount(amount);
                boolean isEntryValid = checkName && checkAmt;
                Log.d(LOG_TAG, "isEntryValid: " + isEntryValid);

                // check for invalid inputs
                if (isEntryValid) {
                    // if specific category is not toggled
                    if (!isSpecifCategory) {
                        category = "";
                    }

                    // store entry in database
                    DBHandler dbHandler = new DBHandler(EditRecordActivity.this);
                    dbHandler.updateRecord(entryType, name, category, amount, date, recordId);

                    Log.d(LOG_TAG, "=== Values stored in database ===================");
                    Log.d(LOG_TAG, "entryType: " + entryType);
                    Log.d(LOG_TAG, "name: " + name);
                    Log.d(LOG_TAG, "category: " + category);
                    Log.d(LOG_TAG, "date: " + date);
                    Log.d(LOG_TAG, "amount: " + amount);

                    // toast for successful submission
                    Toast.makeText(getApplicationContext(),
                            "Record has been edited successfully!",
                            Toast.LENGTH_LONG).show();
                } else {
                    String message = "Please provide ";
                    List<String> invalidInputs = new ArrayList<>();

                    if (!checkName) {
                        invalidInputs.add("a name within 20 characters");
                    } if (!checkAmt) {
                        invalidInputs.add("an amount between 1-10 digits");
                    }

                    String separator = "";
                    for (String s : invalidInputs) {
                        message += (separator + s);
                        separator = ", and ";
                    }

                    Log.d(LOG_TAG, message + ".");

                    // toast for unsuccessful submission
                    Toast.makeText(getApplicationContext(),
                            message + ".",
                            Toast.LENGTH_LONG).show();
                }
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

    // user-defined methdos =========================================================
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
        editExpIncSpnr.setAdapter(adapter);
    }

    private void getCatSpinner() {
        List<String> itemsCat = new ArrayList<>();

        if (isIncome) {
            itemsCat.add("Commissions");
            itemsCat.add("Pocket Money");
            itemsCat.add("Salary");
            itemsCat.add("Savings");
        } else {
            itemsCat.add("Car");
            itemsCat.add("Commute");
            itemsCat.add("Drinks");
            itemsCat.add("Food");
            itemsCat.add("Groceries");
            itemsCat.add("Medicine");
            itemsCat.add("Rental");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner,
                itemsCat
        );

        // update spinner items
        editCatSpnr.setAdapter(adapter);

        // disable/enable spinner when instantiated or updated
        if (isSpecifCategory) {
            editCatSpnr.setEnabled(true);
        } else {
            editCatSpnr.setEnabled(false);
        }
    }

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
                editAmtCurrTv.setText(currToRead);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception: " + e);
            }
        } else {
            editAmtCurrTv.setText("PHP");
        }
    }
}