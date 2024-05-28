package com.example.useme.app.group;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.adapter.TaskMiniAdapter;
import com.example.useme.data.model.Task;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreateHomeworkFragment extends Fragment {

    private TaskMiniAdapter adapter;
    private RecyclerView recyclerView;
    private MaterialButton pickDeadlineButton;
    private LocalDate deadline;
    private static final String KEY_TASKS = "TASKS";
    private List<Task> takedTasks;

    public CreateHomeworkFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            takedTasks = (List<Task>) getArguments().getSerializable(KEY_TASKS);
        } else {
            takedTasks = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_homework, container, false);
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        pickDeadlineButton = view.findViewById(R.id.pickDeadlineButton);
        pickDeadlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeadlinePickerDialog();
            }
        });

        Button addTasksButton = view.findViewById(R.id.add_tasks_button);
        addTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_TASKS, (Serializable) takedTasks);
                Navigation.findNavController(view).navigate(R.id.action_createHomeworkFragment_to_addTaskFragment, bundle);
            }
        });

        recyclerView = view.findViewById(R.id.task_mini_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setTaskAdapter(takedTasks);

        return view;
    }

    private void openDeadlinePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                deadline = LocalDate.of(year, month+1, dayOfMonth);
                pickDeadlineButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                pickDeadlineButton.setStrokeColorResource(R.color.colorPrimary);
                pickDeadlineButton.setText("Указать дедлайн");
                Toast.makeText(getContext(), "Выбранный дедлайн: " + deadline.toString(), Toast.LENGTH_LONG).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Выберите дедлайн");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "сохранить", dialog);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "выйти", dialog);
        dialog.show();
    }

    private void setTaskAdapter(List<Task> tasks) {
        adapter = new TaskMiniAdapter(getContext());
        adapter.setTasks(tasks);
        recyclerView.setAdapter(adapter);
    }
}