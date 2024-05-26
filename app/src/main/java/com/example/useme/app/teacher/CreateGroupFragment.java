package com.example.useme.app.teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Task;
import com.example.useme.data.model.Teacher;
import com.example.useme.data.model.taskdata.Subject;
import com.example.useme.data.model.taskdata.TopicPK;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateGroupFragment extends Fragment {

    private GroupApi groupApi;


    private TextInputEditText groupNameTIET;
    private TextInputLayout groupNameTIL;
    private AutoCompleteTextView targetSubjectACTV;
    private TextInputEditText descriptionTIET;

    private Button createButton;

    public CreateGroupFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupNameTIL = view.findViewById(R.id.TIL_GroupName);
        groupNameTIET = view.findViewById(R.id.TIET_GroupName);

        targetSubjectACTV = view.findViewById(R.id.ACTV_TargetSubject);
        descriptionTIET = view.findViewById(R.id.TIET_Description);

        createButton = view.findViewById(R.id.create_group_button);
        createButton.setEnabled(false);

        targetSubjectACTV.setAdapter(getTargetSubjectAdapter());

        addTextListener(groupNameTIL, groupNameTIET);

        return view;
    }

    private void addTextListener(TextInputLayout TIL, TextInputEditText ET) {
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
                enableCreateButtonIfReady();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableCreateButtonIfReady();
            }
        });
    }

    private void enableCreateButtonIfReady() {
        if (!groupNameTIET.getText().toString().isEmpty())
        {
            createButton.setEnabled(true);
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String groupName = groupNameTIET.getText().toString();
                    String targetSubject = targetSubjectACTV.getText().toString();
                    String description = descriptionTIET.getText().toString();

                    Group group = new Group();
                    group.setName(groupName);
                    group.setTargetSubject(targetSubject);
                    group.setDescription(description);
                    group.setTeacher(new Teacher(TeacherActivity.id));

                    Call<Group> callCreateGroup = groupApi.createGroup(group);
                    callCreateGroup.enqueue(new Callback<Group>() {
                        @Override
                        public void onResponse(Call<Group> call, Response<Group> response) {
                            Toast.makeText(getLayoutInflater().getContext(), "Группа успешно создана", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }

                        @Override
                        public void onFailure(Call<Group> call, Throwable t) {
                            Log.d("CALL", t.toString());
                            Toast.makeText(getLayoutInflater().getContext(), "Произошла непредвиденная ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            createButton.setEnabled(false);
        }
    }

    private ArrayAdapter<String> getTargetSubjectAdapter() {
        ArrayList<String> subjects = new ArrayList<>();
        Call<List<Subject>> callSubjects = groupApi.getTargetSubjects();
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
}