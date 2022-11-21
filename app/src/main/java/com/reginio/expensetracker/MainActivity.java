package com.reginio.expensetracker;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button settingsBtn, addBtn, editBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsBtn = (Button) findViewById(R.id.btnSettings);
        addBtn = (Button) findViewById(R.id.btnAdd);
        editBtn = (Button) findViewById(R.id.btnEdit);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(SettingsActivity.class);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity(AddRecordActivity.class);
            }
        });

//        editBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nextActivity(EditRecordActivity.class);
//            }
//        });
    }

    private void nextActivity(Class dest) {
        intent = new Intent(MainActivity.this, dest);
        startActivity(intent);
    }
}