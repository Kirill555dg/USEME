package com.example.useme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class NumTaskAdapter extends RecyclerView.Adapter<NumTaskAdapter.MiniTaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private Integer ChosenTask;
    private ArrayList<Integer> CompletedTasks;

    @NonNull
    @Override
    public NumTaskAdapter.MiniTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.num_task_item, parent, false);
        ChosenTask = 0;
        return new NumTaskAdapter.MiniTaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NumTaskAdapter.MiniTaskHolder holder, int position) {

        Task task = tasks.get(position);

        if (ChosenTask == position) {
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

    public void setChosenTask(int index){
        ChosenTask = index;
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