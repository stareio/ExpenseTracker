package com.reginio.expensetracker;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button settingsBtn, addBtn, editBtn, checkBtn, homeBtn;
    Intent intent;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsBtn = (Button) findViewById(R.id.btnSettings);
        addBtn = (Button) findViewById(R.id.btnAdd);
        editBtn = (Button) findViewById(R.id.btnEdit);
        checkBtn = (Button) findViewById(R.id.btnCheck);
        homeBtn = (Button) findViewById(R.id.btnHome);

        sp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        Boolean isDark = sp.getBoolean("night", false);

        settingsBtn.setOnClickListener(view -> nextActivity(SettingsActivity.class));

        addBtn.setOnClickListener(view -> nextActivity(AddRecordActivity.class));

        editBtn.setOnClickListener(view -> nextActivity(EditRecordActivity.class));

        checkBtn.setOnClickListener(view -> nextActivity(CheckRecordActivity.class));
        
        homeBtn.setOnClickListener(view -> nextActivity(HomeActivity.class));

        // == DB testing ===========================================================================
        ListView lv = findViewById(R.id.test_list);

        // NTS: listview not auto update when going from add record to main activity
        // need to refresh app to show new records
        // also need formatting in displaying values (ex: for expenses, need negative sign)

        DBHandler db = new DBHandler(this);
        ArrayList<HashMap<String,String>> recordsList = db.getRecords();

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, recordsList,
                R.layout.list_record, new String[]{"category","name","expense"},
                new int[]{R.id.tvRecordCategory, R.id.tvRecordName, R.id.tvRecordAmount}
        );
        lv.setAdapter(adapter);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void nextActivity(Class dest) {
        intent = new Intent(MainActivity.this, dest);
        startActivity(intent);
    }
}