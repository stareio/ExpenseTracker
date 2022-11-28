package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initDatePicker();
        dateButton = findViewById(R.id.datePicker_btn);
        dateButton.setText(getTodaysDate());
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void initDatePicker() {
            DatePickerDialog.OnDateSetListener dateSetListener = new OnDateSetListener() {
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