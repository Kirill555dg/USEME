package com.example.useme.app.group;

import android.os.AsyncTask;
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
import com.example.useme.adapter.StudentAdapter;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.statistic.Statistic;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;
import com.example.useme.retrofit.api.StatisticApi;
import com.example.useme.retrofit.api.StudentApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupPageFragment extends Fragment {

    private StudentAdapter adapter;
    private RecyclerView recyclerView;
    private StudentApi studentApi;
    private StatisticApi statisticApi;
    private GroupApi groupApi;

    public GroupPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        studentApi = retrofitService.getRetrofit().create(StudentApi.class);
        groupApi = retrofitService.getRetrofit().create(GroupApi.class);
        statisticApi = retrofitService.getRetrofit().create(StatisticApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_page, container, false);

        recyclerView = view.findViewById(R.id.students_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new GroupPageFragment.GetStudentAsyncTask().execute();

        FloatingActionButton applicationsButton = view.findViewById(R.id.application_button);
        applicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_groupPageFragment_to_applicationsFragment);
            }
        });

        return view;
    }

    public void setStudentAdapter(List<Student> list){
        adapter = new StudentAdapter();
        adapter.setStudents(list);
        recyclerView.setAdapter(adapter);
    }

    public class GetStudentAsyncTask extends AsyncTask {
        List<Student> students;
        @Override
        protected Object doInBackground(Object[] objects) {
            Call<List<Student>> callGetGroupStudents = studentApi.getGroupStudents(GroupActivity.id);
            try {
                students = callGetGroupStudents.execute().body();
                Log.d("RESPONSE", students.toString());
                for (int i = 0; i < students.size(); i++) {
                    Student student = students.get(i);
                    student.setCountHomeworks(GroupActivity.countHomeworks);
                    Call<List<Statistic>> callStatistic = statisticApi.findStudentStatisticInGroup(student.getId(), GroupActivity.id);
                    List<Statistic> statistics = callStatistic.execute().body();
                    Log.d("RESPONSE", statistics.toString());
                    HashSet<Long> set = new HashSet<>();
                    for (Statistic statistic : statistics) {
                        set.add(statistic.getPk().getHomework().getId());
                    }
                    student.setCountCompleteHomeworks(set.size());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setStudentAdapter(students);
        }
    }
}