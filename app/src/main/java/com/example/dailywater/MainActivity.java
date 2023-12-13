package com.example.dailywater;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.text.ParseException;
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
        AndroidThreeTen.init(this);
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        createDatabase();
        createTables();
        deleteAllDataFromTables();
        insertDummyData();
//        setStartEndDate();

        Navigate();
    }

    // 임시 시작 마지막 날짜
//    private void setStartEndDate() {
//        setSharedPreferences sharedPreferencesManager = new setSharedPreferences(this);
//
//        try {
//            // 시작 날짜를 12월 1일로 설정
//            Date startDate = dateFormat.parse("2023-12-01");
//            sharedPreferencesManager.saveStartDate(startDate);
//
//            // 마지막 날짜를 12월 26일로 설정
//            Date endDate = dateFormat.parse("2023-12-26");
//            sharedPreferencesManager.saveEndDate(endDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            // 날짜 형식 오류 처리
//        }
//    }


    private void Navigate() {
        try {
            String endDateStr = sharedPreferences.getString("endDate", null);
            Date endDate = null;
            if (endDateStr != null) {
                endDate = dateFormat.parse(endDateStr);
            }
            Date currentDate = new Date();
            if (endDate == null || currentDate.after(endDate)) {
                Intent intent = new Intent(this, Activity1.class);
                dropTables();
                createTables();
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

    private void deleteAllDataFromTables() {
        db.execSQL("DELETE FROM DailyResult;");
        db.execSQL("DELETE FROM ToDo;");
        println("All data deleted from tables.");
    }

    private void createDatabase() {
        try {

            db = openOrCreateDatabase("HYDRATE_DB", MODE_PRIVATE, null);
            Log.d("Database", "Database is created.");


        } catch(Exception ex) {
            ex.printStackTrace();
            Log.e("Database", "Database is not created.");
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
        // DailyResult 테이블에 더미 데이터 삽입
        String insertDailyResult = "INSERT INTO DailyResult (date, success, water_drank) VALUES " +
                "('2023-12-01', 1, 1500), " +
                "('2023-12-02', 0, 1000), " +
                "('2023-12-03', 1, 1000), " +
                "('2023-12-04', 0, 1000), " +
                "('2023-12-05', 0, 1000), " +
                "('2023-12-06', 0, 1000), " +
                "('2023-12-07', 1, 1000), " +
                "('2023-12-08', 1, 1000), " +
                "('2023-12-09', 1, 1000), " +
                "('2023-12-10', 1, 1000), " +
                "('2023-12-11', 1, 2000);";
        db.execSQL(insertDailyResult);

        String insertToDo = "INSERT INTO ToDo (activity_name, activity_status, water_reward) VALUES " +
                "('운동하기', 1, 500), " +
                "('독서하기', 0, 300), " +
                "('코딩하기', 1, 700);";
        db.execSQL(insertToDo);
    }

    private void dropTables() {
        try {
            db.execSQL("DROP TABLE IF EXISTS DailyResult");
            db.execSQL("DROP TABLE IF EXISTS ToDo");

            Log.d("MyApp", "Debug message: All tables are dropped.");

        } catch(Exception ex) {
            ex.printStackTrace();
            println("Error dropping tables.");
        }

    }

}