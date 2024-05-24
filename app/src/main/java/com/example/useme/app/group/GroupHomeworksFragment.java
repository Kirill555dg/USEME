package com.example.useme.app.group;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.adapter.TeacherHomeworkAdapter;
import com.example.useme.data.model.Homework;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.HomeworkApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupHomeworksFragment extends Fragment {


    private TeacherHomeworkAdapter adapter;
    private RecyclerView recyclerView;
    private HomeworkApi homeworkApi;

    public GroupHomeworksFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        homeworkApi = retrofitService.getRetrofit().create(HomeworkApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_homeworks, container, false);

        recyclerView = view.findViewById(R.id.homeworks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Homework>> callGetGroupHomeworks = homeworkApi.findGroupHomeworks(GroupActivity.id);
        callGetGroupHomeworks.enqueue(new Callback<List<Homework>>() {
            @Override
            public void onResponse(Call<List<Homework>> call, Response<List<Homework>> response) {
                List<Homework> homeworks = response.body();
                Log.d("RESPONSE", homeworks.toString());
                for (int i = 0; i < homeworks.size(); i++) {
                    Homework homework = homeworks.get(i);
                    homework.setCountStudents(GroupActivity.countMembers);
                }
                setHomeworkAdapter(homeworks);
            }

            @Override
            public void onFailure(Call<List<Homework>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        FloatingActionButton addTaskButton = view.findViewById(R.id.add_homework_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_groupHomeworksFragment_to_addHomeworkFragment);
            }
        });

        return view;
    }

    public void setHomeworkAdapter(List<Homework> list){
        adapter = new TeacherHomeworkAdapter();
        adapter.setHomeworks(list);
        recyclerView.setAdapter(adapter);
    }
}