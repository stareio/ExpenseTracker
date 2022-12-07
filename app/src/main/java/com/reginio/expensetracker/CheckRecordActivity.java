package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.*;

public class CheckRecordActivity extends AppCompatActivity implements OnEditRecordSpnrSelect {

    //Date Picker Object
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    private static final String LOG_TAG = "Debugging";
    ImageButton addBtn;

    ListView lv;

    ListAdapter adapter;
    CheckRecordAdapter recordAdapter;

    ArrayList<String> recordIds;
    Intent intent;
    SharedPreferences sp;
    String currency;

    EntryFormatter ef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);

        dateButton = findViewById(R.id.dateChange_btn);

        //Date Picker Button
        initDatePicker();
        dateButton.setText(getTodaysDate());

        lv = (ListView) findViewById(R.id.list);
        getDateEntryList(getTodaysDateInput());

    }

    private void getDateEntryList(String date) {
        DBHandler db = new DBHandler(this);
        recordIds = new ArrayList<>();
        ArrayList<HashMap<String,String>> dateList = db.getRecordsbyDate(date);

        Log.d(LOG_TAG, "Input Date: " + date);
        Log.d(LOG_TAG, "recordList: " + dateList);

        int count = 0;
        for (Map<String,String> map : dateList) {
            recordIds.add(map.get("id"));
            Log.d(LOG_TAG, "Stored id " + map.get("id") + " at index " + count);

            count++;
        }
        Log.d(LOG_TAG, "recordIds after for loop: " + recordIds);

        recordAdapter = new CheckRecordAdapter(CheckRecordActivity.this, dateList, this);
        lv.setAdapter(recordAdapter);

        db.close();
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

    private String getTodaysDateInput() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        month = month + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
                String date = makeDateString(day, (month + 1), year);
                dateButton.setText(date);

                getDateEntryList(makeInputDate(day, month, year));
                Log.d(LOG_TAG, "Selected Date: " + date);

            }
        };



        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }


    private String makeInputDate(int day, int month, int year) {
        String output =  year + "-" + month + "-" + day;
        Log.d(LOG_TAG, "Input Date: " + output);
        return output;
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

//    FOR RECORD ADAPTER =============================================

    @Override
    public void onItemSelectedListener(String modify, int id) {
        Log.d(LOG_TAG, "CheckRecordActivity ==========================");
        Log.d(LOG_TAG, "modify in CheckRecordActivity: " + modify);
        Log.d(LOG_TAG, "adapter position: " + id);

        if (!modify.equals("")) {
            modifyRecord(modify, id);
        }
    }

    // edit/delete the record
    private void modifyRecord(String modify, int id) {
        String recordId = recordIds.get(id);
        Log.d(LOG_TAG, "recordId: " + recordId);

        if (modify.equals("Edit")) {
            // send the recordId to the edit record page for editing
            Log.d(LOG_TAG, "Record to be edited");

            Intent i = new Intent(CheckRecordActivity.this, EditRecordActivity.class);
            i.putExtra("id", recordId);
            startActivity(i);
        } else if (modify.equals("Delete")) {
            // delete the record from the database
            Log.d(LOG_TAG, "Record deleted");

            DBHandler db = new DBHandler(this);
            db.deleteRecord(Integer.parseInt(recordId));

            recordIds.clear();
            getDateEntryList(getTodaysDateInput());
            ((BaseAdapter) recordAdapter).notifyDataSetChanged();
        }
    }

    // update the displayed list of records
    @Override
    protected void onResume() {
        super.onResume();
        recordIds.clear();
        getDateEntryList(getTodaysDateInput());
        ((BaseAdapter) recordAdapter).notifyDataSetChanged();

    }
}