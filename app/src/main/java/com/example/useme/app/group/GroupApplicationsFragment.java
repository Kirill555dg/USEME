package com.example.useme.app.group;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.adapter.ApplicationAdapter;
import com.example.useme.adapter.MiniTaskAdapter;
import com.example.useme.adapter.StudentAdapter;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.statistic.Statistic;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.ApplicationApi;
import com.example.useme.tool.RecyclerTouchListener;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupApplicationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private ApplicationApi applicationApi;

    public GroupApplicationsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        applicationApi = retrofitService.getRetrofit().create(ApplicationApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_applications, container, false);

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        recyclerView = view.findViewById(R.id.applications_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Student>> callGetApplications = applicationApi.getApplications(GroupActivity.id);
        callGetApplications.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                Log.d("RESPONSE", response.body().toString());
                setApplicationAdapter(response.body());
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT);
                Log.d("FAIL", t.toString());
            }
        });

        return view;
    }

    public void setApplicationAdapter(List<Student> list){
        adapter = new ApplicationAdapter(getContext());
        adapter.setApplications(list);
        recyclerView.setAdapter(adapter);
    }
}