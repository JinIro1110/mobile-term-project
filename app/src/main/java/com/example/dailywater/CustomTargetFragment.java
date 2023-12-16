package com.example.dailywater;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomTargetFragment extends Fragment {
    private TextView textViewPeriod;
    private SimpleDateFormat sdf;
    private FragmentDataListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentDataListener) {
            listener = (FragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentDataListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_target_page, container, false);

        textViewPeriod = view.findViewById(R.id.textViewSelectedDate);
        sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        Calendar today = Calendar.getInstance();
        constraintBuilder.setStart(today.getTimeInMillis());
        constraintBuilder.setValidator(DateValidatorPointForward.from(today.getTimeInMillis()));

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

        Button buttonShowPicker = view.findViewById(R.id.button_show_picker);
        buttonShowPicker.setOnClickListener(view1 -> dateRangePicker.show(getParentFragmentManager(), dateRangePicker.toString()));

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(selection.first);
            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(selection.second);
            textViewPeriod.setText(String.format("기간: %s - %s", sdf.format(startDate.getTime()), sdf.format(endDate.getTime())));

            // Pass the selected dates back to the activity
            listener.setDate(startDate.getTime(), endDate.getTime());
            // Navigate to the next fragment
            listener.nextFragment(new TargetAddFragment());
        });

        return view;
    }
}