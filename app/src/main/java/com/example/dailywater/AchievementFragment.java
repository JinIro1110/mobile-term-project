package com.example.dailywater;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class AchievementFragment extends Fragment {

    @SuppressLint("StringFormatInvalid")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.achievement_fragment, container, false);


        SetSharedPreferences sharedPreferencesManager = new SetSharedPreferences(getContext());
        int consecutiveDays = sharedPreferencesManager.calculateConsecutiveSuccessDays();

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        float successPercentage = calculateSuccessPercentage(sharedPreferencesManager);
        progressBar.setProgress((int) successPercentage);


        TextView textViewGoalAchievement = view.findViewById(R.id.textViewGoalAchievement);
        textViewGoalAchievement.setText(getString(R.string.goal_achievement_text, consecutiveDays));

        TextView textViewProgressPercentage = view.findViewById(R.id.textViewProgressPercentage);
        textViewProgressPercentage.setText(String.format(Locale.getDefault(), "%.0f%%", successPercentage));

        MaterialCalendarView materialCalendarView = view.findViewById(R.id.calendarView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            HashSet<CalendarDay> greenDays = convertStringsToCalendarDays(bundle.getStringArrayList("greenDays"));
            HashSet<CalendarDay> redDays = convertStringsToCalendarDays(bundle.getStringArrayList("redDays"));

            addColorDecorator(materialCalendarView, greenDays, R.drawable.green_selector);

            addColorDecorator(materialCalendarView, redDays, R.drawable.red_selector);
        }

        return view;
    }

    private void addColorDecorator(MaterialCalendarView calendarView, HashSet<CalendarDay> days, int drawableRes) {
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return days.contains(day);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), drawableRes));
            }
        });
    }

    private HashSet<CalendarDay> convertStringsToCalendarDays(ArrayList<String> datesList) {
        HashSet<CalendarDay> days = new HashSet<>();
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
        }

        for (String dateString : datesList) {
            LocalDate date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = LocalDate.parse(dateString, formatter);
            }
            CalendarDay day = CalendarDay.from(date);
            days.add(day);
        }

        return days;
    }

    private float calculateSuccessPercentage(SetSharedPreferences sharedPreferencesManager) {
        Date startDate = sharedPreferencesManager.getStartDate();
        Date endDate = sharedPreferencesManager.getEndDate();
        Date today = new Date();

        if (startDate == null || endDate == null) {
            return 0;
        }

        long totalDuration = endDate.getTime() - startDate.getTime();
        long durationUntilToday = today.getTime() - startDate.getTime();
        if (today.after(endDate)) {
            durationUntilToday = totalDuration;
        }

        float percentage = (totalDuration > 0) ? ((float) durationUntilToday / totalDuration) * 100 : 0;
        return Math.min(percentage, 100);
    }
}