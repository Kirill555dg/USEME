package com.example.useme.app.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.adapter.HomeworkStatisticAdapter;
import com.example.useme.data.model.Homework;
import com.example.useme.data.model.statistic.Statistic;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.StatisticApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticFragment extends Fragment {

    private StatisticApi statisticApi;
    private List<Statistic> statisticList;
    private Long studentId;

    private RecyclerView recyclerView;
    private HomeworkStatisticAdapter adapter;

    public StatisticFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentId = getArguments().getLong("ID");
        } else {
            studentId = StudentActivity.id;
        }
        RetrofitService retrofitService = new RetrofitService();
        statisticApi = retrofitService.getRetrofit().create(StatisticApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        recyclerView = view.findViewById(R.id.statistics_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Statistic>> callGetStatistics = statisticApi.findStudentStatistic(studentId);
        callGetStatistics.enqueue(new Callback<List<Statistic>>() {
            @Override
            public void onResponse(Call<List<Statistic>> call, Response<List<Statistic>> response) {
                Log.d("RESPONSE", response.body().toString());
                statisticList = response.body();
                HashMap<Homework, Integer> completed = new HashMap<>();
                HashSet<Homework> homeworks = new HashSet<>();
                for (Statistic statistic : statisticList) {
                    Homework curHomework = statistic.getPk().getHomework();
                    homeworks.add(curHomework);
                    Integer countCompleted = completed.get(curHomework);
                    if (countCompleted == null) {
                        countCompleted = 0;
                    }
                    if (statistic.getCorrect()) countCompleted++;
                    completed.put(curHomework, countCompleted);
                }
                for (Homework homework : homeworks) {
                    homework.setCompletedTasks(completed.get(homework));
                }
                List<Homework> fullHomework = new ArrayList<>(homeworks);
                setStatisticAdapter(fullHomework);
            }

            @Override
            public void onFailure(Call<List<Statistic>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), "Ошибка подключения", Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        return view;
    }


    public void setStatisticAdapter(List<Homework> list){
        adapter = new HomeworkStatisticAdapter(getResources());
        adapter.setStatistics(list);
        recyclerView.setAdapter(adapter);
    }
}