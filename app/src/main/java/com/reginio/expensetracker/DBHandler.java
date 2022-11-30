package com.reginio.expensetracker;

import android.content.Context;
import android.database.sqlite.*;

import java.sql.Date;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "expensesdb";
    private static final String TABLE_EXPENSES = "expensedetails";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CAT = "category";
    private static final Date KEY_DATE = "category";
    private static final String KEY_CAT = "category";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
