package com.example.dailywater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button buttonOpenCustomTarget = findViewById(R.id.button_open_custom_target);
        buttonOpenCustomTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomTargetActivity.class);
                startActivity(intent);
            }
        });


        Button buttonOpenCustomTarget1 = findViewById(R.id.button_open_achievement_rage);
        buttonOpenCustomTarget1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AchievementRateActivity.class);
                startActivity(intent);
            }
        });

        setContentView(R.layout.main_page_1);

    }
}