package com.example.dailywater;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity2 extends AppCompatActivity implements FragmentDataListener {
    private setSharedPreferences sharedPreferences;
    private Date StartDate;
    private Date EndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_2);
        sharedPreferences = new setSharedPreferences(this);
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.textView).setVisibility(View.GONE);
                findViewById(R.id.waterBottleImageView).setVisibility(View.GONE);
                startButton.setVisibility(View.GONE);
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                navigateToInitialFragment();
            }
        });
    }

    private void navigateToInitialFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        String userName = sharedPreferences.getUserName();
        int weight = sharedPreferences.getWeight();

        Fragment initialFragment;
        if (userName == null || weight == 0) {
            initialFragment = new InfoInputFragment();
        } else {
            initialFragment = new TargetSettingFragment();
        }

        fragmentTransaction.replace(R.id.fragment_container, initialFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void setNameWeight(String userName, int weight) {
        sharedPreferences.saveName(userName);
        sharedPreferences.saveWeight(weight);
    }

    @Override
    public void setLiters(int liters) {
        sharedPreferences.saveLiters(liters);
    }

    @Override
    public void setDate(Date startDate, Date endDate) {
        this.StartDate = startDate;
        this.EndDate = endDate;
        sharedPreferences.saveStartDate(startDate);
        sharedPreferences.saveEndDate(endDate);
    }

    @Override
    public void nextFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void changeActivity1(List<Routine> targetList) {
        SQLiteDatabase db;
        db = openOrCreateDatabase(
                "HYDRATE_DB",
                Activity.MODE_PRIVATE,
                null);

        for (Routine routine : targetList) {
            String targetName = routine.getRoutineName();
            int targetLiter = routine.getRoutineLiter();

            // INSERT 문을 사용하여 데이터 삽입
            String insertQuery = "INSERT INTO ToDo (activity_name, activity_status, water_reward) " +
                    "VALUES ('" + targetName + "', 0, " + targetLiter + ")";
            db.execSQL(insertQuery);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StartDate);
        while (!calendar.getTime().after(EndDate)) {
            String currentDateStr = sdf.format(calendar.getTime());

            // INSERT 문을 사용하여 날짜 데이터 삽입
            String insertDateQuery = "INSERT INTO DailyResult (date, success, water_drank) " +
                    "VALUES ('" + currentDateStr + "', 0, 0)";
            db.execSQL(insertDateQuery);

            // 날짜를 하루씩 증가
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // 데이터베이스 연결 종료
        db.close();

        Intent intent = new Intent(this, Activity1.class);
        startActivity(intent);
        finish();
    }
}