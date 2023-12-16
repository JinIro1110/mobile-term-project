    package com.example.dailywater;

    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;
    import java.util.List;

    public class TargetAddFragment extends Fragment {

        private List<Routine> targetList = new ArrayList<>();
        private RecyclerView recyclerView;
        private TargetAdapter targetAdapter;
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
            View view = inflater.inflate(R.layout.target_add, container, false);

            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            targetAdapter = new TargetAdapter(targetList);
            recyclerView.setAdapter(targetAdapter);

            Button addButton = view.findViewById(R.id.addButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewTarget();
                }
            });

            Button nextButton = view.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.changeActivity1(targetList);
                }
            });

            return view;
        }

        private void addNewTarget() {
            // 다이얼로그 레이아웃을 인플레이트
            View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);

            // 다이얼로그 빌더 생성
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setView(dialogView);

            // 다이얼로그의 할일(목표 이름)와 리터(목표 양) 입력란 참조
            EditText targetNameEditText = dialogView.findViewById(R.id.targetNameEditText);
            EditText targetLiterEditText = dialogView.findViewById(R.id.targetLiterEditText);

            // 다이얼로그의 확인 버튼 설정
            dialogBuilder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 사용자 입력 가져오기
                    String targetName = targetNameEditText.getText().toString();
                    String targetLiterString = targetLiterEditText.getText().toString();

                    // 입력이 비어있지 않은지 확인
                    if (!TextUtils.isEmpty(targetName) && !TextUtils.isEmpty(targetLiterString)) {
                        int targetLiter = Integer.parseInt(targetLiterString);
                        // 여기에서 목록에 추가하는 작업을 수행
                        Routine newRoutine = new Routine(targetName, targetLiter);
                        targetList.add(newRoutine);
                        targetAdapter.notifyDataSetChanged();
                    } else {
                        // 입력이 비어있을 경우 사용자에게 메시지 표시 (선택 사항)
                        Toast.makeText(getActivity(), "할일과 리터를 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 다이얼로그의 취소 버튼 설정
            dialogBuilder.setNegativeButton("취소", null);

            // 다이얼로그 표시
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }
    }
