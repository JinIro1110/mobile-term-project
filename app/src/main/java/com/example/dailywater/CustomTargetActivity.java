package com.example.dailywater;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomTargetActivity extends AppCompatActivity {

    private TextView textViewPeriod;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_target_page);

        textViewPeriod = findViewById(R.id.textViewSelectedDate);

        sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);


        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        Calendar today = Calendar.getInstance();
        constraintBuilder.setStart(today.getTimeInMillis());
        constraintBuilder.setValidator(DateValidatorPointForward.from(today.getTimeInMillis())); // 오늘 이후의 날짜만 선택 가능

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("날짜 선택");
        builder.setCalendarConstraints(constraintBuilder.build());

        builder.setSelection(
                Pair.create(
                        today.getTimeInMillis(),
                        null
                )
        );

        final MaterialDatePicker<Pair<Long, Long>> dateRangePicker = builder.build();

        findViewById(R.id.button_show_picker).setOnClickListener(view -> dateRangePicker.show(getSupportFragmentManager(), dateRangePicker.toString()));

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(selection.first);
            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(selection.second);
            textViewPeriod.setText(String.format("기간: %s - %s", sdf.format(startDate.getTime()), sdf.format(endDate.getTime())));
        });
    }
}