package com.example.useme.app.group;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.useme.R;
import com.example.useme.adapter.TaskMiniAdapter;
import com.example.useme.app.student.SolveHomeworkFragment;
import com.example.useme.data.model.Group;
import com.example.useme.data.model.Homework;
import com.example.useme.data.model.Task;
import com.example.useme.retrofit.RetrofitService;
import com.example.useme.retrofit.api.HomeworkApi;
import com.example.useme.tool.RecyclerTouchListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateHomeworkFragment extends Fragment {

    private TaskMiniAdapter adapter;
    private RecyclerView recyclerView;
    private HomeworkApi homeworkApi;

    private TextInputEditText homeworkTitleTIET;
    private TextInputLayout homeworkTitleTIL;
    private Button sendButton;
    private MaterialButton pickDeadlineButton;
    private String title;
    private String deadline;
    private static final String KEY_TASKS = "TASKS";
    private List<Task> takedTasks;
    private Long id;
    private boolean first;

    public CreateHomeworkFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        takedTasks = new ArrayList<>();
        id = -1L;
        title = "";
        deadline = "";
        first = true;

        RetrofitService retrofitService = new RetrofitService();
        homeworkApi = retrofitService.getRetrofit().create(HomeworkApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (id == -1L) id = getArguments().getLong("ID");
        }
        View view = inflater.inflate(R.layout.fragment_add_homework, container, false);
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        pickDeadlineButton = view.findViewById(R.id.pickDeadlineButton);
        if (!deadline.isEmpty()) {
            pickDeadlineButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            pickDeadlineButton.setStrokeColorResource(R.color.colorPrimary);
            pickDeadlineButton.setText(R.string.change_deadline);
        }

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

        sendButton = view.findViewById(R.id.send_homework_button);

        homeworkTitleTIET = view.findViewById(R.id.TIET_HomeworkTitle);
        homeworkTitleTIL = view.findViewById(R.id.TIL_HomeworkTitle);
        if (!title.isEmpty()) {
            homeworkTitleTIET.setText(title);
        }
        addTextListener(homeworkTitleTIL, homeworkTitleTIET);

        if (id != -1L) {
            Call<Homework> callGetHomework = homeworkApi.findHomework(id);
            if (first) {
                first = false;

                callGetHomework.enqueue(new Callback<Homework>() {
                    @Override
                    public void onResponse(Call<Homework> call, Response<Homework> response) {
                        Homework homework = response.body();
                        deadline = homework.getDeadline();
                        title = homework.getTitle();
                        takedTasks = homework.getTasks();
                        sendButton.setText(R.string.change_homework);
                        pickDeadlineButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        pickDeadlineButton.setStrokeColorResource(R.color.colorPrimary);
                        pickDeadlineButton.setText(R.string.change_deadline);
                        homeworkTitleTIET.setText(title);
                        homeworkTitleTIL.setHelperText(null);
                        setTaskAdapter(takedTasks);
                    }

                    @Override
                    public void onFailure(Call<Homework> call, Throwable t) {
                        Log.d("CALL", t.toString());
                        Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                sendButton.setText(R.string.change_homework);
                pickDeadlineButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                pickDeadlineButton.setStrokeColorResource(R.color.colorPrimary);
                pickDeadlineButton.setText(R.string.change_deadline);
                homeworkTitleTIET.setText(title);
                homeworkTitleTIL.setHelperText(null);
                setTaskAdapter(takedTasks);
            }
        } else {
            setTaskAdapter(takedTasks);
        }
        return view;
    }

    private void addTextListener(TextInputLayout TIL, TextInputEditText TIET) {
        TIET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TIET.getText().toString().isEmpty()) {
                    TIL.setHelperText(null);
                } else {
                    TIL.setHelperText("Нужно заполнить");
                }
                enableSendButtonIfReady();
            }

            @Override
            public void afterTextChanged(Editable s) {
                title = TIET.getText().toString();
                enableSendButtonIfReady();
            }
        });

    }

    private void enableSendButtonIfReady() {
        if (!title.isEmpty() && !deadline.isEmpty() && !takedTasks.isEmpty()) {
            sendButton.setEnabled(true);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (takedTasks.isEmpty()) {
                        Toast.makeText(getContext(), "Нужно добавить задачи", Toast.LENGTH_SHORT).show();
                        sendButton.setEnabled(false);
                        return;
                    }
                    Homework homework = new Homework();
                    homework.setGroup(new Group(GroupActivity.id));
                    homework.setTasks(takedTasks);
                    homework.setTitle(title);
                    homework.setDeadline(deadline);
                    homework.setDateOfIssue(LocalDate.now().toString());

                    if (id == -1L) {
                        Call<Homework> callCreateHomework = homeworkApi.createHomework(homework);
                        callCreateHomework.enqueue(new Callback<Homework>() {
                            @Override
                            public void onResponse(Call<Homework> call, Response<Homework> response) {
                                Log.d("RESPONSE", response.body().toString());
                                Toast.makeText(getLayoutInflater().getContext(), "Домашнее задание успешно создано", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }

                            @Override
                            public void onFailure(Call<Homework> call, Throwable t) {
                                Log.d("CALL", t.toString());
                                Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Call<Homework> callCreateHomework = homeworkApi.updateHomework(id, homework);
                        callCreateHomework.enqueue(new Callback<Homework>() {
                            @Override
                            public void onResponse(Call<Homework> call, Response<Homework> response) {
                                Log.d("RESPONSE", response.body().toString());
                                Toast.makeText(getLayoutInflater().getContext(), "Домашнее задание успешно изменено", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }

                            @Override
                            public void onFailure(Call<Homework> call, Throwable t) {
                                Log.d("CALL", t.toString());
                                Toast.makeText(getLayoutInflater().getContext(), "Ошибка соединения", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            sendButton.setEnabled(false);
        }
    }

    private void openDeadlinePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                deadline = LocalDate.of(year, month + 1, dayOfMonth).toString();
                pickDeadlineButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                pickDeadlineButton.setStrokeColorResource(R.color.colorPrimary);
                pickDeadlineButton.setText("Указать дедлайн");
                Toast.makeText(getContext(), "Выбранный дедлайн: " + deadline, Toast.LENGTH_LONG).show();
                enableSendButtonIfReady();
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
        adapter = new TaskMiniAdapter(getContext(), this);
        adapter.setTasks(tasks);
        recyclerView.setAdapter(adapter);
        enableSendButtonIfReady();
    }
}