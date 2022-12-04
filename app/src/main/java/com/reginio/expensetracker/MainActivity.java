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

public class MainActivity extends AppCompatActivity {

    Button checkBtn, homeBtn;
    ListView lv;

    String LOG_TAG = "Debugging";

    Intent intent;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBtn = (Button) findViewById(R.id.btnCheck);
        homeBtn = (Button) findViewById(R.id.btnHome);

        sp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        Boolean isDark = sp.getBoolean("night", false);

        checkBtn.setOnClickListener(view -> nextActivity(CheckRecordActivity.class));

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

/*
How to update values in RecyclerView on onResume(): https://stackoverflow.com/questions/62984025/how-can-i-refresh-recylverview-item-when-i-press-back
notifyDataSetChanged() not showing up: https://stackoverflow.com/questions/32261572/notifydatasetchanged-not-showing-up-for-custom-list-adapter
*/
