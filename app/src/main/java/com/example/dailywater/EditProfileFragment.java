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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailywater.R;
import com.example.dailywater.SetSharedPreferences;
import com.example.dailywater.dto.ToDoItem;

import java.util.ArrayList;

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

        Button settingTodoButton = view.findViewById(R.id.setting_todo);
        settingTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditToDoFragment editTodoFragment = new EditToDoFragment();
                FragmentManager fragmentManager = getParentFragmentManager(); // API level에 따라 getFragmentManager() 또는 getParentFragmentManager() 사용
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, editTodoFragment);
                fragmentTransaction.addToBackStack(null); // 이전 프래그먼트로 돌아갈 수 있도록 백스택에 추가
                fragmentTransaction.commit();
            }
        });


        return view;
    }
}
