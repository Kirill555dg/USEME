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
import com.example.useme.app.student.StudentActivity;
import com.example.useme.data.model.Homework;
import com.example.useme.data.model.statistic.Statistic;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.HomeworkApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeworkAdapter extends RecyclerView.Adapter<StudentHomeworkAdapter.HomeworkHolder>{
    private List<Homework> homeworks = new ArrayList<>();

    @NonNull
    @Override
    public StudentHomeworkAdapter.HomeworkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_homework_item, parent, false);



        return new StudentHomeworkAdapter.HomeworkHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentHomeworkAdapter.HomeworkHolder holder, int position) {

        Homework homework = homeworks.get(position);
        List<Long> studentIds = homework.getCompleted();
        Log.d("DEBUG", homework.toString());
        Log.d("DEBUG", studentIds.toString());
        holder.id = homework.getId();
        holder.titleTV.setText(homework.getTitle());
        holder.idTV.setText("#"+holder.id);
        holder.dateOfIssueTV.setText(homework.getDateOfIssue());
        holder.deadlineTV.setText(homework.getDeadline());
        holder.isCompleted = (studentIds.indexOf(StudentActivity.id) != -1);
        holder.completedTV.setText(holder.isCompleted ? "да" : "нет");
        holder.completedTV.setBackgroundResource(holder.isCompleted ? R.color.completed : R.color.failed);
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
        private TextView completedTV;
        private Boolean isCompleted;

        public HomeworkHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.student_homework_item_title);
            idTV = itemView.findViewById(R.id.student_homework_item_id);

            dateOfIssueTV = itemView.findViewById(R.id.student_homework_item_date_of_issue);
            deadlineTV = itemView.findViewById(R.id.student_homework_item_deadline);
            completedTV = itemView.findViewById(R.id.student_homework_item_completed);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetrofitService retrofitService = new RetrofitService();
                    HomeworkApi homeworkApi = retrofitService.getRetrofit().create(HomeworkApi.class);
                    Call<Homework> callGetHomework = homeworkApi.findHomework(id);
                    callGetHomework.enqueue(new Callback<Homework>() {
                        @Override
                        public void onResponse(Call<Homework> call, Response<Homework> response) {
                            Homework homework = response.body();

                            Bundle bundle = new Bundle();
                            bundle.putLong("ID", id);
                            bundle.putBoolean("COMPLETE", isCompleted);
                            Navigation.findNavController(itemView).navigate(R.id.action_homeworkFragment_to_solveFragment, bundle);
                        }

                        @Override
                        public void onFailure(Call<Homework> call, Throwable t) {
                        }
                    });
                }
            });
        }
    }
}
