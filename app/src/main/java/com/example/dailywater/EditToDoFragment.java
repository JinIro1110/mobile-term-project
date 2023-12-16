package com.example.dailywater;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dailywater.dto.ToDoItem;

import java.util.ArrayList;

public class EditToDoFragment extends Fragment {

    private RecyclerView recyclerView;
    private UpdateAdapter updateAdapter;
    private ArrayList<ToDoItem> toDoItems;
    ArrayList<Integer> updatedItemIds = new ArrayList<>();
    ArrayList<Integer> deletedItemIds = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // fragment_edit_to_do 레이아웃을 사용합니다. XML 파일 이름을 확인해 주세요.
        View view = inflater.inflate(R.layout.target_update, container, false);

        // 데이터베이스에서 ToDo 항목들을 로드합니다.
        toDoItems = getToDoItemsFromDatabase();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // updateAdapter를 초기화하고 RecyclerView에 설정합니다.
        updateAdapter = new UpdateAdapter(toDoItems, this);
        recyclerView.setAdapter(updateAdapter);

        Button saveButton = view.findViewById(R.id.savelist);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateToDoItemsToDatabase();// 변경사항 저장
                navigateToEditProfileFragment(); // EditProfileFragment로 전환
            }
        });

        return view;
    }

    private ArrayList<ToDoItem> getToDoItemsFromDatabase() {
        // Fragment의 Context를 사용하여 데이터베이스를 엽니다.
        SQLiteDatabase db = getActivity().openOrCreateDatabase("HYDRATE_DB", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM ToDo", null);
        ArrayList<ToDoItem> toDoItems = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String activityName = cursor.getString(cursor.getColumnIndex("activity_name"));
            int activityStatus = cursor.getInt(cursor.getColumnIndex("activity_status"));
            int waterReward = cursor.getInt(cursor.getColumnIndex("water_reward"));
            toDoItems.add(new ToDoItem(id, activityName, activityStatus, waterReward));
        }

        cursor.close();
        db.close();
        return toDoItems;
    }


    public void showEditDialog(final int position, ToDoItem toDoItem) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText targetNameEditText = dialogView.findViewById(R.id.targetNameEditText);
        final EditText targetLiterEditText = dialogView.findViewById(R.id.targetLiterEditText);

        // 기존 값 설정
        targetNameEditText.setText(toDoItem.getActivityName());
        targetLiterEditText.setText(String.valueOf(toDoItem.getWaterReward()));

        dialogBuilder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 사용자 입력을 통해 ToDo 항목 업데이트
                String name = targetNameEditText.getText().toString();
                int reward = Integer.parseInt(targetLiterEditText.getText().toString());
                ToDoItem updatedItem = new ToDoItem(toDoItems.get(position).getId(), name, toDoItems.get(position).getActivityStatus(), reward); // status는 예시로 0 설정
                toDoItems.set(position, updatedItem);
                updateAdapter.notifyDataSetChanged();
                updatedItemIds.add(toDoItems.get(position).getId());
            }
        });

        dialogBuilder.setNegativeButton("취소", null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void showDeleteDialog(final int position, ToDoItem toDoItem) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("항목 삭제");
        dialogBuilder.setMessage("이 항목을 삭제하시겠습니까?");

        dialogBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ToDo 항목 삭제 로직
                int itemId = toDoItems.get(position).getId();
                toDoItems.remove(position);

                // 삭제된 항목의 ID를 배열에 추가
                deletedItemIds.add(itemId);

                updateAdapter.notifyDataSetChanged();
            }
        });

        dialogBuilder.setNegativeButton("취소", null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void updateToDoItemsToDatabase() {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("HYDRATE_DB", Context.MODE_PRIVATE, null);

        for (Integer itemId : updatedItemIds) {
            // updatedItemIds 배열에 있는 항목의 ID로 데이터베이스 업데이트 수행
            // 해당 ID를 가진 항목을 toDoItems에서 찾습니다.
            ToDoItem itemToUpdate = findItemById(itemId);

            if (itemToUpdate != null) {
                String newName = itemToUpdate.getActivityName();
                int newReward = itemToUpdate.getWaterReward();

                // 데이터베이스 업데이트 쿼리 실행
                db.execSQL("UPDATE ToDo SET activity_name = ?, water_reward = ? WHERE _id = ?",
                        new String[]{newName, String.valueOf(newReward), String.valueOf(itemId)});
            }
        }

        // 삭제할 항목 처리
        for (Integer itemId : deletedItemIds) {
            db.execSQL("DELETE FROM ToDo WHERE _id = ?", new String[]{String.valueOf(itemId)});
        }


        // 데이터베이스 작업 완료 후 데이터베이스 닫기
        db.close();
    }

    private ToDoItem findItemById(int itemId) {
        for (ToDoItem item : toDoItems) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null; // 해당 ID를 가진 항목을 찾지 못한 경우
    }


    private void navigateToEditProfileFragment() {
        // EditProfileFragment로 전환하는 로직 구현
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, new EditProfileFragment())
                .commit();
    }
}
