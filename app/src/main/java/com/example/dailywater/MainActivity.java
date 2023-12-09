package com.example.dailywater;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);

        createDatabase();
        createTables();

        Navigate();
    }

    private void Navigate() {
        try {
            String endDateStr = sharedPreferences.getString("endDate", null);
            Date endDate = null;
            if (endDateStr != null) {
                endDate = dateFormat.parse(endDateStr);
            }
            Date currentDate = new Date();
            if (endDate == null || currentDate.after(endDate)) {
                Intent intent = new Intent(this, Activity2.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, Activity1.class);
                startActivity(intent);
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDatabase() {
        try {
            db = openOrCreateDatabase(
                    "HYDRATE_DB",
                    Activity.MODE_PRIVATE,
                    null);

            Log.d("MyApp", "Debug message: Database is created.");

        } catch(Exception ex) {
            ex.printStackTrace();
            println("database is not created.");
        }
    }
    private void createTables() {
        db.execSQL("CREATE TABLE IF NOT EXISTS DailyResult ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " date DATE NOT NULL, "
                + " success INTEGER NOT NULL CHECK (success IN (0,1)) DEFAULT 0, "
                + " water_drank INTEGER NOT NULL DEFAULT 0);");

        db.execSQL("CREATE TABLE IF NOT EXISTS ToDo ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " activity_name TEXT NOT NULL, "
                + " activity_status INTEGER NOT NULL CHECK (activity_status IN (0,1)) DEFAULT 0, "
                + " water_reward INTEGER NOT NULL);");
    }
}