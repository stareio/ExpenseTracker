package com.reginio.expensetracker;

import androidx.appcompat.app.*;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class AddRecordActivity extends AppCompatActivity {

    TextView addExpIncTv, addCatSpnrTv, addDateTv, addSpecifCatTv, addAmtCurrTv;
    EditText addNameEt, addAmtEt;
    Spinner addExpIncSpnr, addCatSpnr;
    Switch addSpecifCatSw;
    Button addDateBtn, addRecordBtn;

    String LOG_TAG = "Debugging";

    String entryType = "";
    String category = "";
    Boolean isIncome = false;
    Boolean isSpecifCategory = false;
    int addYear;
    int addMonth;
    int addDay;

    SharedPreferences sp;
    EntryFormatter ef;
    EntryValidator ev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        addNameEt = findViewById(R.id.etAddName);
        addAmtEt = findViewById(R.id.etAddAmt);

        addExpIncTv = findViewById(R.id.tvAddExpInc);
        addCatSpnrTv = findViewById(R.id.tvAddCatSpnr);
        addDateTv = findViewById(R.id.tvAddDate);
        addSpecifCatTv = findViewById(R.id.tvAddSpecifCatSw);
        addAmtCurrTv = findViewById(R.id.tvAddAmtCurr);

        addExpIncSpnr = findViewById(R.id.spnrAddExpInc);
        addCatSpnr = findViewById(R.id.spnrAddCat);

        addSpecifCatSw = findViewById(R.id.swAddSpecifCat);
        addDateBtn = findViewById(R.id.btnAddDate);

        addRecordBtn = findViewById(R.id.btnAddRecord);

        ef = new EntryFormatter();
        ev = new EntryValidator();

        // populate the dropdown list for expenses/income
        getExIncSpinner();

        // set default date to current date
        getDateToday();

        // get selected item in expense/income spinner
        addExpIncSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                entryType = addExpIncSpnr.getSelectedItem().toString();
                Log.d(LOG_TAG, "entryType: " + entryType);
                addExpIncTv.setText(entryType);

                if (entryType.equals("Income")) {
                    isIncome = true;
                } else {
                    isIncome = false;
                }

                Log.d(LOG_TAG, "isIncome: " + isIncome);

                // populate category spinner
                // if expense is selected, items are car, food, drinks, etc.
                // if income is selected, items are csalary, pocket money, etc.
                getCatSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                entryType = "";
            }
        });

        // get value of switch
        // if enabled, enable and populate category spinner
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
            Log.d(LOG_TAG, "isSpecifCategory: " + isSpecifCategory);
        });

        // get selected item in category spinner
        addCatSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                category = addCatSpnr.getSelectedItem().toString();
                addCatSpnrTv.setText(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                entryType = "";
            }
        });

        // date picker
        addDateBtn.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(
                    AddRecordActivity.this,
                    (dp, year, month, day) -> {
                        addYear = year;
                        addMonth = month;
                        addDay = day;

                        Log.d(LOG_TAG, "year: " + year);
                        Log.d(LOG_TAG, "month: " + month + " + 1 :)");
                        Log.d(LOG_TAG, "day: " + day);

                        // update TextView for date
                        addDateTv.setText(ef.formatDate(year, month, day));
                    },
                    // pass selected date in date picker
                    addYear, addMonth, addDay
            );

            // show date picker dialog
            dpd.show();
        });

        // submit inputs
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = addNameEt.getText().toString();
                // reformat amount to only have 2 decimal places or none
                String amount = ef.formatAmountValue(addAmtEt.getText().toString());
                String date = addYear + "-" + addMonth + "-" + addDay;

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
                    DBHandler dbHandler = new DBHandler(AddRecordActivity.this);
                    dbHandler.addRecord(entryType, name, category, amount, date);

                    Log.d(LOG_TAG, "=== Values stored in database ===================");
                    Log.d(LOG_TAG, "entryType: " + entryType);
                    Log.d(LOG_TAG, "name: " + name);
                    Log.d(LOG_TAG, "category: " + category);
                    Log.d(LOG_TAG, "date: " + date);
                    Log.d(LOG_TAG, "amount: " + amount);

                    // go back to previous page
                    onBackPressed();

                    // toast for successful submission
                    Toast.makeText(getApplicationContext(),
                            "Entry has been submitted successfully!",
                            Toast.LENGTH_LONG).show();
                } else {
                    String message = "Please provide ";
                    List<String> invalidInputs = new ArrayList<>();

                    if (!checkName) {
                        invalidInputs.add("a name within 20 characters");
                    } if (!checkAmt) {
                        invalidInputs.add("an amount > 0 and digits between 1-10");
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

    // user-defined methods =========================================================
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
        List<String> itemsCat = new ArrayList<>();

        if (isIncome) {
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
        addCatSpnr.setAdapter(adapter);

        // disable/enable spinner when instantiated or updated
        if (isSpecifCategory) {
            addCatSpnr.setEnabled(true);
        } else {
            addCatSpnr.setEnabled(false);
        }
    }

    private void getDateToday() {
        final Calendar cal = Calendar.getInstance();

        addYear = cal.get(Calendar.YEAR);
        addMonth = cal.get(Calendar.MONTH);
        addDay = cal.get(Calendar.DAY_OF_MONTH);

        // set default date to current date
        addDateTv.setText(ef.formatDate(addYear, addMonth, addDay));
    }
}

/*
Reference
Dropdown list: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
Customized spinner: https://www.youtube.com/watch?v=N8GfosWTt44
Set opacity for TextView: https://stackoverflow.com/questions/38434108/set-opacity-for-textview
Date picker: https://www.geeksforgeeks.org/datepicker-in-android/
Date formatter, Display current date: https://www.youtube.com/watch?v=qCoidM98zNk
Print elements from array with comma in between: https://stackoverflow.com/questions/18279622/print-out-elements-from-an-array-with-a-comma-between-the-elements
 */