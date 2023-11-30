package com.example.dailywater;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.HashSet;

public class AchievementRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_rate_page);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_achievement_rate);
        }

        MaterialCalendarView materialCalendarView = findViewById(R.id.calendarView);

        HashSet<CalendarDay> greenDays = new HashSet<>();
        // 여기에 초록색으로 표시할 날짜들을 추가
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 10));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 11));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 12));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 13));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 14));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 14));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 16));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 17));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 18));
        greenDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 19));

        HashSet<CalendarDay> redDays = new HashSet<>();
        // 여기에 빨간색으로 표시할 날짜들을 추가
        redDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 15));
        redDays.add(CalendarDay.from(2023, Calendar.NOVEMBER, 20));

        // 초록색 데코레이터 추가
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return greenDays.contains(day);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(ContextCompat.getDrawable(AchievementRateActivity.this, R.drawable.green_selector));
            }
        });

        // 빨간색 데코레이터 추가
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return redDays.contains(day);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(ContextCompat.getDrawable(AchievementRateActivity.this, R.drawable.red_selector));
            }
        });

        // 날짜 선택 비활성화
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
