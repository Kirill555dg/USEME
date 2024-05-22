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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> tasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {

        Task task = tasks.get(position);

        holder.topicTV.setText("Задача №"+task.getTopicPK().getTopicNumber());
        holder.idTV.setText("#"+task.getId());
        holder.subjectTV.setText(task.getTopicPK().getSubject());
        holder.categoryTV.setText(task.getCategory());
        holder.conditionTV.setText(task.getCondition());
        holder.answerTV.setText(task.getAnswer());
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        private TextView topicTV;
        private TextView idTV;
        private TextView subjectTV;
        private TextView categoryTV;
        private TextView conditionTV;
        private TextView answerTV;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            topicTV = itemView.findViewById(R.id.task_item_topic);
            idTV = itemView.findViewById(R.id.task_item_id);
            subjectTV = itemView.findViewById(R.id.task_item_subject);
            categoryTV = itemView.findViewById(R.id.task_item_category);
            conditionTV = itemView.findViewById(R.id.task_item_condition);
            answerTV = itemView.findViewById(R.id.task_item_answer);
        }
    }
}
