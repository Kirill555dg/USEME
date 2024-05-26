package com.example.useme.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MiniTaskAdapter extends RecyclerView.Adapter<com.example.useme.adapter.MiniTaskAdapter.MiniTaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private ArrayList<Integer> mDisabledRows;

    @NonNull
    @Override
    public com.example.useme.adapter.MiniTaskAdapter.MiniTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mini_task_item, parent, false);
        mDisabledRows = new ArrayList<>();
        mDisabledRows.add(0);
        return new com.example.useme.adapter.MiniTaskAdapter.MiniTaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.useme.adapter.MiniTaskAdapter.MiniTaskHolder holder, int position) {

        Task task = tasks.get(position);

        if (mDisabledRows.contains(position)) {
            holder.taskNumTV.setBackgroundResource(R.color.holo_red_light);
        } else {
            holder.taskNumTV.setBackgroundResource(R.color.mini_task);
        }

        holder.taskNumTV.setText(""+(position+1));
    }

    public void setMiniTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getTask(int index){
        return tasks.get(index);
    }

    public void disableRow(int index){
        mDisabledRows.clear();
        mDisabledRows.add(index);
        notifyDataSetChanged();
    }

    public class MiniTaskHolder extends RecyclerView.ViewHolder {

        private TextView taskNumTV;

        public MiniTaskHolder(@NonNull View itemView) {
            super(itemView);
            taskNumTV = itemView.findViewById(R.id.mini_task_num);
        }
    }
}