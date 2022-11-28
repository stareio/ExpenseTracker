package com.reginio.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EditRecordActivity extends AppCompatActivity {

    Spinner editExpIncSpnr, editCatSpnr, editDateSpnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        editExpIncSpnr = findViewById(R.id.spnrEditExpInc);
        editCatSpnr = findViewById(R.id.spnrEditCat);
        editDateSpnr = findViewById(R.id.spnrEditDate);

        // populate the dropdown listS for expenses/income, category, and date
        getSpinners();

        // nts: align text of selected item in spinners at viewEnd
    }

    private void getSpinners() {
        // create a list for each spinner
        Spinner[] spnrs = new Spinner[]{
                editExpIncSpnr,
                editCatSpnr,
                editDateSpnr
        };

        // create a list of items for each spinner.
        String[] itemsExpInc = new String[]{"Expense", "Income"};
        String[] itemsCat = new String[]{
                "Rental",
                "Car",
                "Salary",
                "Food",
                "Coffee",
                "Mobile Account",
                "Entertainment"
        };
        String[] itemsDate = new String[]{"January", "February", "March"};  // placeholder for calendar

        // create a list for each item list
        String[][] items = new String[][]{itemsExpInc, itemsCat, itemsDate};

        // create an adapter to describe how the items are displayed, adapters are used in several places in android.
        for (int i = 0; i < spnrs.length; i++) {
            ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                    this,
                    R.layout.spinner,
                    items[i]
            );

            // set the spinners adapter to the previously created one.
            spnrs[i].setAdapter(adapter);

            Log.d("SPINNER ITEMS", "items: " + items[i]);
            Log.d("SPINNER ITEMS", "spinner: " + spnrs[i]);
        }
    }
}