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
        setContentView(R.layout.custom_target_page); // XML 레이아웃을 설정합니다.

        // '기간'을 표시할 TextView의 ID를 설정합니다.
        textViewPeriod = findViewById(R.id.textViewSelectedDate);

        // 날짜 형식을 정의합니다.
        sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);

// MaterialDatePicker의 날짜 범위 선택기를 설정합니다.
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        Calendar today = Calendar.getInstance();
        constraintBuilder.setStart(today.getTimeInMillis()); // 시작 날짜를 오늘로 설정
        constraintBuilder.setValidator(DateValidatorPointForward.from(today.getTimeInMillis())); // 오늘 이후의 날짜만 선택 가능

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("날짜 선택");
        builder.setCalendarConstraints(constraintBuilder.build());

// 종료 날짜를 선택하지 않은 상태로 시작 날짜를 오늘로 설정
        builder.setSelection(
                Pair.create(
                        today.getTimeInMillis(),
                        null // 종료 날짜는 선택되지 않은 상태로 둡니다.
                )
        );

        final MaterialDatePicker<Pair<Long, Long>> dateRangePicker = builder.build();

        // 날짜 범위 선택기를 표시하는 버튼의 리스너를 설정합니다.
        findViewById(R.id.button_show_picker).setOnClickListener(view -> dateRangePicker.show(getSupportFragmentManager(), dateRangePicker.toString()));

        // 사용자가 날짜를 선택하면 리스너에서 응답합니다.
        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            // 선택된 날짜 범위를 사용하여 TextView 업데이트
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(selection.first);
            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(selection.second);
            textViewPeriod.setText(String.format("기간: %s - %s", sdf.format(startDate.getTime()), sdf.format(endDate.getTime())));
        });
    }
}