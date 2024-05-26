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
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.adapter.StudentGroupAdapter;
import com.example.useme.adapter.TeacherGroupAdapter;
import com.example.useme.app.teacher.TeacherActivity;
import com.example.useme.data.model.Group;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentGroupsFragment extends Fragment {

    private StudentGroupAdapter adapter;
    private RecyclerView recyclerView;
    private GroupApi groupApi;

    public StudentGroupsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        groupApi = retrofitService.getRetrofit().create(GroupApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_groups, container, false);

        recyclerView = view.findViewById(R.id.student_groups_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Group>> callGetGroups = groupApi.getStudentGroups(StudentActivity.id);
        callGetGroups.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.body() != null) {
                    setGroupAdapter(response.body());
                    Log.d("RESPONSE", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        FloatingActionButton addGroupButton = view.findViewById(R.id.invite_button);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_studentGroupsFragment_to_sendApplicationFragment);
            }
        });

        return view;
    }

    public void setGroupAdapter(List<Group> list){
        adapter = new StudentGroupAdapter();
        adapter.setGroups(list);
        recyclerView.setAdapter(adapter);
    }
}