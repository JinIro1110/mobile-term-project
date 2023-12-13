package com.example.dailywater;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailywater.dto.ToDoItem;

import java.util.ArrayList;

public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.ViewHolder> {
    private ArrayList<ToDoItem> toDoItems;
    SQLiteDatabase db;
    public CustomMenuAdapter(ArrayList<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }
    private SetSharedPreferences sharedPreferencesManager;
    private Context context;
    private OnDrinkAmountChangedListener listener;

    public CustomMenuAdapter(Context context, ArrayList<ToDoItem> toDoItems, OnDrinkAmountChangedListener listener) {
        this.context = context;
        this.toDoItems = toDoItems;
        this.sharedPreferencesManager = new SetSharedPreferences(context);
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoItem item = toDoItems.get(position);
        holder.activityName.setText(item.getActivityName());
        holder.activityStatus.setText(item.getActivityStatus() == 1 ? "완료" : "대기");
        holder.waterReward.setText(String.valueOf(item.getWaterReward()) + " ml");
        holder.checkBoxStatus.setChecked(item.getActivityStatus() == 1);
        // 체크박스 클릭 리스너 설정
        holder.checkBoxStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 체크 상태를 토글합니다.
                boolean isChecked = holder.checkBoxStatus.isChecked();
                // 데이터베이스에 상태 변경을 반영합니다.
                updateToDoStatus(holder.itemView.getContext(), item.getId(), isChecked ? 1 : 0);
                // 내부 데이터를 업데이트합니다.
                item.setActivityStatus(isChecked ? 1 : 0);

                int currentLiters = sharedPreferencesManager.getLiters();

                // 체크 상태에 따라 리터 값을 증가시키거나 감소시킵니다.
                if (isChecked) {
                    // 체크 됐을 때는 water_reward 값을 추가합니다.
                    sharedPreferencesManager.incrementLitersByAmount(item.getWaterReward());
                } else {
                    // 체크 해제 됐을 때는 water_reward 값을 감소시킵니다.
                    sharedPreferencesManager.decrementLitersByAmount(item.getWaterReward());
                }
                listener.onDrinkAmountChanged();

            }
        });
    }

    // 데이터베이스에 ToDo 상태를 업데이트하는 메서드
    private void updateToDoStatus(Context context, int id, int status) {
        db = context.openOrCreateDatabase("HYDRATE_DB", MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("activity_status", status);
        db.update("ToDo", contentValues, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public int getItemCount() {
        return toDoItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxStatus;
        TextView activityName;
        TextView activityStatus;
        TextView waterReward;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBoxStatus = itemView.findViewById(R.id.checkBoxStatus);
            activityName = itemView.findViewById(R.id.activityName);
            activityStatus = itemView.findViewById(R.id.activityStatus);
            waterReward = itemView.findViewById(R.id.waterReward);
        }
    }
}

