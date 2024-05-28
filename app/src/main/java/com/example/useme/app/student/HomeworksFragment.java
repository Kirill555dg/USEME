package com.example.useme.app.student;

import android.os.Bundle;

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
import com.example.useme.adapter.StudentHomeworkAdapter;
import com.example.useme.adapter.TeacherHomeworkAdapter;
import com.example.useme.app.group.GroupActivity;
import com.example.useme.data.model.Homework;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.HomeworkApi;
import com.example.useme.retrofit.api.StatisticApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeworksFragment extends Fragment {

    private StudentHomeworkAdapter adapter;
    private RecyclerView recyclerView;
    private HomeworkApi homeworkApi;

    private static final String KEY_GROUP_ID = "ID";
    private Long id;

    public HomeworksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        homeworkApi = retrofitService.getRetrofit().create(HomeworkApi.class);
        if (getArguments() != null) {
            id = getArguments().getLong(KEY_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, container, false);

        recyclerView = view.findViewById(R.id.student_homeworks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Call<List<Homework>> callGetGroupHomeworks = homeworkApi.findGroupHomeworks(id);
        callGetGroupHomeworks.enqueue(new Callback<List<Homework>>() {
            @Override
            public void onResponse(Call<List<Homework>> call, Response<List<Homework>> response) {
                List<Homework> homeworks = response.body();
                Log.d("RESPONSE", homeworks.toString());
                setHomeworkAdapter(homeworks);
            }

            @Override
            public void onFailure(Call<List<Homework>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    public void setHomeworkAdapter(List<Homework> list){
        adapter = new StudentHomeworkAdapter();
        adapter.setHomeworks(list);
        recyclerView.setAdapter(adapter);
    }
}