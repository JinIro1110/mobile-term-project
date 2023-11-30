package com.example.dailywater;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();
        createTables();
    }

    private void createDatabase() {
        try {
            db = openOrCreateDatabase(
                    "HYDRATE_DB",
                    Activity.MODE_PRIVATE,
                    null);

            println("database is created.");

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

    private void insertDummyData() {

    }

}