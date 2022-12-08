package com.reginio.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.*;

public class CheckRecordAdapter extends BaseAdapter implements OnEditRecordSpnrSelect {

    private OnEditRecordSpnrSelect oe;
    private ArrayList<HashMap<String,String>> recordsList;
    private Context context;
    private String modify;
    private LayoutInflater layoutInflater;
    private EntryFormatter ef;

    String LOG_TAG = "Debugging";

    public CheckRecordAdapter(Context context, ArrayList<HashMap<String,String>> data,
                         OnEditRecordSpnrSelect listener) {
        this.context = context;
        recordsList = data;
        this.oe = listener;

        layoutInflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return recordsList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ef = new EntryFormatter();

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.activity_check_entry_template, null);

            viewHolder.recordIconIv = (ImageView) convertView.findViewById(R.id.imageView5);
            viewHolder.recordCategoryTv = (TextView) convertView.findViewById(R.id.check_Category);
            viewHolder.recordNameTv = (TextView) convertView.findViewById(R.id.check_Name);
            viewHolder.recordAmountTv = (TextView) convertView.findViewById(R.id.check_Amount);
            viewHolder.recordSpnr = (Spinner) convertView.findViewById(R.id.spnrRecord);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // set values in each widget
        String category = (recordsList.get(position)).get("category");

        // switch case
        switch (category){
            //for income
            case "Commissions"  :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_account_balance_24); break;
            case "Salary"       :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_account_balance_24); break;
            case "Pocket Money" :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_account_balance_wallet_24); break;
            case "Savings"      :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_savings_24); break;

            //for expenses
            case "Car"          :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_directions_car_24); break;
            case "Commute"      :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_commute_24); break;
            case "Drinks"       :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_water_drop_24); break;
            case "Food"         :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_food_bank_24); break;
            case "Groceries"    :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_local_grocery_store_24); break;
            case "Medicine"     :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_medical_services_24); break;
            case "Rental"       :   viewHolder.recordIconIv.setImageResource(R.drawable.ic_baseline_house_24); break;

            default: viewHolder.recordIconIv.setImageResource(R.drawable.ic_default);
        }

        if (category.equals("")) {
            viewHolder.recordCategoryTv.setTypeface(null, Typeface.ITALIC);
            viewHolder.recordCategoryTv.setText("N/A");
        } else {
            viewHolder.recordCategoryTv.setText(category);
        }


        viewHolder.recordNameTv.setText((recordsList.get(position)).get("name"));

        String type = (recordsList.get(position)).get("type");
        String amount = (recordsList.get(position)).get("amount");
        amount = ef.formatCurrAmount(amount);
        if (type.equals("Expense")) {
            viewHolder.recordAmountTv.setText("-" + amount);    // add negative sign
            viewHolder.recordAmountTv.setTextColor(Color.parseColor("#F8777D"));
        } else if (type.equals("Income")) {
            viewHolder.recordAmountTv.setText(amount);  // no negative sign
            viewHolder.recordAmountTv.setTextColor(Color.parseColor("#65BCBF"));
        }

        // populate the spinner
        populateRecordSpinner(viewHolder.recordSpnr);

        // spinner listener
        ViewHolder finalViewHolder = viewHolder;
        (viewHolder.recordSpnr).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int positionInSpinner, long id) {

                modify = finalViewHolder.recordSpnr.getSelectedItem().toString();
                oe.onItemSelectedListener(modify, position);

                Log.d(LOG_TAG, "RecordAdapter =========================");
                Log.d(LOG_TAG, "modify: " + modify);
                Log.d(LOG_TAG, "position: " + position);

                // hide selection text
                ((TextView)view).setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return convertView;
    }

    @Override
    public void onItemSelectedListener(String modify, int id) {}

    private static class ViewHolder {
        public ImageView recordIconIv;
        public TextView recordCategoryTv, recordNameTv, recordAmountTv;
        public Spinner recordSpnr;
    }

    private void populateRecordSpinner(Spinner spnr) {
        // create a list of items for the spinner
        String[] items = new String[]{"", "Edit", "Delete"};

        // create an adapter to describe how the items are displayed
        ModifyAdapter spnrAdapter = new ModifyAdapter(
                context,
                R.layout.spinner,
                items
        );

        // set the spinner adapter to the previously created one
        spnr.setAdapter(spnrAdapter);
    }
}

/*
References
Customized Adapter (1): https://stackoverflow.com/questions/27248695/get-the-position-in-a-listview-of-a-spinner-and-get-its-selected-value
Customized Adapter (2): https://stackoverflow.com/questions/39342086/update-listview-based-on-spinner-selection-in-android
Customized Adapter (3): https://www.digitalocean.com/community/tutorials/android-listview-with-custom-adapter-example-tutorial
Customized Adapter (4): https://www.geeksforgeeks.org/custom-arrayadapter-with-listview-in-android/
Hide spinner selection text: https://stackoverflow.com/questions/4931186/hide-text-of-android-spinner
Send string from custom Adapter to Activity (1): https://stackoverflow.com/questions/48048977/send-string-from-custom-adapter-to-activity
Send string from custom Adapter to Activity (2): https://stackoverflow.com/questions/35008860/how-to-pass-values-from-recycleadapter-to-mainactivity-or-other-activities
Send string from custom Adapter to Activity (3): https://stackoverflow.com/questions/17417839/how-to-pass-values-from-adapter-to-activity
*/