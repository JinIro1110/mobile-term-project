package com.example.dailywater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dailywater.R;
import com.example.dailywater.SetSharedPreferences;

public class EditProfileFragment extends Fragment {
    private EditText editName;
    private EditText editWeight;
    private SetSharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_fragment, container, false);

        sharedPreferences = new SetSharedPreferences(getActivity());

        editName = view.findViewById(R.id.editName);
        editWeight = view.findViewById(R.id.editWeight);

        String userName = sharedPreferences.getUserName();
        int weight = sharedPreferences.getWeight();

        editName.setText(userName);
        editWeight.setText(weight > 0 ? String.valueOf(weight) : "");

        // "수정하기" 버튼에 리스너 설정
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editName.getText().toString();
                int weight = Integer.parseInt(editWeight.getText().toString());

                sharedPreferences.saveName(userName);
                sharedPreferences.saveWeight(weight);

                Toast.makeText(getActivity(), "프로필이 업데이트 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
