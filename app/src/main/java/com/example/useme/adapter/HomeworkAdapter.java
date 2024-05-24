package com.example.useme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.data.model.Homework;

import java.util.ArrayList;
import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkHolder>{
    private List<Homework> homeworks = new ArrayList<>();

    @NonNull
    @Override
    public HomeworkAdapter.HomeworkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homework_item, parent, false);



        return new HomeworkAdapter.HomeworkHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkAdapter.HomeworkHolder holder, int position) {

        Homework homework = homeworks.get(position);
        holder.id = homework.getId();
        holder.titleTV.setText(homework.getTitle());
        holder.idTV.setText("#"+holder.id);
        holder.dateOfIssueTV.setText(homework.getDateOfIssue());
        holder.deadlineTV.setText(homework.getDeadline());
        holder.countCompletedTV.setText(homework.getCountCompleted()+"/"+homework.getCountStudents());
    }

    public void setHomeworks(List<Homework> homeworks) {
        this.homeworks = homeworks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return homeworks.size();
    }


    public class HomeworkHolder extends RecyclerView.ViewHolder {

        private Long id;
        private TextView titleTV;
        private TextView idTV;
        private TextView dateOfIssueTV;
        private TextView deadlineTV;
        private TextView countCompletedTV;

        public HomeworkHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.homework_item_title);
            idTV = itemView.findViewById(R.id.homework_item_id);

            dateOfIssueTV = itemView.findViewById(R.id.homework_item_date_of_issue);
            deadlineTV = itemView.findViewById(R.id.homework_item_deadline);
            countCompletedTV = itemView.findViewById(R.id.homework_item_count_completed);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("ID", id);
                    Navigation.findNavController(itemView).navigate(R.id.action_teacherGroupsFragment_to_groupActivity, bundle);
                }
            });*/
        }
    }
}
