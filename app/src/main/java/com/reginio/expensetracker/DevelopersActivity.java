package com.reginio.expensetracker;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class DevelopersActivity extends AppCompatActivity {

    TextView dev1DescrTv, dev2DescrTv, dev3DescrTv;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        // set to dark/light mode
        sp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        Boolean isDark = sp.getBoolean("night", false);

        dev1DescrTv = findViewById(R.id.tvDevDescr1);
        dev2DescrTv = findViewById(R.id.tvDevDescr2);
        dev3DescrTv = findViewById(R.id.tvDevDescr3);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            // change description text color
            dev1DescrTv.setTextColor(Color.parseColor("#c4c4c4"));
            dev2DescrTv.setTextColor(Color.parseColor("#c4c4c4"));
            dev3DescrTv.setTextColor(Color.parseColor("#c4c4c4"));
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}