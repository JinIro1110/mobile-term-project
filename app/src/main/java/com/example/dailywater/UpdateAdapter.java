package com.example.dailywater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dailywater.dto.ToDoItem; // ToDoItem 클래스를 import 합니다.

import java.util.List;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.TargetViewHolder> {

    private List<ToDoItem> toDoItems;
    private EditToDoFragment fragment;

    // 생성자 추가
    public UpdateAdapter(List<ToDoItem> toDoItems, EditToDoFragment fragment) {
        this.toDoItems = toDoItems;
        this.fragment = fragment;
    }


    // 수정: 생성자에 Fragment 참조 추가
    @Override
    public TargetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_target, parent, false);
        return new TargetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TargetViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ToDoItem toDoItem = toDoItems.get(position);
        holder.targetStringTextView.setText("할일 " + (position + 1) + " : " + toDoItem.getActivityName());
        holder.targetIntTextView.setText("물양 : " + toDoItem.getWaterReward() + "ml");

        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showEditDialog(position, toDoItems.get(position));
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showDeleteDialog(position, toDoItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoItems.size();
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder {
        public TextView targetStringTextView;
        public TextView targetIntTextView;
        public ImageButton updateButton;
        public ImageButton deleteButton;

        public TargetViewHolder(View itemView) {
            super(itemView);
            targetStringTextView = itemView.findViewById(R.id.targetStringTextView);
            targetIntTextView = itemView.findViewById(R.id.targetIntTextView);
            updateButton = itemView.findViewById(R.id.update);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }
}
