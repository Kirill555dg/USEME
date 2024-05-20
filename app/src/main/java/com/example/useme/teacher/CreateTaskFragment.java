package com.example.useme.teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.model.Task;
import com.example.useme.model.data.Category;
import com.example.useme.model.data.Subject;
import com.example.useme.model.data.Topic;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.TaskApi;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TaskApi taskApi;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Button saveButton;

    private AutoCompleteTextView subjectACTV;
    private AutoCompleteTextView topicACTV;
    private AutoCompleteTextView categoryACTV;

    private EditText conditionET;
    private EditText answerET;

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

        subjectACTV = view.findViewById(R.id.form_FieldSubject);
        topicACTV = view.findViewById(R.id.form_FieldTopic);
        categoryACTV = view.findViewById(R.id.form_FieldCategory);

        conditionET = view.findViewById(R.id.form_FieldCondition);
        answerET = view.findViewById(R.id.form_FieldAnswer);

        saveButton = view.findViewById(R.id.form_saveButton);
        saveButton.setEnabled(false);

        RetrofitService retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);

        subjectACTV.setAdapter(getSubjectAdapter());
        setUnselectReaction(topicACTV, "Сначала выберите предмет");
        setUnselectReaction(categoryACTV, "Сначала выберите раздел");

        subjectACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = String.valueOf(subjectACTV.getText());
                topicACTV.setText(null);
                categoryACTV.setText(null);
                topicACTV.setAdapter(getTopicAdapter(selectedSubject));
                categoryACTV.setAdapter(null);
            }
        });

        topicACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = String.valueOf(subjectACTV.getText());
                Short topicNum = Short.valueOf(String.valueOf(topicACTV.getText()).split(". ")[0]);
                categoryACTV.setText(null);
                categoryACTV.setAdapter(getCategoryAdapter(selectedSubject, topicNum));
            }
        });

        addTextListener(conditionET);
        addTextListener(answerET);

        return view;
    }

    private void addTextListener(EditText ET) {
        ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSaveButtonIfReady();
            }
        });
    }

    private void enableSaveButtonIfReady() {

        saveButton.setEnabled(true);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = String.valueOf(subjectACTV.getText());
                String topic = String.valueOf(topicACTV.getText());
                String category = String.valueOf(categoryACTV.getText());
                String condition = String.valueOf(conditionET.getText());
                String answer = String.valueOf(answerET.getText());

                Task task = new Task();
                task.setSubject(subject);
                task.setTopic(topic);
                task.setCategory(category);
                task.setCondition(condition);
                task.setAnswer(answer);

                Call<Task> callSaveTask = taskApi.saveTask(task);
                callSaveTask.enqueue(new Callback<Task>() {
                    @Override
                    public void onResponse(Call<Task> call, Response<Task> response) {
                        Log.d("CALL", "Task saved successfully");
                        Toast.makeText(getLayoutInflater().getContext(), "Task saved successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Task> call, Throwable t) {
                        Log.d("CALL", t.toString());
                        Toast.makeText(getLayoutInflater().getContext(), "Task save FAILED", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private ArrayAdapter<String> getCategoryAdapter(String selectedSubject, Short topicNum) {
        ArrayList<String> categories = new ArrayList<>();
        Call<List<Category>> callSubjects = taskApi.getAllCategories(selectedSubject, topicNum);
        callSubjects.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> subjectList = response.body();
                Log.d("RESPONSE", response.body().toString());
                for (Category cat : subjectList) {
                    Log.d("RESPONSE_CAT", cat.toString());
                    categories.add(cat.getCategory());
                }
                Log.d("CALL", "Categories loaded successfully");
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("CALL", t.toString());
            }
        });
        return new ArrayAdapter<>(requireContext(), R.layout.drop_down_item, categories);
    }

    private ArrayAdapter<String> getTopicAdapter(String selectedSubject) {
        ArrayList<String> topics = new ArrayList<>();
        Call<List<Topic>> callSubjects = taskApi.getAllTopics(selectedSubject);
        callSubjects.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                List<Topic> subjectList = response.body();
                Log.d("RESPONSE", response.body().toString());
                for (Topic top : subjectList) {
                    Log.d("RESPONSE_TOP", top.toString());
                    topics.add(top.toString());
                }
                Log.d("CALL", "Topics loaded successfully");
            }
            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Log.d("CALL", t.toString());
            }
        });
        return new ArrayAdapter<>(requireContext(), R.layout.drop_down_item, topics);
    }

    private ArrayAdapter<String> getSubjectAdapter() {
        ArrayList<String> subjects = new ArrayList<>();
        Call<List<Subject>> callSubjects = taskApi.getAllSubjects();
        callSubjects.enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                List<Subject> subjectList = response.body();
                for (Subject subj : subjectList) {
                    subjects.add(subj.getSubject());
                }
                Log.d("CALL", "Subjects loaded successfully");
            }
            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Log.d("CALL", t.toString());
            }
        });
        return new ArrayAdapter<>(requireContext(), R.layout.drop_down_item, subjects);
    }

    private void setUnselectReaction(AutoCompleteTextView ACTV, String toastText) {
        ACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ACTV.getAdapter() == null) {
                    Toast.makeText(getLayoutInflater().getContext(), toastText, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}