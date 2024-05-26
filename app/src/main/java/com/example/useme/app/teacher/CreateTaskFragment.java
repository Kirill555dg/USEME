package com.example.useme.app.teacher;

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
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.data.model.Task;
import com.example.useme.data.model.taskdata.Category;
import com.example.useme.data.model.taskdata.Subject;
import com.example.useme.data.model.taskdata.Topic;
import com.example.useme.data.model.taskdata.TopicPK;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.TaskApi;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskFragment extends Fragment {

    private TaskApi taskApi;

    private Button saveButton;

    private AutoCompleteTextView subjectACTV;
    private TextInputLayout subjectTIL;
    private AutoCompleteTextView topicACTV;
    private TextInputLayout topicTIL;
    private AutoCompleteTextView categoryACTV;
    private TextInputLayout categoryTIL;

    private EditText conditionET;
    private TextInputLayout conditionTIL;
    private EditText answerET;
    private TextInputLayout answerTIL;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitService retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        subjectACTV = view.findViewById(R.id.form_FieldSubject);
        subjectTIL = view.findViewById(R.id.TIL_Subject);
        topicACTV = view.findViewById(R.id.form_FieldTopic);
        topicTIL = view.findViewById(R.id.TIL_Topic);
        categoryACTV = view.findViewById(R.id.form_FieldCategory);
        categoryTIL = view.findViewById(R.id.TIL_Category);

        conditionET = view.findViewById(R.id.form_FieldCondition);
        conditionTIL = view.findViewById(R.id.TIL_Condition);
        answerET = view.findViewById(R.id.form_FieldAnswer);
        answerTIL = view.findViewById(R.id.TIL_Answer);

        saveButton = view.findViewById(R.id.form_saveButton);
        saveButton.setEnabled(false);

        subjectACTV.setAdapter(getSubjectAdapter());
        setUnselectReaction(topicACTV, "Сначала выберите предмет");
        setUnselectReaction(categoryACTV, "Сначала выберите раздел");

        subjectACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = String.valueOf(subjectACTV.getText());
                subjectTIL.setHelperText(null);
                topicACTV.setText("");
                categoryACTV.setText("");
                topicACTV.setAdapter(getTopicAdapter(selectedSubject));
                categoryACTV.setAdapter(null);
                topicTIL.setHelperText("Необходимо выбрать");
                categoryTIL.setHelperText("Необходимо выбрать");
                enableSaveButtonIfReady();
            }
        });

        topicACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = String.valueOf(subjectACTV.getText());
                Short topicNum = Short.valueOf(String.valueOf(topicACTV.getText()).split(". ")[0]);
                topicTIL.setHelperText(null);
                categoryACTV.setText("");
                categoryACTV.setAdapter(getCategoryAdapter(selectedSubject, topicNum));
                categoryTIL.setHelperText("Необходимо выбрать");
                enableSaveButtonIfReady();
            }
        });

        categoryACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryTIL.setHelperText(null);
                enableSaveButtonIfReady();
            }
        });

        addTextListener(conditionTIL, conditionET);
        addTextListener(answerTIL, answerET);

        return view;
    }

    private void addTextListener(TextInputLayout TIL, EditText ET) {
        ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!ET.getText().toString().isEmpty()) {
                    TIL.setHelperText(null);
                } else {
                    TIL.setHelperText("Нужно заполнить");
                }
                enableSaveButtonIfReady();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSaveButtonIfReady();
            }
        });
    }

    private void enableSaveButtonIfReady() {
        if (!conditionET.getText().toString().isEmpty() &&
            !answerET.getText().toString().isEmpty() &&
            !subjectACTV.getText().toString().isEmpty() &&
            !topicACTV.getText().toString().isEmpty() &&
            !categoryACTV.getText().toString().isEmpty())
        {
            saveButton.setEnabled(true);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subject = String.valueOf(subjectACTV.getText());
                    Short topicNum = Short.valueOf(String.valueOf(topicACTV.getText()).split(". ")[0]);
                    String category = String.valueOf(categoryACTV.getText());
                    String condition = String.valueOf(conditionET.getText());
                    String answer = String.valueOf(answerET.getText());

                    TopicPK topicPK = new TopicPK();
                    topicPK.setSubject(subject);
                    topicPK.setTopicNumber(topicNum);

                    Task task = new Task();
                    task.setTopicPK(topicPK);
                    task.setCategory(category);
                    task.setCondition(condition);
                    task.setAnswer(answer);

                    Call<Task> callSaveTask = taskApi.saveTask(task);
                    callSaveTask.enqueue(new Callback<Task>() {
                        @Override
                        public void onResponse(Call<Task> call, Response<Task> response) {
                            Toast.makeText(getLayoutInflater().getContext(), "Задача успешно создана", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }

                        @Override
                        public void onFailure(Call<Task> call, Throwable t) {
                            Log.d("CALL", t.toString());
                            Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            saveButton.setEnabled(false);
        }
    }

    private ArrayAdapter<String> getCategoryAdapter(String selectedSubject, Short topicNum) {
        ArrayList<String> categories = new ArrayList<>();
        Call<List<Category>> callSubjects = taskApi.getAllCategories(selectedSubject, topicNum);
        callSubjects.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> subjectList = response.body();
                for (Category cat : subjectList) {
                    categories.add(cat.getCategory());
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("CALL", t.toString());
                Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT).show();
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
                for (Topic top : subjectList) {
                    topics.add(top.toString());
                }
            }
            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Log.d("CALL", t.toString());
                Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT).show();
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
            }
            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Log.d("CALL", t.toString());
                Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT).show();
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