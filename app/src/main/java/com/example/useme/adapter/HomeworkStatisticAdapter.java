package com.example.useme.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

    private List<Homework> statistics = new ArrayList<>();
    private Resources recources;

    public HomeworkStatisticAdapter(Resources resources) {
        this.recources = resources;
    }

    @NonNull
    @Override
    public HomeworkStatisticAdapter.HomeworkStatisticHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homework_statistic_item, parent, false);



        return new HomeworkStatisticAdapter.HomeworkStatisticHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkStatisticAdapter.HomeworkStatisticHolder holder, int position) {

        Homework homework = statistics.get(position);

        holder.id = homework.getId();
        holder.titleTV.setText(homework.getTitle());
        holder.idTV.setText("#"+holder.id);
        holder.dateOfIssueTV.setText(homework.getDateOfIssue());
        holder.groupIdTV.setText("#"+homework.getGroup().getId());
        holder.ratioTV.setText(homework.getCompletedTasks() + "/"+ homework.getCountTasks());
        Integer ratio = (100*homework.getCompletedTasks())/homework.getCountTasks();
        holder.ratio = ratio;
        holder.progressBar.setProgress(ratio);
        LayerDrawable layerDrawable = (LayerDrawable) holder.progressBar.getProgressDrawable();
        ClipDrawable clipDrawable = (ClipDrawable) layerDrawable.getDrawable(1);
        GradientDrawable gradientDrawable = (GradientDrawable) clipDrawable.getDrawable();
        if (ratio <= 45) {
            gradientDrawable.setColor(recources.getColor(R.color.bad));
        } else if (ratio <= 75) {
            gradientDrawable.setColor(recources.getColor(R.color.medium));
        } else {
            gradientDrawable.setColor(recources.getColor(R.color.good));
        }
    }

    public void setStatistics(List<Homework> statistics) {
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
        private TextView groupIdTV;
        private int ratio;
        private TextView ratioTV;
        private ProgressBar progressBar;

        public HomeworkStatisticHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.title);
            idTV = itemView.findViewById(R.id.id);

            dateOfIssueTV = itemView.findViewById(R.id.date_of_issue);
            groupIdTV = itemView.findViewById(R.id.group_id);
            ratioTV = itemView.findViewById(R.id.ratio);
            progressBar = itemView.findViewById(R.id.PB_statistic);


            /*itemView.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }
    }

}
