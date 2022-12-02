package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.*;
import android.content.*;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.util.*;

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

        // set currency
        getCurrency();

        // get record and display its values on each input widget like EditText, Spinner, etc.
        getRecord();

        // get selected item in expense/income spinner
        editExpIncSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                entryType = editExpIncSpnr.getSelectedItem().toString();
                editExpIncTv.setText(entryType);
                Log.d(LOG_TAG, "entryType: " + entryType);

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
            }
        });

        // date picker
        editDateBtn.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(
                    EditRecordActivity.this,
                    (dp, year, month, day) -> {
                        editYear = year;
                        editMonth = month;
                        editDay = day;

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

                    // go back to previous page
                    onBackPressed();

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
                        invalidInputs.add("an amount > 0 and digits between 1-10");
                    }

                    String separator = "";
                    for (String s : invalidInputs) {
                        message += (separator + s);
                        separator = ", and ";
                    }

                    Log.d(LOG_TAG, message + ".");

                    // go back to previous page
                    onBackPressed();

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
    private void getRecord() {
        // NTS: retrieve recordId from check records page or home page
        recordId = 1;   // for debugging
        String type = "", name = "", amount = "", date = "";

        // retrieve record from database
        DBHandler db = new DBHandler(this);
        ArrayList<HashMap<String,String>> record = db.getRecordById(recordId);

        for (Map<String,String> map : record) {
            type = map.get("type");
            name = map.get("name");
            category = map.get("category");
            amount = map.get("amount");
            date = map.get("date");
        }

        Log.d(LOG_TAG, "== Entry values retrieved for editing ===============");
        Log.d(LOG_TAG, "type: " + type);
        Log.d(LOG_TAG, "name: " + name);
        Log.d(LOG_TAG, "category: " + category);
        Log.d(LOG_TAG, "date: " + date);
        Log.d(LOG_TAG, "amount: " + amount);

        // set the retrieved values on each input widget
        entryType = type;
        editExpIncSpnr.setSelection(getIndex(editExpIncSpnr, entryType), true);
        editExpIncTv.setText(entryType);

        editNameEt.setText(name);

        if (!category.equals("")) {
            isSpecifCategory = true;
            editSpecifCatSw.setChecked(true);

            // set opacity to 100%
            editCatSpnrTv.setAlpha(1f);
            editCatSpnr.setAlpha(1f);

            if (type.equals("Income")) {
                isIncome = true;
            }

            editCatSpnrTv.setText(category);
            getCatSpinner();
            editCatSpnr.setSelection(getIndex(editCatSpnr, category), true);
        } else {
            getCatSpinner();
            editCatSpnr.setEnabled(false);
        }

        getDateRecorded(ef.splitDate(date));
        // datepicker

        editAmtEt.setText(amount);
    }

    private void editRecord() {

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

    private void getDateRecorded(int[] dateVals) {
        editYear = dateVals[0];
        editMonth = dateVals[1];
        editDay = dateVals[2];

        // set default date to current date
        editDateTv.setText(ef.formatDate(editYear, editMonth, editDay));
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

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
//        Log.d(LOG_TAG, "spinner.getItemAtPosition(0): " + spinner.getItemAtPosition(0));
//        Log.d(LOG_TAG, "spinner.getItemAtPosition(1): " + spinner.getItemAtPosition(1));

        for (int i=0; i<spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                Log.d(LOG_TAG, "spinner.getItemAtPosition(i): " + spinner.getItemAtPosition(i));
                index = i;
            }
        }

        Log.d(LOG_TAG, "spnrIndex: " + index);
        return index;
    }
}

/*
References
Retrieve values in ArrayList<HashMap<key,value>>: https://stackoverflow.com/questions/42797663/how-to-get-specific-values-from-an-arraylist-of-hashmaps
Set spinner position: https://stackoverflow.com/questions/8769368/how-to-set-position-in-spinner
*/