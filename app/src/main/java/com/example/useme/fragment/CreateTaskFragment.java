package com.example.useme.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.useme.MainActivity;
import com.example.useme.R;
import com.example.useme.model.Task;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.TaskApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        EditText subjectET = view.findViewById(R.id.form_textFieldSubject);
        EditText topicET = view.findViewById(R.id.form_textFieldTopic);
        EditText categoryET = view.findViewById(R.id.form_textFieldCategory);
        EditText conditionET = view.findViewById(R.id.form_textFieldCondition);
        EditText answerET = view.findViewById(R.id.form_textFieldAnswer);
        Button saveButton = view.findViewById(R.id.form_saveButton);

        RetrofitService retrofitService = new RetrofitService();
        TaskApi taskApi = retrofitService.getRetrofit().create(TaskApi.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = String.valueOf(subjectET.getText());
                String topic = String.valueOf(topicET.getText());
                String category = String.valueOf(categoryET.getText());
                String condition = String.valueOf(conditionET.getText());
                String answer = String.valueOf(answerET.getText());

                Task task = new Task();
                task.setSubject(subject);
                task.setTopic(topic);
                task.setCategory(category);
                task.setCondition(condition);
                task.setAnswer(answer);

                Call<Task> call = taskApi.saveTask(task);
                call.enqueue(new Callback<Task>() {
                    @Override
                    public void onResponse(Call<Task> call, Response<Task> response) {
                        Log.d("SUCCESS", response.toString());
                        Toast.makeText(inflater.getContext(), "Save success!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Task> call, Throwable t) {
                        Log.d("FAIL", t.toString());
                        Toast.makeText(inflater.getContext(), "Save failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }


}