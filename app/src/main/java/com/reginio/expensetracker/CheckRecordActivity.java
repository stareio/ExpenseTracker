package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckRecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Debugging";
    ImageButton addBtn;
    ListView lv;

    ListAdapter adapter;

    Intent intent;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);

        lv = (ListView) findViewById(R.id.list);
        getDateList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getDateList();
        ((BaseAdapter) adapter).notifyDataSetChanged();
    }

    private void getDateList() {
        DBHandler db = new DBHandler(this);
        ArrayList<HashMap<String,String>> dateList = db.getDates();
        Log.d(LOG_TAG, "datesList: " + dateList);

        adapter = new SimpleAdapter(CheckRecordActivity.this, dateList,
                R.layout.activity_check_date_template, new String[]{"date"},
                new int[]{R.id.Date}
        );
        lv.setAdapter(adapter);
    }

}