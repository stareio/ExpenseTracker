package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class CheckRecordActivity extends AppCompatActivity {

    ImageButton addBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);

        addBtn = (ImageButton) findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(view -> nextActivity(AddRecordActivity.class));
    }

    private void nextActivity(Class dest) {
        intent = new Intent(CheckRecordActivity.this, dest);
        startActivity(intent);
    }
}