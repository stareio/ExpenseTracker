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
    private TextView incomeText, expenseText;
    private ImageView settingBtn;
    private ListView lv;
    String totalIncome = "0";
    String totalExpense = "0";
    String category = "";

    //Pie Chart Object
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Find ID
        incomeText = findViewById(R.id.income_txt);
        expenseText = findViewById(R.id.expenses_txt);
        settingBtn = findViewById(R.id.setting_img);

        //Item list
        lv = (ListView) findViewById(R.id.item_list);
        //DB handler
        DBHandler db = new DBHandler(this);
        //ArrayList
        ArrayList<HashMap<String, String>> itemList = db.getRecords();
        //ListAdapter Instantiation
        ListAdapter adapter = new SimpleAdapter(
                HomeActivity.this,
                itemList,
                R.layout.activity_home_record_template,
                new String[]{"name","amount"},
                new int[]{R.id.item_txt, R.id.price_txt}
        );
        lv.setAdapter(adapter);

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



}