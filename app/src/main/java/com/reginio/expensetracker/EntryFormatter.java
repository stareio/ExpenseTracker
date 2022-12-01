package com.reginio.expensetracker;

import android.util.Log;

public class EntryFormatter {

    String LOG_TAG = "Debugging";

    // return the amount as either a whole number or not
    public String formatAmount(String amount) {
        // show only 2 decimal places (ex: 20.91111 -> 20.91)
        if (Double.parseDouble(amount) - (int) Double.parseDouble(amount) != 0) {
            amount = String.format("%.02f", Double.parseDouble(amount));
        }

        // remove decimal places if whole number (ex: 20.00 -> 20)
        if (Double.parseDouble(amount) - (int) Double.parseDouble(amount) == 0) {
            amount = Integer.toString((int) Double.parseDouble(amount));
        }

        Log.d(LOG_TAG, "formatAmount: " + amount);
        return amount;
    }

    // ex: December 1, 2022
    public String formatDate(int year, int month, int day) {
        return formatMonth(month) + " " + day + ", " + year;
    }

    // exs: 0 -> January, 11 -> December
    public String formatMonth(int month) {
        String[] monthList = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        return monthList[month];
    }
}

/*
References
Maintain trailing zero in double: https://stackoverflow.com/questions/27832131/round-off-a-double-while-maintaining-the-trailing-zero
*/
