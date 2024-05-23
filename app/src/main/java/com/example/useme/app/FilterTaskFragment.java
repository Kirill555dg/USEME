package com.example.useme.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.useme.R;
import com.example.useme.data.model.Task;
import com.example.useme.data.model.taskdata.Category;
import com.example.useme.data.model.taskdata.Subject;
import com.example.useme.data.model.taskdata.Topic;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.TaskApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterTaskFragment extends DialogFragment {

    private TaskApi taskApi;

    private TextInputEditText idTIET;
    private TextInputLayout idTIL;

    private AutoCompleteTextView subjectACTV;
    private TextInputLayout subjectTIL;
    private AutoCompleteTextView topicACTV;
    private TextInputLayout topicTIL;
    private AutoCompleteTextView categoryACTV;
    private TextInputLayout categoryTIL;

    private Button filterButton;
    private Button resetButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_filter_task, container, false);

        idTIET = view.findViewById(R.id.filter_TIET_id);
        idTIL = view.findViewById(R.id.filter_TIL_id);

        subjectACTV = view.findViewById(R.id.filter_ACTV_subject);
        subjectTIL = view.findViewById(R.id.filter_TIL_subject);
        topicACTV = view.findViewById(R.id.filter_ACTV_topic);
        topicTIL = view.findViewById(R.id.filter_TIL_topic);
        categoryACTV = view.findViewById(R.id.filter_ACTV_category);
        categoryTIL = view.findViewById(R.id.filter_TIL_category);

        resetButton = view.findViewById(R.id.reset_button);
        filterButton = view.findViewById(R.id.filter_button);

        RetrofitService retrofitService = new RetrofitService();
        taskApi = retrofitService.getRetrofit().create(TaskApi.class);

        idTIET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                activateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                activateButton();
            }
        });

        subjectACTV.setAdapter(getSubjectAdapter());
        setUnselectReaction(topicACTV, "Сначала выберите предмет");
        setUnselectReaction(categoryACTV, "Сначала выберите раздел");

        subjectACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = String.valueOf(subjectACTV.getText());
                topicACTV.setAdapter(getTopicAdapter(selectedSubject));
                activateButton();
            }
        });

        topicACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = String.valueOf(subjectACTV.getText());
                Short topicNum = Short.valueOf(String.valueOf(topicACTV.getText()).split(". ")[0]);
                categoryACTV.setAdapter(getCategoryAdapter(selectedSubject, topicNum));
                activateButton();
            }
        });

        categoryACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activateButton();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<Task>> callGetTasks = taskApi.getAllTasks();
                callGetTasks.enqueue(new Callback<List<Task>>() {
                    @Override
                    public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("TASKS", (Serializable) response.body());
                        intent.putExtra("BUNDLE", bundle);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        getDialog().dismiss();
                    }

                    @Override
                    public void onFailure(Call<List<Task>> call, Throwable t) {
                        Toast.makeText(getLayoutInflater().getContext(), t.toString(), Toast.LENGTH_LONG).show();
                        Log.d("FAIL", t.toString());
                    }
                });
            }
        });


        return view;
    }


    private boolean isIDCorrect() {
        String id = idTIET.getText().toString();
        if (!id.matches("[0-9]+")) {
            if (!id.isEmpty()) {
                idTIL.setHelperText("ID может состоять только из цифр");
            } else {
                idTIL.setHelperText(null);
            }
            return false;
        } else {
            idTIL.setHelperText(null);
            return true;
        }
    }

    private void activateButton(){
        if (
                isIDCorrect()
                &&
                (!subjectACTV.getText().toString().isEmpty()) ||
                !topicACTV.getText().toString().isEmpty() ||
                !categoryACTV.getText().toString().isEmpty()
        )
        {
            filterButton.setEnabled(false);
        }
        else if (!filterButton.isEnabled()) {
            filterButton.setEnabled(true);
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
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

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics newDisplayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(newDisplayMetrics);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.getWindow().setLayout(newDisplayMetrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}

