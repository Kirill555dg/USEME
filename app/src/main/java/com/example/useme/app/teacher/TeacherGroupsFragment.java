package com.example.useme.app.teacher;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useme.R;
import com.example.useme.adapter.TeacherGroupAdapter;
import com.example.useme.data.model.Group;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherGroupsFragment extends Fragment  {

    private TeacherGroupAdapter adapter;
    private RecyclerView recyclerView;
    private GroupApi groupApi;

    public TeacherGroupsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        FloatingActionButton addGroupButton = view.findViewById(R.id.add_group_button);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_teacherGroupsFragment_to_createGroupFragment);
            }
        });

        recyclerView = view.findViewById(R.id.groups_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Group>> callGetGroups = groupApi.getTeacherGroups(TeacherActivity.id);
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
                Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<List<Group>> callGetGroups = groupApi.getTeacherGroups(TeacherActivity.id);
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
                Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });
    }

    public void setGroupAdapter(List<Group> list){
        adapter = new TeacherGroupAdapter();
        adapter.setGroups(list);
        recyclerView.setAdapter(adapter);
    }
}