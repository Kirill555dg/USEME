package com.example.useme.adapter;

import android.util.Log;
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

public class NumTaskAdapter extends RecyclerView.Adapter<NumTaskAdapter.NumTaskHolder> {

    private List<Task> tasks = new ArrayList<>();
    private Integer chosenTask;
    private List<Long> completedTasksIds;

    @NonNull
    @Override
    public NumTaskAdapter.NumTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.num_task_item, parent, false);
        return new NumTaskAdapter.NumTaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NumTaskAdapter.NumTaskHolder holder, int position) {

        Task task = tasks.get(position);

        Log.d("chosenTask", String.valueOf(chosenTask));
        Log.d("position", String.valueOf(position));
        if (chosenTask == position) {
            holder.taskNumTV.setBackgroundResource(R.color.holo_red_light);
        } else {
            Log.d("COMPLETED TASKS", completedTasksIds.toString());
            Log.d("TASKID", String.valueOf(task.getId()));
            if (completedTasksIds.contains(task.getId())) {
                holder.taskNumTV.setBackgroundResource(R.color.answered);
            } else {
                holder.taskNumTV.setBackgroundResource(R.color.mini_task);
            }
        }
        holder.taskNumTV.setText(""+(position+1));
    }

    public void setTasks(List<Task> tasks, List<Long> completedTasksIds) {
        this.tasks = tasks;
        this.completedTasksIds = completedTasksIds;
        notifyDataSetChanged();
    }

    public void setCompletedTasks(List<Long> completedTasksIds) {
        this.completedTasksIds = completedTasksIds;
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
        chosenTask = index;
        Log.d("CHOSEN TASK", String.valueOf(index));
        notifyDataSetChanged();
    }

    public class NumTaskHolder extends RecyclerView.ViewHolder {

        private TextView taskNumTV;

        public NumTaskHolder(@NonNull View itemView) {
            super(itemView);
            taskNumTV = itemView.findViewById(R.id.mini_task_num);
        }
    }
}