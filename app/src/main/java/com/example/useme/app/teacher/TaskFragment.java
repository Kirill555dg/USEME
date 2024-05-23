package com.example.useme.app.teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.adapter.TaskAdapter;
import com.example.useme.app.FilterTaskFragment;
import com.example.useme.app.authorization.RegistrationTeacherFragment;
import com.example.useme.data.model.Task;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.StudentApi;
import com.example.useme.retrofit.api.TaskApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskFragment extends Fragment  {

    private TaskAdapter adapter;
    RecyclerView recyclerView;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Task>> callGetTasks = taskApi.getAllTasks();
        callGetTasks.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                setTaskAdapter(response.body());
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        FloatingActionButton addTaskButton = view.findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_taskFragment_to_createTaskFragment);
            }
        });

        Button filterButton = view.findViewById(R.id.task_filter_button);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterTaskFragment fragment = new FilterTaskFragment();
                fragment.setTargetFragment(TaskFragment.this, 0);
                fragment.show(getParentFragmentManager(), fragment.getClass().getName());
            }
        });

        return view;
    }

    public void setTaskAdapter(List<Task> list){
        adapter = new TaskAdapter();
        adapter.setTasks(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent intent = data;
            Bundle args = intent.getBundleExtra("BUNDLE");
            ArrayList<Task> tasks = (ArrayList<Task>) args.getSerializable("TASKS");
            setTaskAdapter(tasks);
            Log.d("DEBUG", tasks.toString());
        }
    }
}