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

    private Integer countHomeworks;

    public GroupPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        studentApi = retrofitService.getRetrofit().create(StudentApi.class);
        groupApi = retrofitService.getRetrofit().create(GroupApi.class);
        statisticApi = retrofitService.getRetrofit().create(StatisticApi.class);

        Call<Group> getGroup = groupApi.findGroup(GroupActivity.id);
        getGroup.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                Group group = response.body();
                Log.d("RESPONSE", group.toString());
                countHomeworks = group.getCountHomeworks();
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_page, container, false);

        recyclerView = view.findViewById(R.id.students_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new GetStudentAsyncTask().execute();

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
                boolean isDone = false;
                for (int i = 0; i < students.size(); i++) {
                    Student student = students.get(i);
                    student.setCountHomeworks(countHomeworks);
                    Call<List<Statistic>> callStatistic = statisticApi.findStudentStatisticInGroup(student.getId(), GroupActivity.id);
                    List<Statistic> statistics = callStatistic.execute().body();
                    Log.d("RESPONSE", statistics.toString());
                    HashSet<Long> set = new HashSet<>();
                    for (Statistic statistic : statistics) {
                        set.add(statistic.getPk().getHomeworkId());
                    }
                    Log.d("DEBUG", String.valueOf(set.size()));
                    student.setCountCompleteHomeworks(set.size());
                    Log.d("DEBUG", student.toString());
                }
                Log.d("DEBUG", students.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("DEBUG", students.toString());
            setStudentAdapter(students);
        }
    }

}