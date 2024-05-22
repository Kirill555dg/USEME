package com.example.useme.app.teacher;

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
import com.example.useme.adapter.TaskAdapter;
import com.example.useme.data.model.Task;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.StudentApi;
import com.example.useme.retrofit.api.TaskApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskFragment extends Fragment {

    private TaskAdapter adapter;
    private TaskApi taskApi;

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public TaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter();
        RetrofitService retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);

        Call<List<Task>> callGetTasks = taskApi.getAllTasks();
        callGetTasks.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                adapter.setTasks(response.body());
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}