package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CheckRecordActivity extends AppCompatActivity {

    //Date Picker Object
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

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

        dateButton = findViewById(R.id.datePicker_btn);

        //Date Picker Button
        initDatePicker();
        dateButton.setText(getTodaysDate());

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

    //FOR DATE PICKER
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1 : return "JAN";
            case 2 : return "FEB";
            case 3 : return "MAR";
            case 4 : return "APR";
            case 5 : return "MAY";
            case 6 : return "JUN";
            case 7 : return "JUL";
            case 8 : return "AUG";
            case 9 : return "SEP";
            case 10 : return "OCT";
            case 11 : return "NOV";
            case 12 : return "DEC";
            default : return "JAN";
        }
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

}