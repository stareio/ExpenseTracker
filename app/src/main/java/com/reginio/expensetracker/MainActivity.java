package com.reginio.expensetracker;

import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button settingsBtn, addBtn, editBtn, checkBtn, homeBtn;
    ListView lv;

    ListAdapter adapter;

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
        lv = findViewById(R.id.test_list);
        getList();  // retrieve list of records

        // NTS: add formatting in some values (ex: for expenses, need negative sign & P/$)

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getList();
        ((BaseAdapter) adapter).notifyDataSetChanged();
    }

    private void nextActivity(Class dest) {
        intent = new Intent(MainActivity.this, dest);
        startActivity(intent);
    }

    private void getList() {
        DBHandler db = new DBHandler(this);
        ArrayList<HashMap<String,String>> recordsList = db.getRecords();

        adapter = new SimpleAdapter(MainActivity.this, recordsList,
                R.layout.list_record, new String[]{"category","name","amount"},
                new int[]{R.id.tvRecordCategory, R.id.tvRecordName, R.id.tvRecordAmount}
        );
        lv.setAdapter(adapter);
    }
}

/*
How to update values in RecyclerView on onResume(): https://stackoverflow.com/questions/62984025/how-can-i-refresh-recylverview-item-when-i-press-back
notifyDataSetChanged() not showing up: https://stackoverflow.com/questions/32261572/notifydatasetchanged-not-showing-up-for-custom-list-adapter
*/