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
    Switch darkModeSw;
    ImageButton devBtn;
    Button saveBtn;

    String LOG_TAG = "Debugging";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    Boolean isDark = false;     // store darkModeSw value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameEt = findViewById(R.id.etGreetingName);
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

        // check for saved username
        readSettings();

        // get value of dark mode switch
        darkModeSw.setOnCheckedChangeListener((buttonView, isChecked) -> isDark = isChecked);

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

        Log.d(LOG_TAG, "nameToSave: " + nameToSave);
        Log.d(LOG_TAG, "isDark: " + isDark);

        try {
            FileOutputStream fos = openFileOutput("ExpenseTracker_Settings.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            ArrayList<String> lines = new ArrayList<>();
            lines.add(nameToSave);

            // save username
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

            // go back to previous page
            onBackPressed();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception: " + e);
        }
    }

    public void readSettings() {
        // check if file exists
        if (getBaseContext().getFileStreamPath("ExpenseTracker_Settings.txt").exists()) {
            Log.d(LOG_TAG, "Settings file exists");

            try {
                FileInputStream fis = openFileInput("ExpenseTracker_Settings.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                // get name
                String nameToRead = br.readLine();

                Log.d(LOG_TAG, "nameToRead: " + nameToRead);

                br.close();
                isr.close();
                fis.close();

                // update the displayed username
                nameEt.setText(nameToRead);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception: " + e);
            }
        } else {
            Log.d(LOG_TAG, "Settings file does NOT exist");
        }
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
//        Log.d(LOG_TAG, "spinner.getItemAtPosition(0): " + spinner.getItemAtPosition(0));
//        Log.d(LOG_TAG, "spinner.getItemAtPosition(1): " + spinner.getItemAtPosition(1));

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
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            spEditor = sp.edit();
            spEditor.putBoolean("night", true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            spEditor = sp.edit();
            spEditor.putBoolean("night", false);
        }

        spEditor.apply();
    }
}

/*
References
Dropdown list: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
Customized spinner: https://www.youtube.com/watch?v=N8GfosWTt44
Get switch value: https://stackoverflow.com/questions/26676367/how-to-get-switch-value-in-android
Get switch value and store in database: https://stackoverflow.com/questions/41374946/how-to-get-switch-button-value-and-stored-in-database
Switch on/off event listener: https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
Set spinner position: https://stackoverflow.com/questions/8769368/how-to-set-position-in-spinner
Get spinner selected item: https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
Get position of each String value in spinner: https://stackoverflow.com/questions/13112020/android-spinner-get-position-of-string-in-code-behind
Dark and light mode: https://www.youtube.com/watch?v=_XN-c5Yz0wk
Get switch value: https://stackoverflow.com/questions/10576307/android-how-do-i-correctly-get-the-value-from-a-switch
*/