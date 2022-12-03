package com.reginio.expensetracker;

import android.util.Log;

public class EntryFormatter {

    String LOG_TAG = "Debugging";

    // return the amount as either a whole number or not
    public String formatAmountValue(String amount) {
        // check if not null
        if (!amount.equals("")) {
            // show only 2 decimal places (ex: 20.91111 -> 20.91)
            if (Double.parseDouble(amount) - (int) Double.parseDouble(amount) != 0) {
                amount = String.format("%.02f", Double.parseDouble(amount));
            }

            // remove decimal places if whole number (ex: 20.00 -> 20)
            if (Double.parseDouble(amount) - (int) Double.parseDouble(amount) == 0) {
                amount = Integer.toString((int) Double.parseDouble(amount));
            }
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

    public int[] splitDate(String date) {
        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);

        return new int[]{year, month, day};
    }

    // add currency symbol (ex: ₱99.99)
    public String formatCurrAmount(String amount) {
        return "₱" + amount;
    }
}

/*
References
Maintain trailing zero in double: https://stackoverflow.com/questions/27832131/round-off-a-double-while-maintaining-the-trailing-zero
Split date: https://stackoverflow.com/questions/8445465/split-java-dashed-string
*/
