package com.example.dailywater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.TargetViewHolder> {

    private List<Routine> targetList;

    public TargetAdapter(List<Routine> targetList) {
        this.targetList = targetList;
    }

    @Override
    public TargetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_target, parent, false);
        return new TargetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TargetViewHolder holder, int position) {
        Routine routine = targetList.get(position);
        holder.targetStringTextView.setText("할일 " + String.valueOf(position + 1) + " : "+ routine.getRoutineName());
        holder.targetIntTextView.setText("물양 : " + String.valueOf(routine.getRoutineLiter()) + "ml");
    }

    @Override
    public int getItemCount() {
        return targetList.size();
    }

    public class TargetViewHolder extends RecyclerView.ViewHolder {
        public TextView targetStringTextView;
        public TextView targetIntTextView;

        public TargetViewHolder(View itemView) {
            super(itemView);
            targetStringTextView = itemView.findViewById(R.id.targetStringTextView);
            targetIntTextView = itemView.findViewById(R.id.targetIntTextView);
        }
    }
}
