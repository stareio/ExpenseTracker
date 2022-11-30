package com.reginio.expensetracker;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

        homeBtn.setOnClickListener(view -> nextActivity(HomeActivity.class));
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