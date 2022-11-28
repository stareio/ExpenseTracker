package com.reginio.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.*;

public class SettingsActivity extends AppCompatActivity {

    EditText nameEt;
    Spinner currencySpnr;
    Switch darkModeSw;
    ImageButton devBtn;
    Button saveBtn;

    String LOG_TAG = "Logs";

    String currency = "PHP";     // store currency value
    Boolean isDark = false;     // store darkModeSw value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameEt = findViewById(R.id.etSettingsName);
        currencySpnr = findViewById(R.id.spnrCurrency);
        darkModeSw = findViewById(R.id.swDarkMode);
        devBtn = findViewById(R.id.btnDevelopers);
        saveBtn = findViewById(R.id.btnSaveSettings);

        // populate the dropdown list for currency
        populateCurrencySpinner();

        // check for past saved settings
        readFile();

        // get selected item in currency spinner
        currencySpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currency = currencySpnr.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                currency = "PHP";
            }
        });

        // NTS: do dark/light mode feature !!

        // get value of dark mode switch
        darkModeSw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDark = isChecked;
        });

        // redirect to developers page
        devBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, DevelopersActivity.class);
            startActivity(intent);
        });

        // save settings in internal storage
        saveBtn.setOnClickListener(view -> writeFile());
    }

    public void writeFile() {
        String nameToSave = nameEt.getText().toString();
        String currToSave = currency;
        String darkModeToSave = isDark.toString();

        Log.d(LOG_TAG, "nameToSave: " + nameToSave);
        Log.d(LOG_TAG, "currToSave: " + currToSave);
        Log.d(LOG_TAG, "darkModeToSave: " + darkModeToSave);

        try {
            FileOutputStream fos = openFileOutput("ExpenseTracker_Settings.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            ArrayList<String> lines = new ArrayList<>();
            lines.add(nameToSave);
            lines.add(currToSave);
            lines.add(darkModeToSave);

            for (int i=0; i<lines.size(); i++) {
                bw.write(lines.get(i));

                if (i < lines.size()-1)
                    bw.newLine();
            }

            bw.close();
            osw.close();
            fos.close();

            Toast.makeText(getApplicationContext(), "Current Settings Saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        // check if file exists
        if (getBaseContext().getFileStreamPath("ExpenseTracker_Settings.txt").exists()) {
            Log.d(LOG_TAG, "Settings file exists");
            try {
                FileInputStream fis = openFileInput("ExpenseTracker_Settings.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                // store each line from the file in its respective variable
                String nameToRead = br.readLine();
                String currToRead = br.readLine();
                String darkModeToRead = br.readLine();

                br.close();
                isr.close();
                fis.close();

                // update the values displayed in the settings page
                nameEt.setText(nameToRead);
                currencySpnr.setSelection(
                        ((ArrayAdapter)currencySpnr.getAdapter()).getPosition(currToRead)
                );
                darkModeSw.setChecked(Boolean.parseBoolean(darkModeToRead));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(LOG_TAG, "Settings file does NOT exist");
        }
    }

    private void populateCurrencySpinner() {
        // get the spinner from the xml.
        currencySpnr = findViewById(R.id.spnrCurrency);

        // create a list of items for the spinner.
        String[] items = new String[]{"PHP", "USD"};

        // create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner,
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
Get switch value: https://stackoverflow.com/questions/26676367/how-to-get-switch-value-in-android
Get switch value and store in database: https://stackoverflow.com/questions/41374946/how-to-get-switch-button-value-and-stored-in-database
Switch on/off event listener: https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
Set spinner item programmatically: https://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically
Get spinner selected item: https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
*/