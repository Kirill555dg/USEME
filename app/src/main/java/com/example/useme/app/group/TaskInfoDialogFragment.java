package com.example.useme.app.group;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.useme.R;
import com.example.useme.data.model.Task;

public class TaskInfoDialogFragment extends DialogFragment {

    private Task task;


    public TaskInfoDialogFragment(Task task){
        this.task = task;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.task_info, container, false);

        TextView topicTV = view.findViewById(R.id.task_item_topic);
        TextView idTV = view.findViewById(R.id.task_item_id);
        TextView subjectTV = view.findViewById(R.id.task_item_subject);
        TextView categoryTV = view.findViewById(R.id.task_item_category);
        TextView conditionTV = view.findViewById(R.id.task_item_condition);
        TextView answerTV = view.findViewById(R.id.task_item_answer);

        topicTV.setText("Задача №"+task.getTopicPK().getTopicNumber());
        idTV.setText("#"+task.getId());
        subjectTV.setText(task.getTopicPK().getSubject());
        categoryTV.setText(task.getCategory());
        conditionTV.setText(task.getCondition());
        answerTV.setText(task.getAnswer());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics newDisplayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(newDisplayMetrics);
            dialog.getWindow().setLayout(newDisplayMetrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
