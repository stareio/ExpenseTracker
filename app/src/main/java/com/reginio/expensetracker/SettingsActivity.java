package com.reginio.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    ImageButton devBtn;
    Spinner currencySpnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        devBtn = findViewById(R.id.btnDevelopers);

        // populate the dropdown list for currency
        getCurrencySpinner();

        devBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, DevelopersActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCurrencySpinner() {
        // get the spinner from the xml.
        currencySpnr = findViewById(R.id.spnrCurrency);

        // create a list of items for the spinner.
        String[] items = new String[]{"PHP", "USD"};

        // create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
                R.layout.spinner_currency,
                items
        );

        // set the spinners adapter to the previously created one.
        currencySpnr.setAdapter(adapter);
    }
}

/*
Reference
Dropdown list: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
Customized spinner: https://www.youtube.com/watch?v=N8GfosWTt44
 */