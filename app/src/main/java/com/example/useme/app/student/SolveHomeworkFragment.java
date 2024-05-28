package com.example.useme.app.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.useme.adapter.NumTaskAdapter;
import com.example.useme.app.group.GroupActivity;
import com.example.useme.data.model.Homework;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.Task;
import com.example.useme.data.model.statistic.LocalStatistic;
import com.example.useme.data.model.statistic.Statistic;
import com.example.useme.data.model.statistic.StatisticPK;
import com.example.useme.data.repository.LocalStatisticRepository;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.HomeworkApi;
import com.example.useme.retrofit.api.StatisticApi;
import com.example.useme.tool.RecyclerTouchListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolveHomeworkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_GROUP_ID = "ID";
    private static final String KEY_COMPLETE = "COMPLETE";

    public static Long id;
    private Boolean isCompleted;

    private NumTaskAdapter adapter;
    private RecyclerView recyclerView;
    private HomeworkApi homeworkApi;
    private StatisticApi statisticApi;
    private LocalStatisticRepository repository;
    private List<Task> tasks;

    public SolveHomeworkFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(KEY_GROUP_ID);
            isCompleted = getArguments().getBoolean(KEY_COMPLETE);
            Log.d(KEY_COMPLETE, String.valueOf(isCompleted));
        }
        repository = new LocalStatisticRepository(getActivity().getApplication().getApplicationContext());

        //new GetCompletedTasksAsyncTask().execute();

        RetrofitService retrofitService = new RetrofitService();
        homeworkApi = retrofitService.getRetrofit().create(HomeworkApi.class);
        statisticApi = retrofitService.getRetrofit().create(StatisticApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solve, container, false);

        recyclerView = view.findViewById(R.id.mini_task_recycler_view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Call<List<Task>> callGetHomeworkTasks = homeworkApi.getHomeworkTasks(id);

        callGetHomeworkTasks.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                tasks = response.body();
                Log.d("RESPONSE", tasks.toString());
                setMiniTasksAdapter(tasks);
                Task task = adapter.getTask(0);
                Bundle bundle = new Bundle();
                bundle.putLong("TaskID", task.getId());
                bundle.putString("TaskCondition", task.getCondition());
                bundle.putString("TaskSubject", task.getTopicPK().getSubject());
                bundle.putString("TaskTopic", "Задача №" + task.getTopicPK().getTopicNumber());
                bundle.putString("TaskCategory", task.getCategory());
                bundle.putString("TaskAnswer", task.getAnswer());
                bundle.putBoolean(KEY_COMPLETE, isCompleted);
                Log.d(KEY_COMPLETE, String.valueOf(isCompleted));
                SolveTaskFragment solveTaskFragment = new SolveTaskFragment();
                solveTaskFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.solveTaskFragment, solveTaskFragment).commit();
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                Log.d("FAIL", t.toString());
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                adapter.setChosenTask(position);
                Task task = adapter.getTask(position);
                Bundle bundle = new Bundle();
                bundle.putLong("TaskID", task.getId());
                bundle.putString("TaskCondition", task.getCondition());
                bundle.putString("TaskSubject", task.getTopicPK().getSubject());
                bundle.putString("TaskTopic", "Задача №" + task.getTopicPK().getTopicNumber());
                bundle.putString("TaskCategory", task.getCategory());
                bundle.putString("TaskAnswer", task.getAnswer());
                bundle.putBoolean(KEY_COMPLETE, isCompleted);
                Log.d(KEY_COMPLETE, String.valueOf(isCompleted));
                SolveTaskFragment solveTaskFragment = new SolveTaskFragment();
                solveTaskFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.solveTaskFragment, solveTaskFragment).commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Button sendButton = view.findViewById(R.id.send_solve_button);
        if (isCompleted) {
            sendButton.setEnabled(false);
        } else {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getContext())
                            .setMessage("Вы точно готовы отправить Д/З на проверку?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<LocalStatistic> statistics = repository.findHomeworkStatistics(StudentActivity.id, SolveHomeworkFragment.id);
                                            List<Statistic> sendStatistics = new ArrayList<>();
                                            List<Long> completedTaskIds = new ArrayList<>();
                                            for (LocalStatistic statistic : statistics) {
                                                Statistic sendStatistic = new Statistic();

                                                StatisticPK pk = new StatisticPK();

                                                Student student = new Student();
                                                student.setId(StudentActivity.id);
                                                pk.setStudent(student);

                                                Homework homework = new Homework();
                                                homework.setId(SolveHomeworkFragment.id);
                                                pk.setHomework(homework);

                                                Task task = new Task();
                                                task.setId(statistic.getTaskId());
                                                pk.setTask(task);

                                                completedTaskIds.add(statistic.getTaskId());

                                                sendStatistic.setPk(pk);
                                                sendStatistic.setIsCorrect(statistic.getCorrect());

                                                sendStatistics.add(sendStatistic);
                                            }
                                            for (Task task : tasks) {
                                                if (completedTaskIds.indexOf(task.getId()) == -1) {
                                                    Statistic sendStatistic = new Statistic();

                                                    StatisticPK pk = new StatisticPK();

                                                    Student student = new Student();
                                                    student.setId(StudentActivity.id);
                                                    pk.setStudent(student);

                                                    Homework homework = new Homework();
                                                    homework.setId(SolveHomeworkFragment.id);
                                                    pk.setHomework(homework);

                                                    pk.setTask(task);

                                                    sendStatistic.setPk(pk);
                                                    sendStatistic.setIsCorrect(false);

                                                    sendStatistics.add(sendStatistic);
                                                }
                                            }
                                            Log.d("STATISTICS", sendStatistics.toString());

                                            Call<List<Statistic>> callSaveStatistic = statisticApi.createHomeworkStatistic(sendStatistics);
                                            callSaveStatistic.enqueue(new Callback<List<Statistic>>() {
                                                @Override
                                                public void onResponse(Call<List<Statistic>> call, Response<List<Statistic>> response) {
                                                    Toast.makeText(getLayoutInflater().getContext(), "Домашнее задание проверено", Toast.LENGTH_SHORT).show();
                                                    Log.d("RESPONSE", response.body().toString());
                                                    getActivity().getSupportFragmentManager().popBackStack();
                                                }

                                                @Override
                                                public void onFailure(Call<List<Statistic>> call, Throwable t) {
                                                    Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                                                    Log.d("FAIL", t.toString());
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            })
                            .setNegativeButton("Нет", null)
                            .show();
                }
            });
        }

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    public void setMiniTasksAdapter(List<Task> list) {
        adapter = new NumTaskAdapter();
        adapter.setMiniTasks(list);
        recyclerView.setAdapter(adapter);
    }

    /*public class GetCompletedTasksAsyncTask extends AsyncTask {
        List<Student> students;
        @Override
        protected Object doInBackground(Object[] objects) {
            List<LocalStatistic> statistics = repository.findHomeworkStatistics(StudentActivity.id, GroupActivity.id);
            completedTasksIds = new ArrayList<>();
            for (LocalStatistic statistic : statistics) {
                completedTasksIds.add(statistic.getTaskId());
            }
            Call<List<Task>> callGetHomeworkTasks = homeworkApi.getHomeworkTasks(id);
            callGetHomeworkTasks.enqueue(new Callback<List<Task>>() {
                @Override
                public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                    tasks = response.body();
                    Log.d("RESPONSE", tasks.toString());
                    setMiniTasksAdapter(tasks, completedTasksIds);
                    Task task = adapter.getTask(0);
                    Bundle bundle = new Bundle();
                    bundle.putLong("TaskID", task.getId());
                    bundle.putString("TaskCondition", task.getCondition());
                    bundle.putString("TaskSubject", task.getTopicPK().getSubject());
                    bundle.putString("TaskTopic", "Задача №" + task.getTopicPK().getTopicNumber());
                    bundle.putString("TaskCategory", task.getCategory());
                    bundle.putString("TaskAnswer", task.getAnswer());
                    bundle.putBoolean(KEY_HOMEWORK_COMPLETE, isHomeworkCompleted);
                    Log.d("DEBUG", String.valueOf(isHomeworkCompleted));
                    bundle.putBoolean("TaskComplete", completedTasksIds.contains(task.getId()));
                    SolveTaskFragment solveTaskFragment = new SolveTaskFragment();
                    solveTaskFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.solveTaskFragment, solveTaskFragment).commit();
                }

                @Override
                public void onFailure(Call<List<Task>> call, Throwable t) {
                    Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                    Log.d("FAIL", t.toString());
                }
            });
            return null;
        }

        *//*@Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }*//*
    }*/
}