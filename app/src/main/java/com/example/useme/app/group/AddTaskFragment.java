package com.example.useme.app.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import com.example.useme.adapter.AddTaskAdapter;
import com.example.useme.app.FilterTaskFragment;
import com.example.useme.data.model.Task;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.TaskApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddTaskFragment extends Fragment {

    private AddTaskAdapter adapter;
    RecyclerView recyclerView;
    private TaskApi taskApi;
    private static final String KEY_TASKS = "TASKS";
    private static List<Task> takedTasks;

    public AddTaskFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            takedTasks = (List<Task>) getArguments().getSerializable(KEY_TASKS);
        } else {
            takedTasks = new ArrayList<>();
        }
        Log.d("DEBUG", takedTasks.toString());
        adapter = new AddTaskAdapter();
        RetrofitService retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_TASKS, (Serializable) takedTasks);
                Log.d("DEBUG", takedTasks.toString());
                getParentFragment().setArguments(bundle);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        recyclerView = view.findViewById(R.id.add_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Task>> callGetTasks = taskApi.getAllTasks();
        callGetTasks.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                List<Task> tasks = response.body();
                for (Task takedTask : takedTasks) {
                    if (tasks.contains(takedTask)) {
                        tasks.remove(takedTask);
                    }
                }
                Log.d("DEBUG", takedTasks.toString());
                Log.d("DEBUG", tasks.toString());
                setTaskAdapter(tasks);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        Button filterButton = view.findViewById(R.id.add_task_filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterTaskFragment fragment = new FilterTaskFragment();
                fragment.setTargetFragment(AddTaskFragment.this, 0);
                fragment.show(getParentFragmentManager(), fragment.getClass().getName());
            }
        });

        return view;
    }

    private void setTaskAdapter(List<Task> list) {
        adapter = new AddTaskAdapter();
        adapter.setTasks(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent intent = data;
            Bundle args = intent.getBundleExtra("BUNDLE");
            List<Task> tasks = (ArrayList<Task>) args.getSerializable("TASKS");
            for (Task takedTask : takedTasks) {
                if (tasks.contains(takedTask)) {
                    tasks.remove(takedTask);
                }
            }
            setTaskAdapter(tasks);
            Log.d("DEBUG", tasks.toString());
        }
    }

    public static void addTask(Task task){
        takedTasks.add(task);
    }
}