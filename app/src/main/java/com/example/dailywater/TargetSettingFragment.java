package com.example.dailywater;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class TargetSettingFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.target_setting, container, false);

        // 버튼 클릭 리스너 및 로직 구현
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.nextFragment(new CustomTargetFragment());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.nextFragment(new AutoSettingFragment());
            }
        });


        return view;
    }
}