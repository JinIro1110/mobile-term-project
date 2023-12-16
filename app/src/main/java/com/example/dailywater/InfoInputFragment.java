package com.example.dailywater;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.Fragment;

public class InfoInputFragment extends Fragment {
    private FragmentDataListener listener;
    private TextView weightTextView;

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
        View view = inflater.inflate(R.layout.info_input, container, false);

        TextInputEditText nameEditText = view.findViewById(R.id.userName);
        Slider weightSlider = view.findViewById(R.id.weightSlider);
        Button submitButton = view.findViewById(R.id.nextButton);
        weightTextView = view.findViewById(R.id.weight);

        weightSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                // Slider 값이 변경될 때 호출됩니다.
                // TextView에 값을 업데이트합니다.
                weightTextView.setText(String.valueOf((int) value));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameEditText.getText().toString();
                int weight = (int) weightSlider.getValue();

                listener.setNameWeight(userName, weight);
                listener.nextFragment(new TargetSettingFragment());
            }
        });

        return view;
    }
}