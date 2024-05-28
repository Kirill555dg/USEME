package com.example.useme.app.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.useme.R;
import com.example.useme.data.model.statistic.LocalStatistic;
import com.example.useme.data.repository.LocalStatisticRepository;
import com.google.android.material.textfield.TextInputEditText;

public class SolveTaskFragment extends Fragment {

    private static final String KEY_ID = "TaskID";
    private static final String KEY_CONDITION = "TaskCondition";
    private static final String KEY_SUBJECT = "TaskSubject";
    private static final String KEY_TOPIC = "TaskTopic";
    private static final String KEY_CATEGORY = "TaskCategory";
    private static final String KEY_ANSWER = "TaskAnswer";
    private static final String KEY_COMPLETE = "COMPLETE";

    private Boolean isCompleted = true;
    private Long id;
    private String subject;
    private String topic;
    private String category;
    private String condition;
    private String answer;
    private String localAnswer;

    private TextView topicTV;
    private TextView idTV;
    private TextView subjectTV;
    private TextView categoryTV;
    private TextView conditionTV;
    private TextInputEditText answerTIET;

    private Button saveButton;


    public LocalStatisticRepository repository;

    public SolveTaskFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new LocalStatisticRepository(getActivity().getApplication().getApplicationContext());
        if (getArguments() != null) {
            id = getArguments().getLong(KEY_ID);
            subject = getArguments().getString(KEY_SUBJECT);
            topic = getArguments().getString(KEY_TOPIC);
            category = getArguments().getString(KEY_CATEGORY);
            condition = getArguments().getString(KEY_CONDITION);
            answer = getArguments().getString(KEY_ANSWER);
            isCompleted = getArguments().getBoolean(KEY_COMPLETE);

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LocalStatistic statistic = repository.findTaskStatistics(StudentActivity.id, SolveHomeworkFragment.id, id);
                            if (statistic != null) {
                                localAnswer = statistic.getAnswer();
                            }
                        }
                    }).start();
                    if (answerTIET != null) {
                        answerTIET.setText(localAnswer);
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solve_task, container, false);

        idTV = view.findViewById(R.id.solve_task_id);
        topicTV = view.findViewById(R.id.solve_task_topic);
        subjectTV = view.findViewById(R.id.solve_task_subject);
        categoryTV = view.findViewById(R.id.solve_task_category);
        conditionTV = view.findViewById(R.id.solve_task_condition);
        answerTIET = view.findViewById(R.id.solve_answer_TIET);
        saveButton = view.findViewById(R.id.save_answer_button);

        if (localAnswer != null) {
            answerTIET.setText(localAnswer);
        }

        idTV.setText("#"+id);
        topicTV.setText(topic);
        subjectTV.setText(subject);
        categoryTV.setText(category);
        conditionTV.setText(condition);

        if (isCompleted) {
            saveButton.setEnabled(false);
        } else {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalStatistic statistic = new LocalStatistic(
                            StudentActivity.id,
                            SolveHomeworkFragment.id,
                            id,
                            answer.equals(answerTIET.getText().toString()),
                            answerTIET.getText().toString());

                    Log.d("INSERT", statistic.toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            repository.insertStatistic(statistic);
                        }
                    }).start();

                    Toast.makeText(getContext(), "Ответ сохранен", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}
