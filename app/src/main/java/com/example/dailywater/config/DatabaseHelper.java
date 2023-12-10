package com.example.dailywater.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HYDRATE_DB";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // isSuccessOnDate 메서드 추가
    @SuppressLint("Range")
    public boolean isSuccessOnDate(Date date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        Cursor cursor = db.rawQuery("SELECT success FROM DailyResult WHERE date = ?", new String[]{dateString});

        boolean isSuccess = false;
        if (cursor.moveToFirst()) {
            isSuccess = cursor.getInt(cursor.getColumnIndex("success")) == 1;
        }

        cursor.close();
        db.close();
        return isSuccess;
    }
}
