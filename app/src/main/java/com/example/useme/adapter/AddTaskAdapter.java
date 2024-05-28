package com.example.useme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class AddTaskAdapter extends RecyclerView.Adapter<AddTaskAdapter.AddTaskHolder> {

    private List<Task> tasks = new ArrayList<>();

    @NonNull
    @Override
    public AddTaskAdapter.AddTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_task_item, parent, false);
        return new AddTaskAdapter.AddTaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddTaskAdapter.AddTaskHolder holder, int position) {

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


    public class AddTaskHolder extends RecyclerView.ViewHolder {

        private TextView topicTV;
        private TextView idTV;
        private TextView subjectTV;
        private TextView categoryTV;
        private TextView conditionTV;
        private TextView answerTV;

        public AddTaskHolder(@NonNull View itemView) {
            super(itemView);

            topicTV = itemView.findViewById(R.id.add_task_item_topic);
            idTV = itemView.findViewById(R.id.add_task_item_id);
            subjectTV = itemView.findViewById(R.id.add_task_item_subject);
            categoryTV = itemView.findViewById(R.id.add_task_item_category);
            conditionTV = itemView.findViewById(R.id.add_task_item_condition);
            answerTV = itemView.findViewById(R.id.add_task_item_answer);

            Button addTaskButton = itemView.findViewById(R.id.add_task_item_button);
            addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tasks.remove(getAbsoluteAdapterPosition());
                    notifyItemRemoved(getAbsoluteAdapterPosition());
                }
            });
        }
    }
}