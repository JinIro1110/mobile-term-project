package com.example.dailywater;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Date;

public class Activity2 extends AppCompatActivity implements FragmentDataListener {
    private setSharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_2);
        sharedPreferences = new setSharedPreferences(this);
        Button startButton = findViewById(R.id.startButton);
        // 초기 프래그먼트 설정
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
            // 이름 또는 몸무게가 설정되지 않았다면 info_input 프래그먼트로 이동
            initialFragment = new InfoInputFragment();
        } else {
            // 이름과 몸무게가 모두 설정되었다면 target_setting 프래그먼트로 이동
            initialFragment = new TargetSettingFragment();
        }

        fragmentTransaction.replace(R.id.fragment_container, initialFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void getNameWeight(String userName, int weight) {
        sharedPreferences.saveName(userName);
        sharedPreferences.saveWeight(weight);
    }

    @Override
    public void getLiters(int liters) {
        sharedPreferences.saveLiters(liters);
    }

    @Override
    public void getDate(Date startDate, Date endDate) {
        sharedPreferences.saveStartDate(startDate); // 날짜 형식 변환 필요
        sharedPreferences.saveEndDate(endDate); // 날짜 형식 변환 필요
    }

    @Override
    public void nextFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
