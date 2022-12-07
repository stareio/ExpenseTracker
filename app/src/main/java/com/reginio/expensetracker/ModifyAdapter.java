package com.reginio.expensetracker;

import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ModifyAdapter extends ArrayAdapter<String> {

    public ModifyAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if (position == 0) {
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = super.getDropDownView(position, null, parent);
        }
        return v;
    }
}

/*
Reference
Hide item in spinner: https://stackoverflow.com/questions/9863378/how-to-hide-one-item-in-an-android-spinner
*/
