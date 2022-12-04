package com.reginio.expensetracker;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import java.util.*;

// user-defined classes
import com.reginio.expensetracker.EntryFormatter;
import com.reginio.expensetracker.EntryValidator;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "expensesdb";
    private static final String TABLE_EXPENSES = "expensedetails";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NAME = "name";
    private static final String KEY_CAT = "category";
    private static final String KEY_AMT = "amount";
    private static final String KEY_DATE = "date";

    String LOG_TAG = "Debugging";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TYPE + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_CAT + " TEXT,"
                + KEY_AMT + " TEXT,"
                + KEY_DATE + " TEXT"
                + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

        // Create tables again
        onCreate(db);
    }

    // CRUD Operations ========================================================
    // Add record
    void addRecord(String type, String name, String category,
                   String amount, String date) {
        // Get data repo in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create new map of values w/ column names as keys
        ContentValues cv = new ContentValues();
        cv.put(KEY_TYPE, type);
        cv.put(KEY_NAME, name);
        cv.put(KEY_CAT, category);
        cv.put(KEY_AMT, amount);
        cv.put(KEY_DATE, date);

        // Insert new row, returning its primary key value
        long newRowId = db.insert(TABLE_EXPENSES, null, cv);
        db.close();
    }

    // Retrieve records based on date
    public ArrayList<HashMap<String, String>> getRecordsbyDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> recordList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_EXPENSES
                            + " WHERE date = '" + date + "'";
        Cursor cursor = db.rawQuery(query,null);

        while (cursor.moveToNext()){
            HashMap<String,String> record = new HashMap<>();
            record.put("id", cursor.getString(cursor.getColumnIndex(KEY_ID)));
            record.put("type", cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            record.put("name", cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            record.put("category", cursor.getString(cursor.getColumnIndex(KEY_CAT)));
            record.put("amount", cursor.getString(cursor.getColumnIndex(KEY_AMT)));
            record.put("date", cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            recordList.add(record);
        }

        return recordList;
    }

    // Retrieve list of records based on id
    public ArrayList<HashMap<String,String>> getRecordById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> recordList = new ArrayList<>();
        String query = "SELECT type, name, category, amount, date FROM " + TABLE_EXPENSES;
        Cursor cursor = db.query(TABLE_EXPENSES, new String[]{
                        KEY_TYPE,
                        KEY_NAME,
                        KEY_CAT,
                        KEY_AMT,
                        KEY_DATE
                }, KEY_ID + "= ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor.moveToNext()) {
            HashMap<String, String> record = new HashMap<>();
            record.put("type", cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            record.put("name", cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            record.put("category", cursor.getString(cursor.getColumnIndex(KEY_CAT)));
            record.put("amount", cursor.getString(cursor.getColumnIndex(KEY_AMT)));
            record.put("date", cursor.getString(cursor.getColumnIndex(KEY_DATE)));

            recordList.add(record);
        }

        return recordList;
    }

    // Delete record
    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + "= ?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    // Edit record
    public int updateRecord(String type, String name, String category,
                            String amount, String date, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_TYPE, type);
        cv.put(KEY_NAME, name);
        cv.put(KEY_CAT, category);
        cv.put(KEY_AMT, amount);
        cv.put(KEY_DATE, date);

        return db.update(TABLE_EXPENSES, cv, KEY_ID + "= ?",
                new String[]{String.valueOf(id)});
    }

    // Get all available dates in the table
    public ArrayList<HashMap<String, String>> getDates() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> recordList = new ArrayList<>();
        String query = "SELECT DISTINCT date FROM " + TABLE_EXPENSES;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            HashMap<String, String> record = new HashMap<>();
            record.put("date", cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            recordList.add(record);
        }
        return recordList;
    }

}