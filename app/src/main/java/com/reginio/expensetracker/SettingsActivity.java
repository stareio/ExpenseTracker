package com.reginio.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.*;
import java.util.*;

public class SettingsActivity extends AppCompatActivity {

    EditText nameEt;
    Spinner currencySpnr;
    Switch darkModeSw;
    ImageButton devBtn;
    Button saveBtn;

    String LOG_TAG = "Debugging";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    String currency = "";     // store currency value
    Boolean isDark = false;     // store darkModeSw value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameEt = findViewById(R.id.etGreetingName);
        currencySpnr = findViewById(R.id.spnrCurrency);
        darkModeSw = findViewById(R.id.swDarkMode);
        devBtn = findViewById(R.id.btnDevelopers);
        saveBtn = findViewById(R.id.btnSaveSettings);

        sp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        isDark = sp.getBoolean("night", false);
        Log.d(LOG_TAG, "isDark: " + isDark);

        // check if dark mode
        if (isDark) {
            darkModeSw.setChecked(true);
        }

        // check for saved username and currency
        readFile();

        // populate the dropdown list for currency
        populateCurrencySpinner();

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

        // redirect to developers page
        devBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, DevelopersActivity.class);
            startActivity(intent);
        });

        // save settings in internal storage
        saveBtn.setOnClickListener(view -> {
            saveSettings();
        });
    }

    public void saveSettings() {
        String nameToSave = nameEt.getText().toString();
        String currToSave = currency;

        Log.d(LOG_TAG, "nameToSave: " + nameToSave);
        Log.d(LOG_TAG, "currToSave: " + currToSave);

        try {
            FileOutputStream fos = openFileOutput("ExpenseTracker_Settings.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            ArrayList<String> lines = new ArrayList<>();
            lines.add(nameToSave);
            lines.add(currToSave);

            // save username & currency
            for (int i=0; i<lines.size(); i++) {
                bw.write(lines.get(i));

                if (i < lines.size()-1)
                    bw.newLine();
            }

            bw.close();
            osw.close();
            fos.close();

            // save mode as dark/light
            setMode();

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

                Log.d(LOG_TAG, "nameToRead: " + nameToRead);
                Log.d(LOG_TAG, "currToRead: " + currToRead);

                br.close();
                isr.close();
                fis.close();

                // update the displayed username and currency
                nameEt.setText(nameToRead);

                // NTS: fix issue -> not displaying if USD is saved
                populateCurrencySpinner();
                currencySpnr.setSelection(getIndex(currencySpnr, currToRead), true);
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

    private int getIndex(Spinner spinner, String myString){

        int index = 0;
        Log.d(LOG_TAG, "spinner.getItemAtPosition(0): " + spinner.getItemAtPosition(0));
        Log.d(LOG_TAG, "spinner.getItemAtPosition(1): " + spinner.getItemAtPosition(1));

        for (int i=0; i<spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                Log.d(LOG_TAG, "spinner.getItemAtPosition(i): " + spinner.getItemAtPosition(i));
                index = i;
            }
        }

        Log.d(LOG_TAG, "spnrIndex: " + index);
        return index;
    }

    private void setMode() {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            spEditor = sp.edit();
            spEditor.putBoolean("night", false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            spEditor = sp.edit();
            spEditor.putBoolean("night", true);
        }

        spEditor.apply();
    }
}

/*
Reference
Dropdown list: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
Customized spinner: https://www.youtube.com/watch?v=N8GfosWTt44
Get switch value: https://stackoverflow.com/questions/26676367/how-to-get-switch-value-in-android
Get switch value and store in database: https://stackoverflow.com/questions/41374946/how-to-get-switch-button-value-and-stored-in-database
Switch on/off event listener: https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
Set spinner positiono: https://stackoverflow.com/questions/8769368/how-to-set-position-in-spinner
Get spinner selected item: https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
Get position of each String value in spinner: https://stackoverflow.com/questions/13112020/android-spinner-get-position-of-string-in-code-behind
Dark and light mode: https://www.youtube.com/watch?v=_XN-c5Yz0wk
*/