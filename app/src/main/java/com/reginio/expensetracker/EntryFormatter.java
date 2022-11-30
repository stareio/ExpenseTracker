package com.reginio.expensetracker;

import android.util.Log;

public class EntryFormatter {

    String LOG_TAG = "Debugging";

    // returns the amount as either a whole number or not
    public String formatAmount(String amount) {
        double doubleAmt = Double.parseDouble(amount);
        int intFromDouble = (int) doubleAmt;

        // removes decimal places if whole number (ex: 20.00 -> 20)
        if (doubleAmt - intFromDouble == 0) {
            amount = Integer.toString(intFromDouble);
        }

        // show only 2 decimal places (ex: 20.91111 -> 20.91)
        else {
            amount = String.format("%.02f", doubleAmt);
        }

        Log.d(LOG_TAG, "formatAmount: " + amount);
        return amount;
    }
}

/*
References
Maintain trailing zero in double: https://stackoverflow.com/questions/27832131/round-off-a-double-while-maintaining-the-trailing-zero
*/
