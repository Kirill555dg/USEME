package com.example.useme.app.group;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.app.MainActivity;
import com.example.useme.app.teacher.TeacherActivity;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Teacher;
import com.example.useme.data.model.taskdata.Subject;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.GroupApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupProfileFragment extends Fragment {

    private GroupApi groupApi;

    private Button deleteButton;
    private Button changeInfoButton;
    private Button saveInfoButton;

    private TextView nameTV;
    private TextInputEditText nameTIET;
    private TextView targetSubjectTV;
    private TextInputLayout targetSubjectTIL;
    private AutoCompleteTextView targetSubjectACTV;
    private TextView descriptionTV;
    private TextInputEditText descriptionTIET;
    private TextView passwordTV;
    private TextInputEditText passwordTIET;
    public GroupProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_group_profile, container, false);

        nameTV = view.findViewById(R.id.TV_ProfileGroupName);
        nameTIET = view.findViewById(R.id.TIET_ProfileGroupName);

        descriptionTV = view.findViewById(R.id.TV_ProfileGroupDescription);
        descriptionTIET = view.findViewById(R.id.TIET_ProfileGroupDescription);

        targetSubjectTV = view.findViewById(R.id.ACTV_ProfileGroupTargetSubject);
        targetSubjectTIL = view.findViewById(R.id.TIL_ProfileGroupTargetSubject);
        targetSubjectACTV = view.findViewById(R.id.ACTV_ProfileGroupTargetSubject);

        passwordTV = view.findViewById(R.id.TV_ProfileGroupPassword);
        passwordTIET = view.findViewById(R.id.TIET_ProfileGroupPassword);

        changeInfoButton = view.findViewById(R.id.Button_GroupChangeInfo);
        saveInfoButton = view.findViewById(R.id.Button_GroupSaveInfo);
        deleteButton = view.findViewById(R.id.Button_GroupDelete);
        changeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTV.setVisibility(View.GONE);
                nameTIET.setVisibility(View.VISIBLE);

                descriptionTV.setVisibility(View.GONE);
                descriptionTIET.setVisibility(View.VISIBLE);

                targetSubjectTV.setVisibility(View.GONE);
                targetSubjectTIL.setVisibility(View.VISIBLE);
                targetSubjectACTV.setVisibility(View.VISIBLE);

                passwordTV.setVisibility(View.GONE);
                passwordTIET.setVisibility(View.VISIBLE);

                changeInfoButton.setVisibility(View.GONE);
                saveInfoButton.setVisibility(View.VISIBLE);
            }
        });

        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTIET.getText().toString();
                String targetSubject = targetSubjectACTV.getText().toString();
                String description = descriptionTIET.getText().toString();
                String password = passwordTIET.getText().toString();

                Group group = new Group();
                group.setName(name);
                group.setTargetSubject(targetSubject);
                group.setDescription(description);
                group.setPassword(password);

                Call<Group> callUpdateGroup = groupApi.updateGroup(GroupActivity.id, group);
                callUpdateGroup.enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        Group newGroup = response.body();

                        GroupActivity.name = newGroup.getName();
                        ((TextView)(getActivity().findViewById(R.id.group_title))).setText(newGroup.getName());
                        GroupActivity.targetSubject = newGroup.getTargetSubject();
                        GroupActivity.description = newGroup.getDescription();
                        GroupActivity.password = newGroup.getPassword();
                        Toast.makeText(getLayoutInflater().getContext(), "Информация изменена", Toast.LENGTH_LONG).show();

                        onUpdateView();
                        nameTV.setVisibility(View.VISIBLE);
                        nameTIET.setVisibility(View.GONE);

                        descriptionTV.setVisibility(View.VISIBLE);
                        descriptionTIET.setVisibility(View.GONE);

                        targetSubjectTV.setVisibility(View.VISIBLE);
                        targetSubjectTIL.setVisibility(View.GONE);
                        targetSubjectACTV.setVisibility(View.GONE);

                        passwordTV.setVisibility(View.VISIBLE);
                        passwordTIET.setVisibility(View.GONE);

                        changeInfoButton.setVisibility(View.VISIBLE);
                        saveInfoButton.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {
                        Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
                        Log.d("FAIL", t.toString());
                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Удаление группы")
                        .setMessage("Вы точно хотите удалить данную группу?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<Void> callDelete = groupApi.deleteGroup(GroupActivity.id);
                                callDelete.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(getLayoutInflater().getContext(), "Группа удалена", Toast.LENGTH_LONG).show();
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
                                        Log.d("FAIL", t.toString());
                                    }
                                });
                            }

                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }
        });

        onUpdateView();
        return view;
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
                Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
            }
        });
        return new ArrayAdapter<>(requireContext(), R.layout.drop_down_item, subjects);
    }

    private void onUpdateView() {
        nameTV.setText(GroupActivity.name);
        nameTIET.setText(GroupActivity.name);

        descriptionTV.setText(GroupActivity.description);
        descriptionTIET.setText(GroupActivity.description);

        targetSubjectTV.setText(GroupActivity.targetSubject);
        targetSubjectACTV.setText(GroupActivity.targetSubject);
        targetSubjectACTV.setAdapter(getTargetSubjectAdapter());

        passwordTV.setText(GroupActivity.password);
        passwordTIET.setText(GroupActivity.password);
    }
}