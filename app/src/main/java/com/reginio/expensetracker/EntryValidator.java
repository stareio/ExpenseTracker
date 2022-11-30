package com.reginio.expensetracker;

import android.util.*;

public class EntryValidator {

    String LOG_TAG = "Debugging";

    public boolean checkAmount(String amount) {
        try {
            // checks if the amount has 1-10 digits
            if (amount.length() <= 10 && amount.length() >= 1) {

                // throws exception if invalid
                double d = Double.valueOf(amount);

                // check if negative value or 0
                if (d > 0) {
                    Log.d(LOG_TAG, "checkAmount is true");
                    return true;
                }
            }
        }

        catch (Exception e) {
            Log.e(LOG_TAG, "Exception: " + Log.getStackTraceString(e));
        }

        Log.d(LOG_TAG, "checkAmount is false");
        return false;
    }

    public boolean checkName(String str) {
        return str.length() <= 20;
    }
}