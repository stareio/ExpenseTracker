package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.*;
import android.app.DatePickerDialog.*;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.*;

public class HomeActivity extends AppCompatActivity {
    //Instantiations

    //Date Picker Object
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private TextView incomeText, expenseText;
    private ImageView settingBtn;
    String totalIncome = "10000";
    String totalExpense = "500";

    //Pie Chart Object
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Find ID
        dateButton = findViewById(R.id.datePicker_btn);
        incomeText = findViewById(R.id.income_txt);
        expenseText = findViewById(R.id.expenses_txt);
        settingBtn = findViewById(R.id.setting_img);

        //Date Picker Button
        initDatePicker();
        dateButton.setText(getTodaysDate());

        //Pie Chart Implementation
        pieChart = findViewById(R.id.piechart);

        //add data to piechart
        setData();

        //Set Text from value
        incomeText.setText(totalIncome);
        expenseText.setText(totalExpense);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    //FOR PIE CHART
    private void setData() {

        pieChart.addPieSlice(
                new PieModel(
                        "Income",
                        //Integer.parseInt(incomeText.getText().toString()),
                        Integer.parseInt(totalIncome),
                        Color.parseColor("#856214")
                )
        );
        pieChart.addPieSlice(
                new PieModel(
                        "Income",
                        //Integer.parseInt(expenseText.getText().toString()),
                        Integer.parseInt(totalExpense),
                        Color.parseColor("#213933")
                )
        );

        //To animate pie chart
        pieChart.startAnimation();
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