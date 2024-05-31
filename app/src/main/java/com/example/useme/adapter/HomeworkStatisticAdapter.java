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

public class HomeworkStatisticAdapter extends RecyclerView.Adapter<HomeworkStatisticAdapter.HomeworkStatisticHolder> {

    private List<Statistic> statistics = new ArrayList<>();

    @NonNull
    @Override
    public HomeworkStatisticAdapter.HomeworkStatisticHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homework_statistic_item, parent, false);



        return new HomeworkStatisticAdapter.HomeworkStatisticHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkStatisticAdapter.HomeworkStatisticHolder holder, int position) {

        Statistic statistic = statistics.get(position);

        holder.id = statistic.getPk().getHomework().getId();
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }


    public class HomeworkStatisticHolder extends RecyclerView.ViewHolder {

        private Long id;
        private TextView titleTV;
        private TextView idTV;
        private TextView dateOfIssueTV;
        private TextView deadlineTV;
        private TextView completedTV;
        private Boolean isCompleted;

        public HomeworkStatisticHolder(@NonNull View itemView) {
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
