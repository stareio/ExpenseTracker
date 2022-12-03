package com.reginio.expensetracker;

import android.content.*;
import android.database.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnEditRecordSpnrSelect {

    Button settingsBtn, addBtn, checkBtn, homeBtn;
    ListView lv;
    Spinner recordSpnr;

    ArrayList<String> recordIds;
    RecordAdapter recordAdapter;

    String LOG_TAG = "Debugging";
    String currency;

    Intent intent;
    SharedPreferences sp;

    // retrieve modify value from adapter
    @Override
    public void onItemSelectedListener(String modify, int id) {
        Log.d(LOG_TAG, "MainActivity ==========================");
        Log.d(LOG_TAG, "modify in MainActivity: " + modify);
        Log.d(LOG_TAG, "adapter position: " + id);

        if (!modify.equals("")) {
            modifyRecord(modify, id);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsBtn = (Button) findViewById(R.id.btnSettings);
        addBtn = (Button) findViewById(R.id.btnAdd);
        checkBtn = (Button) findViewById(R.id.btnCheck);
        homeBtn = (Button) findViewById(R.id.btnHome);

        sp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        Boolean isDark = sp.getBoolean("night", false);

        settingsBtn.setOnClickListener(view -> nextActivity(SettingsActivity.class));

        addBtn.setOnClickListener(view -> nextActivity(AddRecordActivity.class));

        checkBtn.setOnClickListener(view -> nextActivity(CheckRecordActivity.class));
        
        homeBtn.setOnClickListener(view -> nextActivity(HomeActivity.class));

        // == DB testing ===========================================================================
        lv = findViewById(R.id.test_list);
        recordSpnr = findViewById(R.id.spnrRecord);

        getList();  // retrieve list of records

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

        recordAdapter = new RecordAdapter(MainActivity.this, recordsList,
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

            Intent i = new Intent(MainActivity.this, EditRecordActivity.class);
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

    private void nextActivity(Class dest) {
        intent = new Intent(MainActivity.this, dest);
        startActivity(intent);
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
                currency = currToRead;
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception: " + e);
            }
        } else {
            currency = "PHP";
        }
    }
}

/*
How to update values in RecyclerView on onResume(): https://stackoverflow.com/questions/62984025/how-can-i-refresh-recylverview-item-when-i-press-back
notifyDataSetChanged() not showing up: https://stackoverflow.com/questions/32261572/notifydatasetchanged-not-showing-up-for-custom-list-adapter
*/