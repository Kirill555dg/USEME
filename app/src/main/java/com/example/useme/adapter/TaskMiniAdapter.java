package com.example.useme.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.app.group.CreateHomeworkFragment;
import com.example.useme.app.group.GroupActivity;
import com.example.useme.app.group.TaskInfoDialogFragment;
import com.example.useme.data.model.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskMiniAdapter extends RecyclerView.Adapter<TaskMiniAdapter.TaskMiniHolder> {

    private List<Task> tasks = new ArrayList<>();
    private Context context;
    private Fragment fragment;

    public TaskMiniAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TaskMiniAdapter.TaskMiniHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_mini_item, parent, false);
        return new TaskMiniAdapter.TaskMiniHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskMiniAdapter.TaskMiniHolder holder, int position) {

        Task task = tasks.get(position);

        holder.topicTV.setText("Задача №"+task.getTopicPK().getTopicNumber());
        holder.idTV.setText("#"+task.getId());
        holder.subjectTV.setText(task.getTopicPK().getSubject());
        holder.categoryTV.setText(task.getCategory());
        holder.conditionTV.setText(task.getCondition());
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class TaskMiniHolder extends RecyclerView.ViewHolder {

        private TextView topicTV;
        private TextView idTV;
        private TextView subjectTV;
        private TextView categoryTV;
        private TextView conditionTV;

        public TaskMiniHolder(@NonNull View itemView) {
            super(itemView);

            topicTV = itemView.findViewById(R.id.task_item_topic);
            idTV = itemView.findViewById(R.id.task_item_id);
            subjectTV = itemView.findViewById(R.id.task_item_subject);
            categoryTV = itemView.findViewById(R.id.task_item_category);
            conditionTV = itemView.findViewById(R.id.task_item_condition);

            Button deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("Вы точно хотите удалить задачу "+ idTV.getText() +" из Д/З?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tasks.remove(getAbsoluteAdapterPosition());
                                    notifyItemRemoved(getAbsoluteAdapterPosition());
                                }
                            })
                            .setNegativeButton("Нет", null)
                            .show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskInfoDialogFragment taskInfoDialogFragment = new TaskInfoDialogFragment(tasks.get(getAbsoluteAdapterPosition()));
                    taskInfoDialogFragment.show(fragment.getParentFragmentManager(), taskInfoDialogFragment.getClass().getName());
                }
            });
        }
    }
}
