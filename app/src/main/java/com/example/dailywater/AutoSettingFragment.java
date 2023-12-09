package com.example.dailywater;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

public class AutoSettingFragment extends Fragment {
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auto_setting, container, false);
        Button nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setLiters(2600);
                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();

                // 2주 후의 날짜 계산
                calendar.add(Calendar.WEEK_OF_YEAR, 2);
                Date twoWeeksLater = calendar.getTime();

                listener.setDate(today, twoWeeksLater);
                listener.nextFragment(new TargetAddFragment());
            }
        });

        return view;
    }
}
